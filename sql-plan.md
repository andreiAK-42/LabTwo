# Описание сущностей, атрибутов и связей:
## 1. User
		login (string, pk)
		password (string)
## 2. Resource:
		id(string):	
		name (string)
		value (int)
		parent_id (string, fk)
## 3. ResourceAccess:
		id (int)
		resource_id (string, fk)
		user_login (string, fk)
		access_mode (string)
## Связи:
User <-> ResourceAccess

Resource <-> ResourceAccess

Resource <-> Resource

# ER-диаграмма:
```mermaid
erDiagram
    USER {
        string login PK
        string password
    }

    RESOURCE {
        string id PK
        string name
        int value
        string parent_id FK
    }

    RESOURCE_ACCESS {
        int id PK
        string resource_id FK
        string user_login FK
        string access_mode
    }

    USER ||--o{ RESOURCE_ACCESS : "has_access_to"
    RESOURCE ||--o{ RESOURCE_ACCESS : "has_defined_access"
    RESOURCE }o--|| RESOURCE : "is_child_of"
```
# Техническая часть
БД: H2.

Драйвер: JDBC.

Управление: Ручное управление соединениями.

Инициализация и заполение БД будет выполнятся за счет sql скриптов.
# Изменения в коде
WIP
