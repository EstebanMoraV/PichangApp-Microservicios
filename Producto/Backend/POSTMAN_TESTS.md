# Guía de Pruebas Postman - PichangApp

Esta guía contiene todos los endpoints de la arquitectura de microservicios de PichangApp desplegada en Railway.

## Configuración Inicial en Postman

Para ejecutar estas pruebas de forma eficiente, configura un **Environment** en Postman con las siguientes variables:

### 1. Variables de Entorno
- `baseUrl`: `https://pichangapp-microservicios-production.up.railway.app`
- `token`: (dejar vacío, se llenará automáticamente)

### 2. Guardado Automático del Token
En la pestaña **Tests** de la petición de **Login**, pega el siguiente código para que el token se actualice automáticamente en tu ambiente:

```javascript
if (pm.response.code === 200) {
    const jsonData = pm.response.json();
    pm.environment.set("token", jsonData.token);
    console.log("Token guardado correctamente");
}
```

---

## 1. Auth — Registro e Inicio de Sesión

### 1.1 Registro de Usuario
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/auth/register`
**Requiere JWT:** No

**Headers:**
- Content-Type: application/json

**Body (raw JSON):**
```json
{
  "correo": "test@pichangapp.cl",
  "password": "password123",
  "nombre": "Esteban",
  "apellido": "Trabajos"
}
```

**Respuesta esperada:** 201 Created
**Qué valida:** Creación de un nuevo usuario en `users-service`.

### 1.2 Inicio de Sesión (Login)
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/auth/login`
**Requiere JWT:** No

**Headers:**
- Content-Type: application/json

**Body (raw JSON):**
```json
{
  "correo": "test@pichangapp.cl",
  "password": "password123"
}
```

**Respuesta esperada:** 200 OK
**Qué valida:** Autenticación correcta y retorno del JWT. (Usa el script de Tests para guardar el token).

### 1.3 Intento de Login Fallido (Prueba Negativa)
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/auth/login`
**Requiere JWT:** No

**Body (raw JSON):**
```json
{
  "correo": "test@pichangapp.cl",
  "password": "wrong_password"
}
```

**Respuesta esperada:** 401 Unauthorized o 404 si el usuario no existe.
**Qué valida:** El sistema no permite el acceso con credenciales incorrectas.

---

## 2. Usuarios — Consulta y Gestión

### 2.1 Obtener Perfil
**Método:** GET
**URL:** `{{baseUrl}}/api/v1/users/1`
**Requiere JWT:** Sí

**Headers:**
- Authorization: Bearer {{token}}

**Respuesta esperada:** 200 OK
**Qué valida:** Retorno de los datos públicos del usuario.

### 2.2 Actualizar Perfil
**Método:** PUT
**URL:** `{{baseUrl}}/api/v1/users/1`
**Requiere JWT:** Sí

**Body (raw JSON):**
```json
{
  "nombre": "Esteban Editado",
  "apellido": "Pérez"
}
```

**Respuesta esperada:** 200 OK
**Qué valida:** Persistencia de cambios en el nombre/apellido.

### 2.3 Cambiar Contraseña
**Método:** PUT
**URL:** `{{baseUrl}}/api/v1/users/1/password`
**Requiere JWT:** Sí

**Body (raw JSON):**
```json
{
  "currentPassword": "password123",
  "newPassword": "newpassword456"
}
```

**Respuesta esperada:** 204 No Content
**Qué valida:** Cambio seguro de credenciales.

---

## 3. Karma — Check-in, Penalizaciones y Consultas

### 3.1 Consultar Karma de un Usuario
**Método:** GET
**URL:** `{{baseUrl}}/api/v1/karma/1`
**Requiere JWT:** Sí

**Respuesta esperada:** 200 OK
**Qué valida:** Obtención del puntaje actual y categoría (Bronce, Plata, Oro).

### 3.2 Registrar Check-in (Puntaje Positivo)
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/karma/check-in`
**Requiere JWT:** Sí

**Body (raw JSON):**
```json
{
  "userId": "1",
  "eventId": "100",
  "location": "-33.4489,-70.6693"
}
```

**Respuesta esperada:** 200 OK
**Qué valida:** Incremento de karma por asistir a un evento.

### 3.3 Registrar Inasistencia (Penalización)
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/karma/absence/1/event/100`
**Requiere JWT:** Sí

**Respuesta esperada:** 200 OK
**Qué valida:** Descuento de puntos por no llegar al evento.

### 3.4 Validación por Organizador
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/karma/validation`
**Requiere JWT:** Sí

**Body (raw JSON):**
```json
{
  "userId": "1",
  "eventId": "100",
  "organizerId": "2",
  "isPositiveValidation": true
}
```

**Respuesta esperada:** 200 OK
**Qué valida:** Ajuste manual de karma por parte de un tercero confiable.

---

## 4. Notificaciones — Tokens e Historial

### 4.1 Registrar Token de Dispositivo (FCM)
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/notifications/device-token`
**Requiere JWT:** Sí

**Body (raw JSON):**
```json
{
  "userId": "1",
  "token": "fcm_token_ejemplo_123456"
}
```

**Respuesta esperada:** 200 OK
**Qué valida:** Asociación del dispositivo con el usuario para envíos push.

### 4.2 Enviar Notificación Individual
**Método:** POST
**URL:** `{{baseUrl}}/api/v1/notifications/send`
**Requiere JWT:** Sí

**Body (raw JSON):**
```json
{
  "userId": "1",
  "title": "¡Puntaje Karma Subió!",
  "body": "Has recibido +10 puntos por tu puntualidad.",
  "type": "KARMA_INCREASE"
}
```

**Respuesta esperada:** 200 OK
**Qué valida:** Ejecución del flujo de envío (Firebase + Registro en DB).

### 4.3 Consultar Historial de Notificaciones
**Método:** GET
**URL:** `{{baseUrl}}/api/v1/notifications/1?page=0&size=10`
**Requiere JWT:** Sí

**Respuesta esperada:** 200 OK (Paginado)
**Qué valida:** Recuperación de mensajes previos enviados al usuario.

---

## 5. Seguridad — Pruebas de Acceso Indebido

### 5.1 Acceso a Usuarios sin JWT (Prueba Negativa)
**Método:** GET
**URL:** `{{baseUrl}}/api/v1/users/1`
**Requiere JWT:** No (No enviar header)

**Respuesta esperada:** 401 Unauthorized
**Qué valida:** El API Gateway bloquea peticiones sin token.

### 5.2 Acceso con Token Inválido (Prueba Negativa)
**Método:** GET
**URL:** `{{baseUrl}}/api/v1/karma/1`
**Requiere JWT:** Sí (Enviar un token mal formado)

**Headers:**
- Authorization: Bearer token_falso_123

**Respuesta esperada:** 401 Unauthorized
**Qué valida:** El filtro JWT valida la firma y vigencia del token.

---

## 6. Health Checks — Estado de los Servicios

### 6.1 Estado Global del Sistema
**Método:** GET
**URL:** `{{baseUrl}}/health`
**Requiere JWT:** No

**Respuesta esperada:** 200 OK
**Qué valida:** Que el Gateway y los microservicios (Users, Karma, Notifications) estén activos y conectados.

---

## Flujo Completo de Prueba (Orden Recomendado)

Para probar todo el ecosistema sin errores, sigue estos pasos:

1. **Health Check:** Llama a `/health` para confirmar que todo está `UP`.
2. **Registro:** Crea un usuario nuevo en `/api/v1/auth/register`.
3. **Login:** Obtén tu token en `/api/v1/auth/login`. *Asegúrate de que la variable `{{token}}` se haya actualizado.*
4. **Perfil:** Consulta `/api/v1/users/1` para verificar que el usuario existe.
5. **Karma Inicial:** Consulta `/api/v1/karma/1`. Debería tener puntaje inicial.
6. **Check-in:** Simula asistencia con `/api/v1/karma/check-in`.
7. **Karma Actualizado:** Vuelve a consultar karma para ver el incremento.
8. **Token Push:** Registra un token ficticio en `/api/v1/notifications/device-token`.
9. **Notificación:** Envía una prueba a `/api/v1/notifications/send`.
10. **Historial:** Revisa `/api/v1/notifications/1` para ver la notificación registrada.
