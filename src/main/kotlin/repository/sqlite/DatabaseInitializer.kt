package repository.sqlite

import org.springframework.stereotype.Component
import repository.sqlite.scipts.init
import java.io.File

@Component
class DatabaseInitializer {
    private val dbFile = File("top-secret.db")

    fun ensureInitialized() {
        if (dbFile.exists()) {
            return
        }
        init()
    }
}

