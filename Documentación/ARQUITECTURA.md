# Arquitectura del Proyecto PichangApp

## Tabla de Contenidos
- [1. Visión General](#1-visión-general)
- [2. Estructura del Repositorio](#2-estructura-del-repositorio)
- [3. Detalle de cada Microservicio](#3-detalle-de-cada-microservicio)
- [4. Frontend Android](#4-frontend-android)
- [5. Patrones de Diseño Utilizados](#5-patrones-de-diseño-utilizados)
- [6. Base de Datos](#6-base-de-datos)
- [7. Seguridad](#7-seguridad)

## 1. Visión General

El sistema completo funciona mediante un API Gateway que expone de forma centralizada la comunicación hacia los diversos microservicios backend. La persistencia se realiza de forma separada utilizando el principio *Database per Service*.

```text
               +-------------------+
               |    App Android    |
               | (Kotlin/Compose)  |
               +---------+---------+
                         |  (HTTPS / REST)
                         v
               +-------------------+
               |    API Gateway    |
               |  (Spring Cloud)   |
               +---------+---------+
                         |
       +-----------------+-----------------+-----------------+
       |                 |                 |                 |
       v                 v                 v                 v
+-------------+   +-------------+   +-------------+   +-------------+
|users-service|   |karma_service|   |events-service|  |notification-|
|(Spring Boot)|   |(Spring Boot)|   |(Spring Boot)|   |   service   |
+------+------+   +------+------+   +------+------+   +------+------+
       |                 |                 |                 |
       v                 v                 v                 v
+-------------+   +-------------+   +-------------+   +-------------+
| MySQL       |   | MySQL       |   | MySQL       |   | MySQL       |
| (users)     |   | (karma)     |   | (events)    |   | (notif)     |
+-------------+   +-------------+   +-------------+   +-------------+
```

## 2. Estructura del Repositorio

El repositorio está organizado dividiendo las capas de negocio y la documentación de la siguiente forma:

- **`Producto/Backend/users-service/`** — Microservicio encargado de la autenticación, generación de tokens JWT, manejo de perfiles y validación de correos electrónicos.
- **`Producto/Backend/karma_service/`** — Microservicio que implementa la lógica de reputación del usuario (Karma), calculando puntos a partir de asistencias, inasistencias y validaciones.
- **`Producto/Backend/events-service/`** — Microservicio que maneja el ciclo de vida de los eventos deportivos, inscripciones de jugadores, búsquedas geolocalizadas y cancelación.
- **`Producto/Backend/notification-service/`** — Microservicio que gestiona el registro de tokens de dispositivos y envía alertas asíncronas vía Firebase Cloud Messaging (FCM) o WebSockets en tiempo real.
- **`Producto/Backend/api-gateway/`** — Puerta de entrada a los servicios backend. Realiza ruteo hacia los distintos puertos en un ambiente local y a las URLs correspondientes en Railway.
- **`Producto/front-end/PichangApp/`** — Aplicación móvil nativa en Kotlin empleando Jetpack Compose para la capa UI, siguiendo Clean Architecture.
- **`Documentación/`** — Contiene manuales, el plan de pruebas, las guías de despliegue en Railway, pruebas de Postman y la descripción de la arquitectura del proyecto.

## 3. Detalle de cada Microservicio

### Users Service
- **Responsabilidad principal:** Manejo de Identidad, Autenticación y Perfiles.
- **Endpoins Expuestos:**

| Método | Ruta | Descripción | Requiere JWT |
| :--- | :--- | :--- | :--- |
| POST | `/api/v1/auth/register` | Registro de usuario | No |
| POST | `/api/v1/auth/verify` | Verifica código de email | No |
| POST | `/api/v1/auth/resend-code` | Reenvía código verificación | No |
| POST | `/api/v1/auth/login` | Iniciar sesión y obtener JWT | No |
| GET | `/api/v1/users/profile` | Obtiene el perfil del usuario actual | Sí |
| PUT | `/api/v1/users/profile` | Actualiza el perfil del usuario | Sí |
| GET | `/api/v1/users/exists/{id}` | Valida si un usuario existe | Sí |

- **Entidades/Tablas:** `users`, `roles`
- **Comunicaciones:** Se comunica de forma indirecta ya que es la principal fuente de perfiles. Otros servicios (como events) usan sus endpoints o decodifican el token que este servicio generó.

### Karma Service
- **Responsabilidad principal:** Sistema de reputación y penalizaciones por comportamiento en eventos.
- **Endpoins Expuestos:**

| Método | Ruta | Descripción | Requiere JWT |
| :--- | :--- | :--- | :--- |
| GET | `/api/v1/karma/{userId}` | Obtiene karma del usuario | Sí |
| POST | `/api/v1/karma/check-in` | Registra asistencia y suma karma | Sí |
| POST | `/api/v1/karma/absence/{userId}/{eventId}` | Registra inasistencia y resta karma | Sí |
| POST | `/api/v1/karma/organizer-validation` | Valida asistencia por el organizador | Sí |

- **Entidades/Tablas:** `karma_scores`, `karma_history`
- **Comunicaciones:** Recibe peticiones desde el `events-service` (vía RestTemplate) cuando finalizan eventos o se marcan inasistencias.

### Events Service
- **Responsabilidad principal:** Creación, búsqueda y suscripción a eventos deportivos.
- **Endpoins Expuestos:**

| Método | Ruta | Descripción | Requiere JWT |
| :--- | :--- | :--- | :--- |
| POST | `/api/v1/events` | Crear nuevo evento | Sí |
| GET | `/api/v1/events/nearby` | Buscar eventos por lat/lng | Sí |
| GET | `/api/v1/events/{id}` | Detalle completo de un evento | Sí |
| POST | `/api/v1/events/{id}/join` | Unirse a un evento | Sí |
| DELETE| `/api/v1/events/{id}/leave` | Cancelar participación | Sí |
| GET | `/api/v1/events/{id}/attendees`| Ver inscritos (solo org) | Sí |
| POST | `/api/v1/events/{id}/attendance`| Marcar asistencia del usuario | Sí |
| POST | `/api/v1/events/{id}/finish` | Finaliza evento y evalúa karma | Sí |
| DELETE| `/api/v1/events/{id}` | Eliminar evento | Sí |
| GET | `/api/v1/events/my-events` | Eventos donde el usuario asiste | Sí |
| GET | `/api/v1/events/organized` | Eventos que el usuario organiza | Sí |

- **Entidades/Tablas:** `events`, `event_registrations`
- **Comunicaciones:** Llama al `karma_service` para compensar karma si se elimina un evento o al finalizar. Llama al `notification-service` para alertar de cancelaciones a los inscritos.

### Notification Service
- **Responsabilidad principal:** Centralización de notificaciones push a través de Firebase.
- **Endpoins Expuestos:**

| Método | Ruta | Descripción | Requiere JWT |
| :--- | :--- | :--- | :--- |
| POST | `/api/v1/notifications/token` | Registra el token FCM del usuario | Sí |
| POST | `/api/v1/notifications/send` | Envía notificación push asíncrona | Sí |
| GET | `/api/v1/notifications/history` | Obtiene el historial paginado | Sí |

- **Entidades/Tablas:** `notifications`, `device_tokens`
- **Comunicaciones:** Se conecta con APIs externas (Firebase/FCM) y recibe llamadas desde `events-service` para notificar de manera síncrona/asíncrona.

## 4. Frontend Android

La aplicación está diseñada bajo el patrón MVVM y Clean Architecture en una estructura de múltiples capas, con Kotlin y Jetpack Compose:

- **`core/`** — Contiene configuraciones fundamentales, utilidades globales de inyección (Hilt), interceptores de red para JWT, navegación, tema Material 3 y estado de UI global.
- **`data/`** — Implementa el acceso a datos. Contiene los DTOs que mapean las respuestas REST, los clientes de API (Retrofit) y los `Repositories` concretos que intermedian entre el network y el dominio.
- **`domain/`** — Capa pura de negocio. Contiene modelos limpios y casos de uso (UseCases) que ejecutan la lógica que orquesta los repositorios.
- **`ui/screens/`** — Composables que conforman cada pantalla (Login, Dashboard, Events, Karma, Profile). Son controlados por sus respectivos ViewModels que reaccionan a los eventos del usuario.
- **`di/`** — Módulos de inyección de dependencias configurados con Dagger Hilt (NetworkModule, RepositoryModule, AppModule) para proveer instancias únicas y controladas.

## 5. Patrones de Diseño Utilizados

- **Model-View-ViewModel (MVVM):** Usado intensivamente en la App Android para mantener la capa visual (Jetpack Compose) completamente reactiva al estado proveniente del ViewModel.
- **Arquitectura en capas (Backend):** Separación tradicional de Spring Boot en `Controller` (Exposición REST), `Service` (Lógica de Negocio) y `Repository` (Persistencia Spring Data JPA).
- **API Gateway Pattern:** Patrón distribuido para unificar un único punto de acceso al frontend de forma que oculte las complejidades del particionado del dominio backend.
- **Autenticación Basada en JWT:** Stateless token mechanism. Users Service lo emite; API Gateway lo filtra o delega a los microservicios que lo decodifican localmente.
- **Comunicación Síncrona REST (RestTemplate/Feign):** Para la orquestación entre microservicios de manera directa cuando se requiere inmediatez en el proceso (ej. Events a Karma).

## 6. Base de Datos

El diseño es *Database per Service*.

- **`pichangapp_users`**:
  - `users`: ID, correo, password, nombre, apellido, código verificación, enabled.
  - `roles`: ID, nombre (USER, ADMIN).
  - *Relación:* Usuarios pueden tener uno o más roles.

- **`pichangapp_karma`**:
  - `karma_scores`: userId, puntaje, categoría (Bajo, Medio, Alto).
  - `karma_history`: id, userId, eventId, cambio_puntaje, tipo_evento, fecha.
  - *Relación:* Cada historial pertenece a un userId particular.

- **`pichangapp_events`**:
  - `events`: id, organizer_id, nombre, fecha, status, max_players, current_players, lat, lng.
  - `event_registrations`: id, event_id, user_id, status, fecha_registro.
  - *Relación:* Uno-a-Muchos entre eventos e inscripciones.

- **`pichangapp_notifications`**:
  - `notifications`: id, user_id, title, body, status, created_at.
  - `device_tokens`: id, user_id, token_fcm.
  - *Relación:* Ninguna directa; ambas asocian al user_id.

## 7. Seguridad

El flujo de autenticación JWT ocurre bajo la siguiente premisa:
1. El usuario envía credenciales a `/api/v1/auth/login`.
2. El `users-service` comprueba las credenciales usando Spring Security y emite un JWT firmado que contiene el rol y el ID (`sub` claim).
3. El frontend Android intercepta y almacena este token.
4. Las llamadas subsiguientes añaden el header `Authorization: Bearer <token>`.
5. El `api-gateway` (o los microservicios, según la configuración de los filtros de Spring Security) valida la firma secreta del token.
6. Si es válido, se extrae el ID del usuario del JWT para realizar las operaciones sobre sus propios datos sin requerir confiar en un parámetro por URL. Si está expirado o no existe, retorna código 401 Unauthorized.
