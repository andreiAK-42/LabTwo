package repository.sqlite

import models.Resource
import models.ResourceAccess
import models.ResponseCode
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import kotlin.collections.toTypedArray

private val url = "jdbc:sqlite:top-secret.db"

fun getResourceByName(name: String): Pair<Resource?, ResponseCode> {
    val connection: Connection = DriverManager.getConnection(url)

    try {
        val resourceInfo = getResourceInfo(connection, name) ?: return Pair(null, ResponseCode.BAD_RESOURCE)
        val accessList = getResourceAccess(connection, name)
        val children = getChildResources(connection, name)

        val resource = Resource(
            name = resourceInfo.name,
            value = resourceInfo.value,
            accessList = accessList.toTypedArray(),
            resources = if (children.isEmpty()) null else children.toTypedArray()
        )
        return Pair(resource, ResponseCode.SUCCESS)
    } catch (e: Exception) {
        return Pair(null, ResponseCode.SQL_REQUEST_ERROR)
    } finally {
        connection.close()
    }
}

fun getResourceByPath(pathParts: List<String>): Pair<Resource?, ResponseCode> {
    if (pathParts.isEmpty()) return Pair(null, ResponseCode.BAD_RESOURCE)

    var currentResource: Resource? = null

    for (part in pathParts) {
        val (returnedResource, responseCode) = getResourceByName(part)
        if (responseCode != ResponseCode.SUCCESS || returnedResource == null) {
            return Pair(null, responseCode)
        }
        currentResource = returnedResource

        if (currentResource.name != part) {
            return Pair(null, ResponseCode.BAD_RESOURCE)
        }
    }

    return Pair(currentResource, ResponseCode.SUCCESS)
}

fun updateResourceValue(resourceName: String, newValue: Int): ResponseCode {
    val connection: Connection = DriverManager.getConnection(url)

    return try {
        val sql = "UPDATE resource SET value = ? WHERE name = ?"
        val statement: PreparedStatement = connection.prepareStatement(sql)
        statement.setInt(1, newValue)
        statement.setString(2, resourceName)

        val updatedRows = statement.executeUpdate()

        if (updatedRows > 0) {
            ResponseCode.SUCCESS
        } else {
            ResponseCode.BAD_RESOURCE
        }
    } catch (e: Exception) {
        ResponseCode.SQL_REQUEST_ERROR
    } finally {
        connection.close()
    }
}

private data class ResourceInfo(val name: String, val value: Int)

private fun getResourceInfo(connection: Connection, name: String): ResourceInfo? {
    val sql = "SELECT name, value FROM resource WHERE name = ?"
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, name)

    val resultSet = statement.executeQuery()
    return if (resultSet.next()) {
        ResourceInfo(
            name = resultSet.getString("name"),
            value = resultSet.getInt("value")
        )
    } else {
        null
    }
}

private fun getResourceAccess(connection: Connection, resourceName: String): List<ResourceAccess> {
    val sql = "SELECT user_login, access_mode FROM resource_access WHERE resource_name = ?"
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, resourceName)

    val resultSet = statement.executeQuery()
    val accessList = mutableListOf<ResourceAccess>()

    while (resultSet.next()) {
        accessList.add(
            ResourceAccess(
                userLogin = resultSet.getString("user_login"),
                access = resultSet.getString("access_mode")
            )
        )
    }

    return accessList
}

private fun getChildResources(connection: Connection, parentName: String): List<Resource> {
    val sql = "SELECT name FROM resource WHERE parent_name = ?"
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, parentName)

    val resultSet = statement.executeQuery()
    val children = mutableListOf<Resource>()

    while (resultSet.next()) {
        val childName = resultSet.getString("name")
        val (childResource, responseCode) = getResourceByName(childName)
        if (responseCode == ResponseCode.SUCCESS && childResource != null) {
            children.add(childResource)
        }
    }

    return children
}

fun userExists(login: String): Pair<Boolean, ResponseCode> {
    val connection: Connection = DriverManager.getConnection(url)

    return try {
        val sql = "SELECT COUNT(*) as count FROM user WHERE login = ?"
        val statement: PreparedStatement = connection.prepareStatement(sql)
        statement.setString(1, login)

        val resultSet = statement.executeQuery()
        resultSet.next()
        val count = resultSet.getInt("count")

        Pair(count > 0, ResponseCode.SUCCESS)
    } catch (e: Exception) {
        Pair(false, ResponseCode.SQL_REQUEST_ERROR)
    } finally {
        connection.close()
    }
}

fun resourceExists(name: String): Pair<Boolean, ResponseCode> {
    val connection: Connection = DriverManager.getConnection(url)

    return try {
        val sql = "SELECT COUNT(*) as count FROM resource WHERE name = ?"
        val statement: PreparedStatement = connection.prepareStatement(sql)
        statement.setString(1, name)

        val resultSet = statement.executeQuery()
        resultSet.next()
        val count = resultSet.getInt("count")

        Pair(count > 0, ResponseCode.SUCCESS)
    } catch (e: Exception) {
        Pair(false, ResponseCode.SQL_REQUEST_ERROR)
    } finally {
        connection.close()
    }
}

