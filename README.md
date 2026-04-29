# NovaBank Spring Boot
## Introducción
NovaBank es una API REST desarrollada con Spring Boot para la gestión de un sistema
bancario básico. Permite la administración de clientes, cuentas bancarias y la realización 
de operaciones financieras (depósitos, retiros y transferencias), se garantiza la consistencia
de los datos y la seguridad de los mismos a través de autentificación JWT.
## Tecnologías Utilizadas
* **Java 17**
* **Spring Boot 3**: Se ha usado Web, Data JPA, Security y validation.
* **Spring Security & JWT**: Autentificación y Autorización.
* **PostgreSQL**: Base de datos principal.
* **H2 Database**: Utilizada para el Testing como memoria.
* **JUnit 5 & Mockito**: Testing Unitario y de Integración.
* **Swagger / OpenAPI**: Documentación de la API.
*  **Lombok**: Reducción de código boilerplate.
* **Maven 3.6+**: Gestión de dependencias.

## Arquitectura y Principios
El proyecto sigue una arquitectura multicapa y respeta los principios **Solid**, 
lo que garantiza un código limpio, escalable y mantenible. Se ha implementado **DTO** para
aislar el modelo de la base de datos de los datos expuestos en los endpoints.

├── controller: Puntos de entrada a la API.   
├── repository: Acceso a datos (Spring Data JPA)  
├── model: Entidades de base de datos  
├── config  
├── dto: Objetos de transferencia de datos.  
├── exception  
├── mapper  
├── security  
└── service: Lógica de negocio e interfaz.  

Además, al incluir el controlador de excepciones global, `GlobalExceptionHandler` sirve para estandarizar las respuestas de error, `401`, `403`,`404`, etc.

## Configuración Base de Datos
Asegurarse de tener PostgreSQL instalado y ejecutándos. Actualizamos el archivo `src/main/resources/application.yml`
con las credenciales:
* **Gestor de base de datos**: PostgreSQL.
* **Configuración clave**:
```bash
spring:
  application:
    name: novabank-api

  datasource:
    url: jdbc:postgresql://localhost:5432/novabank
    username: postgres
    password: tu_contraseña_aqui  # Asegúrate de que coincida con tu Postgres local
    driver-class-name: org.postgresql.Driver

  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  # Esto ayuda a que el GlobalExceptionHandler pueda capturar mejor los mensajes

  mvc:
    throw-exception-if-no-handler-found: true
  web:
    resources:
      add-mappings: true
server:
  port: 8081 # Asegurate de que este es tu puerto, de normal puede ser 8080

  error:
    include-message: always # Para que tus excepciones personalizadas muestren el mensaje en el JSON
jwt:
  secret: "qwertyuiopasdfghjklzxcvbnm123456qwertyuiopasdfghjklzxcvbnm123456"
  expiration: 86400000   # 24 horas
  
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: alpha # Ordena los endpoints alfabéticamente (más limpio)
  api-docs:
    path: /v3/api-docs
```

## Ejecución de la Aplicación
Para levantar la API, se puede usar la terminal y poner:
```bash
mvn spring-boot:run
```
La aplicación está disponible (si no se ha cambiado el puerto), desde 
```bash
http://localhost:8081
```
## Documentación y seguridad
La API está documentada de forma interactiva con **Swagger UI**. Para tener acceso:
```bash
http://localhost:8081/swagger-ui.html
```
Como hacer uso de la aplicación bien en **Postman (recomendado)** o con **Swagge**.

1. Localizamos le **endpoint** `POST /login`, usamos las credenciales designadas para el proyecto `"username":admin`, `"password":password`.
2. Se autogenera un **Token JWT** que será el que debemos de usar para Autorizar el resto de endpoints
3. En **Swagger**, en la parte superior lo pegamos en authorize, con el siguiente formato, respetando el espacio y sin comillas.
```bash
Bearer <Token>
```
Mientras, en **Postman** buscamos la pestaña authorization y pegamos el token.
4. Una vez Autorizado podremos probar el resto de la API.

## Testing
Se ha implementado una estrategia de Testing exhaustiva dividida en 4 niveles para asegurar la fiabilidad de las operaciones bancarias:
1. **Test Unitarios**: Lógica de negocio de los servicios (Mockito).
2. **Test de Repositorios**: Consultas y persistencia (H2 Database).
3. **Test de Controlador**: Endpoints, códigos HTTP y seguridad (MockMvc).
4. **Test de Integración**: Flujo completo desde el controlador a la base de datos real.

Para llevar a cabo toda la ejecución de los test:
```bash
mvn test
```

![TodosLosTestPasados](img.png)

## Arquitectura basada en principios **SOLID**
El proyecto sigue una arquitectura multicapa basada en los principios SOLID:
1. Controller: Gestiona las peticiones HTTP y validación de entrada.
2. Service: Implementa la lógica de negocio e interfaces.
3. Repository: Capa de acceso a datos.
4. Model y DTO: Entidades de persistencia y objetos de transferencia de datos.
5. Exception: Manejo global de errores y excepciones personalizadas.


---
# Información Importante
## Autor
Rubén Manuel Rodríguez Chamorro, estudiante de Ingeniería de la Salud, especializado en Bioinformática es el dueño y autor
de este proyecto, realizado en la empresa NTT-DATA elaborado para el completar el plan formativo.

---

## Aviso legal y Licencia de uso
Este proyecto ha sido desarrollado exclusivamente con **fines educativos y académicos**, como parte del programa de formación 
**NTT-DATA**.

El código contenido en este repositorio no está diseñado ni testado para su uso en entornos de producción reales, especialmente en sistemas 
financieros críticos. El autor no se hace responsable del mal uso de esta aplicación.

Queda prohibida la reproducción total o parcial, publicación, modificación o distribución de este software con fines comerciales sin el consentimiento explícito y por escrito del autor.
Todos los derechos están reservados. El uso de este material fuera del ámbito evaluativo de la formación backend debe ser consultado previamente.