# Toma Chocolate - API

> "Toma chocolate, paga lo que debes."

¡Bienvenido al repositorio del backend de **Toma Chocolate**! Esta es la API REST de una aplicación diseñada para simplificar y optimizar la división de gastos grupales en juntadas, asados o eventos. El objetivo principal es ofrecer una experiencia de usuario sumamente sencilla, rápida y sólida pero sin dejar de lado funcionalidades en el proceso.

Este proyecto provee los servicios necesarios para la interfaz web: [TomaChocolate-Front](https://github.com/Zadios/TomaChocolate-Front).


## Características Principales

- **Gestión Centralizada de Datos:** Procesa y persiste de manera eficiente la información de juntadas, participantes y gastos asociados mediante un modelo relacional.
- **Cálculo Inteligente de Saldos:** Incorpora un algoritmo optimizado en el servidor que automatiza la división de cuentas, minimizando la cantidad de transferencias cruzadas necesarias.
- **Exposición de Servicios REST:** Endpoints desacoplados, limpios y configurados con políticas de CORS para una integración nativa con interfaces de usuario modernas.
- **Arquitectura Robusta:** Manejo global de excepciones para garantizar respuestas estructuradas ante errores y validación estricta de datos mediante DTOs.


## Tecnologías Utilizadas

El backend fue desarrollado bajo estándares modernos del ecosistema de Java:

* **Java 21** (Aprovechando las últimas características estables del lenguaje).
* **Spring Boot 3.x** (Framework principal para la creación de la API REST y la inyección de dependencias).
* **Spring Data JPA / Hibernate** (Para el mapeo de entidades y abstracción de la base de datos).
* **MySQL** (Base de datos relacional para la persistencia del estado de las juntadas y gastos).
* **Lombok** (Para la reducción de código repetitivo/boilerplate).
* **Maven** (Como herramienta de automatización de construcción y ciclo de vida del proyecto).
* **Swagger / OpenAPI** (Para la documentación, prueba y exposición interactiva de los endpoints).


## Configuración local (clonar proyecto en otra computadora)

#### 1\. Clonar el repositorio

```bash
git clone https://github.com/Zadios/TomaChocolate-API.git
cd TomaChocolate-API
```

#### 2\. Configurar la base de datos:
Asegurate de tener corriendo una instancia de MySQL y creá una base de datos llamada tomachocolate_db. Luego, verificá tus credenciales en el archivo src/main/resources/application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tomachocolate_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

#### 3\. Levantar la aplicación:

Podés compilar y correr el servidor utilizando el wrapper de Maven desde la raíz del proyecto:

```text
./mvnw spring-boot:run
```
La API estará disponible localmente en http://localhost:8080.

#### 4\. Documentación de la API (Swagger):
Una vez que el servidor esté corriendo, podés acceder a la interfaz interactiva para probar los endpoints en:
http://localhost:8080/swagger-ui/index.html


## Estructura del Proyecto

El backend sigue una arquitectura por capas estandarizada para Spring Boot, promoviendo el desacoplamiento y la fácil mantenibilidad:

```text
src/main/java/com/tomachocolate/api/
├── config/       # Configuraciones globales de la app (CORS, seguridad, etc.)
├── controller/   # Endpoints de la API REST (Meetings, Expenses, Participants)
├── dto/          # Objetos de Transferencia de Datos (Requests, Responses y Payloads)
├── exception/    # Manejo global de excepciones y respuestas estructuradas de error
├── model/        # Entidades del dominio para la persistencia con JPA / Hibernate
├── repository/   # Capa de acceso a datos (Spring Data Repositories)
└── service/      # Lógica de negocio pura (Algoritmo de optimización de saldos)
```
#### Detalle de Paquetes Clave:
* **config/WebConfig.java:** Centraliza las políticas de CORS, permitiendo la comunicación segura y nativa con el frontend de React (http://localhost:5173).

* **dto/:** Protege el modelo de la base de datos utilizando DTOs (como MeetingRequest o ParticipantUpdateDTO) para tipar estrictamente los datos que ingresan y egresan de la API.

## Desarrollador
- Ariel Viscovich - [LinkedIn](https://www.linkedin.com/in/arielviscovich)
