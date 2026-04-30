# Guía de Pruebas de Integración - PichangApp (Postman)

Este documento detalla los pasos necesarios para probar la integración y comunicación entre los microservicios `users-service` y `karma_service`.

---

## 1. Preparación de la Base de Datos (Laragon)
Antes de ejecutar los microservicios o probar en Postman, es obligatorio inicializar las bases de datos locales:

1. Abre **Laragon** y enciende **MySQL**.
2. Abre tu gestor de base de datos favorito (ej. HeidiSQL).
3. **Crea las bases de datos** (si no existen):
   - `pichangapp`
   - `pichangapp_karma`
4. **Ejecuta los scripts SQL** proporcionados en el proyecto:
   - En la base de datos `pichangapp`, ejecuta el archivo: `Producto/Backend/users-service/sql/init_users.sql`
   - En la base de datos `pichangapp_karma`, ejecuta el archivo: `Producto/Backend/karma_service/sql/init_karma.sql`

> **Nota importante:** El script `init_users.sql` crea automáticamente un usuario de prueba activo con el correo `test@pichangapp.cl` y contraseña `password123`. Usaremos este usuario para las pruebas.

---

## 2. Iniciar los Microservicios
Asegúrate de tener corriendo ambos proyectos de Spring Boot al mismo tiempo:
- **`users-service`** corriendo en el puerto **8080**
- **`karma_service`** corriendo en el puerto **8081**

---

## 3. Pruebas en Postman

Para todas las peticiones (excepto el Login), deberás configurar el token JWT en la pestaña **Authorization** de Postman:
- Type: **Bearer Token**
- Token: `<el_token_obtenido_en_el_login>`

### 3.1. Login (Obtener Token)
*Petición a users-service para obtener acceso.*

- **Método:** `POST`
- **URL:** `http://localhost:8080/api/v1/auth/login`
- **Body (raw / JSON):**
  ```json
  {
    "correo": "test@pichangapp.cl",
    "password": "password123"
  }
  ```
- **Acción:** Copia el valor de `"token"` y el `"id"` del usuario que viene en la respuesta. Necesitarás ambos para los siguientes pasos.

### 3.2. Verificar Existencia de Usuario
*Petición a users-service para asegurar que el endpoint de consulta cruzada funciona.*

- **Método:** `GET`
- **URL:** `http://localhost:8080/api/v1/users/{{tu_id}}/exists`
- **Headers:** Authorization -> Bearer Token
- **Resultado Esperado:** Debe devolver `true`.

### 3.3. Obtener Karma Inicial
*Petición a karma_service. El servicio verificará internamente tu token con users-service y creará tu perfil de karma.*

- **Método:** `GET`
- **URL:** `http://localhost:8081/api/v1/karma/{{tu_id}}`
- **Headers:** Authorization -> Bearer Token
- **Resultado Esperado:** 
  ```json
  {
    "userId": "{{tu_id}}",
    "karmaScore": 100,
    "category": "Excelente"
  }
  ```

### 3.4. Registrar un Check-in (+10 Puntos)
*Simula la asistencia a un evento.*

- **Método:** `POST`
- **URL:** `http://localhost:8081/api/v1/karma/check-in`
- **Headers:** Authorization -> Bearer Token
- **Body (raw / JSON):**
  ```json
  {
    "userId": "{{tu_id}}",
    "eventId": "PARTIDO_FINAL",
    "location": "-33.45,-70.66"
  }
  ```
- **Resultado Esperado:** El `karmaScore` debe subir a `110`.

### 3.5. Registrar Inasistencia (-15 Puntos)
*Simula faltar a un evento sin avisar.*

- **Método:** `POST`
- **URL:** `http://localhost:8081/api/v1/karma/absence/{{tu_id}}/event/OTRO_PARTIDO`
- **Headers:** Authorization -> Bearer Token
- **Body:** Ninguno (los datos van en la URL).
- **Resultado Esperado:** El `karmaScore` debe bajar a `95`.

### 3.6. Validación del Organizador (+/- 5 Puntos)
*Simula la evaluación de un organizador hacia un jugador.*

- **Método:** `POST`
- **URL:** `http://localhost:8081/api/v1/karma/validation`
- **Headers:** Authorization -> Bearer Token
- **Body (raw / JSON):**
  ```json
  {
    "userId": "{{tu_id}}",
    "eventId": "PARTIDO_FINAL",
    "organizerId": "ORG_ADMIN",
    "isPositiveValidation": true
  }
  ```
- **Resultado Esperado:** Como pusimos `true`, el `karmaScore` debe sumar 5 puntos y volver a `100`.
