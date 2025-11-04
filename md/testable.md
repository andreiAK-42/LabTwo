# Анализ тестопригодности и рефакторинг
Что пришлось изменить в проекте для повышения тестопригодности:

Для обеспечения возможности модульного тестирования класса `ResourceManager`, его зависимость от `AccessControlService была` вынесена в конструктор.

Также из методов класса были убраны вызовы exitProcess(), которые немедленно завершали программу. 
Вместо этого методы были переписаны так, чтобы возвращать результат своей работы (данные или код ошибки).

## Примеры проверяемых сценариев:
### UserAuthenticationTests:


### ResourceManagerTests:
`tryGetResource should return resource on valid path and sufficient volume():` Проверяет успешное получение существующего ресурса при запросе корректного объема.

`tryGetResource should return BadResource on invalid path():` Убеждается, что система возвращает ошибку, если запрошен ресурс по несуществующему пути.

`tryGetResource should return BigValue when requested volume is too large():` Тестирует случай, когда ресурс найден, но запрашиваемый объем превышает доступный.

`tryDoAction should return Success when access is granted():` Проверяет, что действие (например, WRITE) успешно выполняется, 
если mock-объект AccessControlService возвращает разрешение на доступ. Также проверяется, что объем ресурса был корректно списан.

`tryDoAction should return NotAccess when access is denied():` Проверяет, что система корректно отказывает в доступе, если mock-объект 
AccessControlService возвращает запрет. Важной частью теста является проверка того, что объем ресурса не изменился.