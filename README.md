# FriendsApp RESTful API
Серверная часть выпускной квалифиационной работы по теме "Разработка мобильного приложения FriendsApp для организации совместного досуга."
## Стэк технологий:
- Gradle
- Java 14
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Security (JWT)
- Swagger (OpenAPI 3.0)
- Postgresql
- MapStruct
- Lombok
- Junit 5
- Mockito

## Сборка приложения:
Установите OpenJDK 14
Установите gradle: https://https://gradle.org/install/
Настройте базу данных Postgresql
Введите учетные данные для сервера базы данных и access-token, secret-key для интерации с социальной сетью "ВКонтакте" в application.yml
```shell
# Загрузите и распакуйте архив проектом, либо склонируйте из репозитория с помощью Git: 
git clone https://github.com/Akvamarinika/friendsappserver.git

# Выполните в корневой папке: 
gradle build

# Запустите приложение: 
gradle bootRun
```
Откройте в браузере Swagger: http://localhost:9000/swagger-ui/index.html

## OpenAPI:
1. Откройте адрес в браузере http://localhost:9000/swagger-ui/index.html
2. Выполните регистрацию пользователя http://localhost:9000/swagger-ui/index.html#/authentication-rest-controller/registration
3. Скопируйте токен и авторизируетесь в Swagger UI. После чего token будет вставлен автоматически в запросы.

## OpenAPI (screenshots):
<img src="https://i.ibb.co/cFDwgfq/0000.png" alt="0000" border="0">
<img src="https://i.ibb.co/Xt7qfzm/1111.png" alt="1111" border="0">
<img src="https://i.ibb.co/nM7MxjR/2222.png" alt="2222" border="0">
<img src="https://i.ibb.co/f2dNPn7/3333.png" alt="3333" border="0">

## Пример ответа в формате JSON (screenshots):
<img src="https://i.ibb.co/qCNvZDr/json.png" alt="json" border="0">
