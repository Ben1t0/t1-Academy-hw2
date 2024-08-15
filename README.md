# Задание 2 открытой школы T1

## Spring Boot Starter, который предоставляет возможность логировать HTTP запросы в приложении на базе Spring Boot.

### Описание проекта
При подключении к проекту стартер логгирует все входящие и исходящии HTTP запросы, лог включает в себя 
- Метод запроса
- URL
- Заголовки запроса и ответа (Опционально)
- Код ответа
- Время обработки запроса
- Тело запроса и ответа при наличии (Опционально)

Логирование реализовано на базе ServletFilter - OncePerRequestFilter
Автоконфигурация стартера создает бин фильтра по условиям @ConditionalOnProperty и @ConditionalOnWebApplication

### Инструкции по запуску и настройке
Для установки стартера ввести команду
```
mvn clean install
```
Данная команда установит в локальный maven репозиторий артефакт
Для подключения добавить в целевом проекте в pom.xml следующую зависимость:
```xml
<dependency>
    <groupId>ru.t1-academy.java</groupId>
    <artifactId>hw2</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Для включения логгирования в целевом проекте добавить параметр:
```yaml
web-logging-starter:
  enabled: true
```

Для включения логирования заголовков и тела запроса
```yaml
web-logging-starter:
  enabled: true
  include:
    - body #включение тела запроса
    - headers #включение заголовков запроса
```

Для удобства добавлены метаданные параметров конфигурации в файле
- additional-spring-configuration-metadata.json

### Пример логов
При включении логирования на каждый запрос будет выводиться сообщение следующего вида:
```text
-----------------------------
>>>>>>>>>>REQUEST>>>>>>>>>>>
METHOD: POST
URI: /api/v1/users
----------HEADERS----------
HEADER content-type : application/json
HEADER user-agent : PostmanRuntime/7.41.1
HEADER accept : */*
HEADER postman-token : de3bb978-951d-46ad-bdfc-435199f02963
HEADER host : localhost:8080
HEADER accept-encoding : gzip, deflate, br
HEADER connection : keep-alive
HEADER content-length : 68
BODY:  {
BODY:      "name":"John Smith",
BODY:      "email":"JohnSmith007@server.com"
BODY:  }
-----------------------------
Execution time: 155
<<<<<<<<<<RESPONSE<<<<<<<<<<
STATUS: 201
----------HEADERS----------
HEADER Content-Type : application/json
HEADER Content-Length : 61
HEADER Date : Thu, 15 Aug 2024 08:27:44 GMT
HEADER Keep-Alive : timeout=60
HEADER Connection : keep-alive
BODY:  {"id":4,"name":"John Smith","email":"JohnSmith007@server.com"}
-----------------------------
```

### Тестирование
- Тестирование проводилось на Задании 1 с подключенной зависимостью из Задания 2 
[Ветка первого задания с подключенной зависимостью](https://github.com/Ben1t0/t1-Academy-hw1/tree/use-hw2-starter)