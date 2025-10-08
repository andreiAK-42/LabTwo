# Анализ проекта и план рефакторинга

## Нарушения SOLID принципов
### Принцип Единственной Ответственности (Single Responsibility Principle, SRP)
1. Файл `GlobalVariables.kt` отвечает за хранение константы (соль), структуры данных и тестовых данных одновременно. 
Следует вынести константы в отдельный файл `AppConfig.kt` и тестовые данные в файл `TestData.kt` (лист пользователей, создание ресурсов)

2. Файл `Utils.kt` содержит множество несвязных функций: Хэширование, аутентификация, работа с ресурсами и проверка прав
Следует разбить файл на специализированные классы:
- `PasswordHasher` - хэширование паролей
- `UserAuthenticator` - аутентификация пользователей
- `ResourceManager` - управление ресурсами
- `AcessControlService` - контроль доступа
 
### Принцип разделения интерфейса (Interface Segregation Principle, ISP)
  1. В коде Отсутствуют интерфейсы. Из за этого код сильно связан, что затрудняет его тестирование и дальнейшие модификации
     Следует ввести интерфейсы для ключевых компонентов
```
    interface UserRepository {
    fun findByLogin(login: String): User?
    }
    
    interface ResourceRepository {
    fun findByPath(path: String): Resource?
    fun save(resource: Resource)
    }

    interface AccessControl {
    fun canPerformAction(user: User, resource: Resource, action: Action): Boolean
    }
 ```

  ### Принцип инверсии зависимостей (Dependency Inversion Principle, DIP)
  Функции `tryGetUser()` и `tryGetResource()` напрямую зависят от глобальных переменных `UserStorage`, `MainResource`.
  Следует внедрить зависимости через конструкторы
```
  class UserService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher
  ) {
    fun authenticate(login: String, password: String): User {
        val user = userRepository.findByLogin(login) ?: throw AuthenticationException()
        if (user.password != passwordHasher.hash(password)) {
            throw AuthenticationException()
        }
        return user
    }
}
```
     
