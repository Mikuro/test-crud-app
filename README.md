# test-crud-app

Сервис с CRUD операциями над счетчиком

# Процессы:

## Добавление нового счетчика с указанным именем
1. Данный метод отвечает только за создание, но не изменение имеющегося.

## Получение значения счетчика с указанным именем

## Увеличение значения счетчика с указанным именем, возвращает его новое значение

## Удаление счетчика с указанным именем

## Получение всех счетчиков (список имен и значений)
1. Имеется ограничение вычитки ktor.routing.counterLimit = 20. При значении 0 ограничения нет.

## Обработка ошибок
 - Заранее определены 3 категории ошибок ошибка валидации запроса, техническая ошибка процесса и непредвиденная ошибка в ExceptionGenerator
 - Класс AppException предлагает единый стандарт ошибок: 
<p>locationId - место вызова исключения в коде
<p>code - код ошибки по таблице
<p>message - соответствующее сообщение<p>

## Логгирование
1. Добавлены businessThread(название бизнес процесса), и requestId(айди экземпляра процесса)
2. Данные выведены в MDC context в логах.


## Запросы и ответы
- Используется единые обертки RequestItem(дополнительная информация Meta и данные запроса Data) и ResponseItem (Время ответа, статус успех или провал, данные ответа)

## Exceptions:

| Code  | Description              |
|:------|:-------------------------|
| ER001 | Ошибка валидации запроса |
| ER002 | Техническая ошибка       |
| ER003 | Непредвиденная ошибка    |

## Настройки окружения для запуска проекта на локальной машине:
- для запуска необходимо ввести <pre><code>docker-compose up</pre></code> в корне директории
- самоподписанный сертификат также находится к орне директории
- для создания таблицы и ее первичного наполнения использован liquibase
- коллекция для Postman находится в postman_collection
- OpenAPI спецификация в api_spec