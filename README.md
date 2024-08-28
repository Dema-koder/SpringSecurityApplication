# Spring Security JWT Authentication

Этот проект демонстрирует реализацию аутентификации с использованием JWT (JSON Web Token) в приложении на Spring Boot. Проект включает регистрацию пользователей, аутентификацию и авторизацию на основе токенов.

## Особенности

- **Регистрация пользователей:** Пользователи могут зарегистрироваться, предоставив имя пользователя и пароль.
- **JWT Аутентификация:** Пользователи могут аутентифицироваться, получая access и refresh токены.
- **Обновление токенов:** Пользователи могут обновить свои access токены с помощью refresh токенов.
- **Контроль доступа на основе ролей:** Ограничение доступа к определенным эндпоинтам на основе ролей пользователей.

## Технологии

- **Spring Boot**: Фреймворк для создания приложений на Java.
- **Spring Security**: Обеспечивает аутентификацию и управление доступом.
- **JWT**: Стандарт для создания токенов, подтверждающих запросы между клиентом и сервером.
- **Spring Data JPA**: Работа с базой данных.
- **Postgres Database**: База данных для пользователей.
- **Swagger/OpenAPI**: Документация и тестирование API.
- **Lombok**: Упрощение кода с помощью аннотаций.
- **JUnit & Mockito**: Юнит-тестирование.

## Установка и запуск

### Предварительные требования

- **Java 22**
- **Maven**
## Структура проекта

```bash
.
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── SpringSecurityApplication
│   │   │               ├── SpringSecurityApplication.java
│   │   │               ├── aspect
│   │   │               │   └── LoggingAspect.java
│   │   │               ├── config
│   │   │               │   ├── PasswordEncoderConfig.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   └── SwaggerConfig.java
│   │   │               ├── controllers
│   │   │               │   ├── AdminController.java
│   │   │               │   └── UserController.java
│   │   │               ├── filter
│   │   │               │   └── JwtAuthFilter.java
│   │   │               ├── model
│   │   │               │   ├── RefreshToken.java
│   │   │               │   ├── UserInfo.java
│   │   │               │   └── dto
│   │   │               │       ├── AuthRequestDTO.java
│   │   │               │       ├── JwtResponseDTO.java
│   │   │               │       └── RefreshTokenRequestDTO.java
│   │   │               ├── repository
│   │   │               │   ├── RefreshTokenRepository.java
│   │   │               │   └── UserInfoRepository.java
│   │   │               └── service
│   │   │                   ├── JwtService.java
│   │   │                   ├── RefreshTokenService.java
│   │   │                   ├── UserInfoDetails.java
│   │   │                   └── UserInfoService.java
│   │   └── resources
│   │       ├── application.yaml
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── SpringSecurityApplication
│                       ├── SpringSecurityApplicationTests.java
│                       ├── controllers
│                       │   └── UserControllerTest.java
│                       └── service
│                           ├── RefreshTokenServiceTest.java
│                           └── UserInfoServiceTest.java
```
#### `src/main/java/com/example/SpringSecurityApplication/aspect`
- **LoggingAspect.java**: Содержит компоненты для аспектно-ориентированного программирования (AOP), обеспечивающие логирование вызовов методов, исключений.

#### `src/main/java/com/example/SpringSecurityApplication/config`
- **PasswordEncoderConfig.java**: Конфигурация для бина шифрования паролей, используемого для хеширования паролей пользователей.
- **SecurityConfig.java**: Класс конфигурации Spring Security, определяющий правила аутентификации и авторизации.
- **SwaggerConfig.java**: Конфигурация для интеграции Swagger/OpenAPI в проект, позволяющая документировать и тестировать API.

#### `src/main/java/com/example/SpringSecurityApplication/controllers`
- **AdminController.java**: REST-контроллер, обрабатывающий запросы, специфичные для администраторов.
- **UserController.java**: REST-контроллер для аутентификации пользователей, регистрации и операций с обновлением токенов.

#### `src/main/java/com/example/SpringSecurityApplication/filter`
- **JwtAuthFilter.java**: Фильтр для обработки JWT токенов в HTTP-запросах, их валидации и установки контекста аутентификации.

#### `src/main/java/com/example/SpringSecurityApplication/model`
- **RefreshToken.java**: Класс сущности, представляющий токен обновления в системе.
- **UserInfo.java**: Класс сущности, представляющий информацию о пользователе.
- **dto/**: Содержит объекты передачи данных (DTO), используемые для инкапсуляции данных в API-запросах и ответах.
   - **AuthRequestDTO.java**: DTO для захвата данных запроса на вход в систему.
   - **JwtResponseDTO.java**: DTO для отправки JWT токенов доступа и обновления в ответе.
   - **RefreshTokenRequestDTO.java**: DTO для захвата данных запроса на обновление токена.

#### `src/main/java/com/example/SpringSecurityApplication/repository`
- **RefreshTokenRepository.java**: Интерфейс репозитория для выполнения CRUD-операций с сущностями `RefreshToken`.
- **UserInfoRepository.java**: Интерфейс репозитория для выполнения CRUD-операций с сущностями `UserInfo`.

#### `src/main/java/com/example/SpringSecurityApplication/service`
- **JwtService.java**: Сервисный класс для генерации и валидации JWT токенов.
- **RefreshTokenService.java**: Сервисный класс для управления токенами обновления, включая их создание и валидацию.
- **UserInfoDetails.java**: Класс, реализующий `UserDetails`, используемый Spring Security для аутентификации.
- **UserInfoService.java**: Сервисный класс для управления информацией о пользователях, включая загрузку пользователей по имени и добавление новых пользователей.

### `src/test/java/com/example/SpringSecurityApplication`
- **controllers/**: Содержит юнит-тесты и интеграционные тесты для REST-контроллеров.
   - **UserControllerTest.java**: Тесты для класса `UserController`.
- **service/**: Содержит юнит-тесты для сервисного слоя.
   - **RefreshTokenServiceTest.java**: Тесты для класса `RefreshTokenService`.
   - **UserInfoServiceTest.java**: Тесты для класса `UserInfoService`.
### Запуск приложения

1. **Клонирование репозитория**

   ```bash
   git clone https://github.com/Dema-koder/spring-security-jwt-example.git
   cd spring-security-jwt-example
   ```
2. **Редактирование application.yaml**
   
   Внесите необходимые данные в application.yaml: 
   ```yaml
   spring:
      application:
         name: SpringSecurityApplication
   datasource:
      url: ${POSTGRES_URL}
      driver-class-name: org.postgresql.Driver
      username: ${POSTGRES_USER}
      password: ${POSTGRES_PASS}
   jpa:
      hibernate:
         ddl-auto: create-drop
      show-sql: false
      properties:
         hibernate:
            format_sql: true
   jwt:
      secret: ${SECRET_TOKEN}
      tokenExpiration: 1m
      refreshTokenExpiration: 10m
   ```
   Вставьте свои данные вместо ${POSTGRES_URL}, ${POSTGRES_USER}, ${POSTGRES_PASS} и ${SECRET_TOKEN}. Пример ${SECRET_TOKEN}: `9D0EB6B1C2E1FAD0F53A248F6C3B5E4E2F6D8G3H1I0J7K4L1M9N2O3P5Q0R7S9T1U4V2W6X0Y3Z`
3. **Сборка приложения**
    ```bash
   mvn clean install
   ```
4. **Запуск приложения**
    ```bash
   mvn spring-boot:run
   ```
5. **Доступ к приложению**

    Приложение будет доступно по адресу http://localhost:8080.
6. **Swagger UI**

   Документация API доступна по адресу http://localhost:8080/swagger-ui/index.html.

### API Эндпоинты

Аутентификация

- `POST /api/register`: Регистрация нового пользователя.
- `POST /api/login`: Аутентификация пользователя и получение access и refresh токенов.

Защищенные эндпоинты

- `GET /api/user`: Доступно только пользователям с ролью USER. Требуется Bearer токен.
- `GET /api/admin`: Доступно только пользователяс с ролью ADMIN. Требуется Bearer токен.
- `POST /api/refreshToken`: Обновление access токена с использованием refresh токена.

### Запуск тестов
Юнит-тесты написаны с использованием JUnit и Mockito. Для запуска тестов выполните:
```bash
mvn test
```

### Тестирования приложения с помощью Swagger
После запуска приложения перейдите по ссылке http://localhost:8080/swagger-ui/index.html. 

Воспользуйтесь эндпоинтом `/api/register` чтобы зарегестрировать пользователя в системе. Укажите роли, которые будут у 
пользователя(USER, ADMIN), разделенные только запятой :

![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.44.47.png)
![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.45.02.png)

С помощью эндпоинта `/api/login` можно получить access и refresh токены:

![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.49.32.png)
![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.49.46.png)

Теперь когда у вас есть access токен то можно авторизироваться использую кнопку Authorize, туда нужно ввести полученный access токен

![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.51.26.png)
![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.51.32.png)

После этого вам будут доступны эндпоинты `/api/user` и `/api/admin`. Если права у пользователя USER то ему на запрос `GET /api/user` придет респонс Hello World!
Если у пользователя есть роль ADMIN то в ответ на запрос `GET /api/admin` придет то же сообщение.  

![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.51.47.png)
В случае же если вы получается отправить запрос на эндпоинт права на который у вас нет, то вы увидите что то похожее:
![Alt text](/images/Снимок%20экрана%202024-08-28%20в%2014.52.17.png)