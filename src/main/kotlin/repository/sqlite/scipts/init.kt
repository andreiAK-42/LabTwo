package repository.sqlite.scipts

import localStorage.MainResource
import localStorage.UserStorage
import models.Resource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

fun init() {
    val url = "jdbc:sqlite:top-secret.db"
    val connection: Connection = DriverManager.getConnection(url)

    initTables(connection)
    feelTables(connection)
    connection.close()
}

private fun initTables(connection: Connection) {
    val statement: Statement = connection.createStatement()

    statement.execute("""
        CREATE TABLE IF NOT EXISTS user (
            login TEXT PRIMARY KEY,
            password TEXT NOT NULL
        );
    """)

    statement.execute("""
        CREATE TABLE IF NOT EXISTS resource (
            name TEXT PRIMARY KEY,
            value INTEGER NOT NULL,
            parent_name TEXT,
            FOREIGN KEY(parent_name) REFERENCES resource(name)
        );
    """)

    statement.execute("""
        CREATE TABLE IF NOT EXISTS resource_access (
            id INTEGER PRIMARY KEY,
            resource_name TEXT NOT NULL,
            user_login TEXT NOT NULL,
            access_mode TEXT NOT NULL,
            FOREIGN KEY(resource_name) REFERENCES resource(name),
            FOREIGN KEY(user_login) REFERENCES user(login)
        );
    """)
}

private fun feelTables(connection: Connection) {
    addUsers(connection)
    addResources(connection, MainResource, null)
}

private fun addUsers(connection: Connection) {
    val insertUserSQL = "INSERT INTO user (login, password) VALUES (?, ?)"
    val preparedStatement = connection.prepareStatement(insertUserSQL)

    UserStorage.forEach { user ->
        preparedStatement.setString(1, user.login)
        preparedStatement.setString(2, user.password)
        preparedStatement.addBatch()
    }

    preparedStatement.executeBatch()
}

private fun addResources(connection: Connection, resource: Resource, parentId: String?) {
    insertResource(connection, resource, parentId)

    resource.resources?.forEach { childResource ->
        addResources(connection, childResource, resource.name)
    }
}

private fun insertResource(connection: Connection, resource: Resource, parentId: String?) {
    val insertResourceSQL = "INSERT INTO resource (name, value, parent_name) VALUES (?, ?, ?)"
    val resourceStatement = connection.prepareStatement(insertResourceSQL)

    resourceStatement.setString(1, resource.name)
    resourceStatement.setInt(2, resource.value)
    resourceStatement.setString(3, parentId)
    resourceStatement.execute()

    insertResourceAccess(connection, resource)
}

private fun insertResourceAccess(connection: Connection, resource: Resource) {
    val insertAccessSQL = "INSERT INTO resource_access (resource_name, user_login, access_mode) VALUES (?, ?, ?)"
    val accessStatement = connection.prepareStatement(insertAccessSQL)

    resource.accessList.forEach { access ->
        accessStatement.setString(1, resource.name)
        accessStatement.setString(2, access.userLogin)
        accessStatement.setString(3, access.access)
        accessStatement.addBatch()
    }

    accessStatement.executeBatch()
}