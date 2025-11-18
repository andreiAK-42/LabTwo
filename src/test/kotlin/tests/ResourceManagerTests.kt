package tests.kotlin.tests

import repository.sqlite.scipts.MainResource
import models.ResponseCode
import models.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import services.ResourceManager
import services.AccessControlService


class MockAccessControlService : AccessControlService() {
    var hasAccess = true

    override fun checkAccess(userAccess: String?, needAccess: Int): ResponseCode {
        return if (hasAccess) ResponseCode.SUCCESS else ResponseCode.NOT_ACCESS
    }
}

class ResourceManagerTests {

    private lateinit var mockAccessControlService: MockAccessControlService
    private lateinit var resourceManager: ResourceManager
    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        mockAccessControlService = MockAccessControlService()
        resourceManager = ResourceManager(mockAccessControlService)
        testUser = User("testUser", "testPass")
        MainResource.value = 250
        MainResource.resources?.find { it.name == "A8B" }?.value = 56
    }

    @Test
    fun `tryGetResource should return resource on valid path and sufficient volume`() {
        val (resource, code) = resourceManager.tryGetResource("A.A8B", 10)
        assertEquals(ResponseCode.SUCCESS, code)
        assertNotNull(resource)
        assertEquals("A8B", resource?.name)
    }

    @Test
    fun `tryGetResource should return BadResource on invalid path`() {
        val (resource, code) = resourceManager.tryGetResource("A.X.Y", 10)
        assertEquals(ResponseCode.BAD_RESOURCE, code)
    }

    @Test
    fun `tryGetResource should return BigValue when requested volume is too large`() {
        val (resource, code) = resourceManager.tryGetResource("A", 300)
        assertEquals(ResponseCode.BIG_VALUE, code)
        assertNotNull(resource)
    }

    @Test
    fun `tryGetResource should return BadValue when volume is negative`() {
        val (resource, code) = resourceManager.tryGetResource("A", -5)
        assertEquals(ResponseCode.BAD_RESOURCE_OR_VALUE, code)
    }


    @Test
    fun `tryDoAction should return Success when access is granted for WRITE`() {
        mockAccessControlService.hasAccess = true
        val (resource, _) = resourceManager.tryGetResource("A", 10)
        val initialValue = resource!!.value

        val code = resourceManager.tryDoAction(resource, testUser, "WRITE", 10)

        assertEquals(ResponseCode.SUCCESS, code)
        assertEquals(initialValue - 10, resource.value)
    }

    @Test
    fun `tryDoAction should return NotAccess when access is denied`() {
        mockAccessControlService.hasAccess = false
        val (resource, _) = resourceManager.tryGetResource("A", 10)
        val initialValue = resource!!.value

        val code = resourceManager.tryDoAction(resource, testUser, "WRITE", 10)

        assertEquals(ResponseCode.NOT_ACCESS, code)
        assertEquals(initialValue, resource.value)
    }

    @Test
    fun `tryDoAction should return GetReport for READ action when access is granted`() {
        mockAccessControlService.hasAccess = true
        val (resource, _) = resourceManager.tryGetResource("A", 10)

        val code = resourceManager.tryDoAction(resource!!, testUser, "READ", 10)

        assertEquals(ResponseCode.GET_REPORT, code)
    }

    @Test
    fun `tryDoAction should return BadAction for an invalid action string`() {
        val (resource, _) = resourceManager.tryGetResource("A", 10)

        val code = resourceManager.tryDoAction(resource!!, testUser, "DELETE_THIS", 10)

        assertEquals(ResponseCode.BAD_ACTION, code)
    }
}