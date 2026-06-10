# Despliegue del Panel de Administración en Vercel

Guía paso a paso para desplegar `admin-panel` (React + Vite) en [Vercel](https://vercel.com).

## Requisitos previos

- Cuenta en Vercel (puedes iniciar sesión con GitHub).
- El repositorio del proyecto subido a GitHub.
- El backend (API Gateway) desplegado y accesible públicamente en Railway.
- El usuario administrador creado automáticamente por el `users-service`:
  - **Correo:** `admin@pichangapp.cl`
  - **Contraseña:** `Admin@2024!`

---

## Opción A — Despliegue desde el panel web de Vercel (recomendado)

1. **Importar el proyecto**
   - Entra a https://vercel.com/new
   - Selecciona el repositorio de GitHub que contiene PichangApp.

2. **Configurar el directorio raíz (Root Directory)**
   - Vercel preguntará por el *Root Directory*. Selecciona:
     ```
     Producto/admin-panel
     ```
   - Esto es importante porque el panel no está en la raíz del repositorio.

3. **Framework Preset**
   - Vercel detectará automáticamente **Vite**.
   - Si no lo hace, selecciónalo manualmente.
   - Build Command: `npm run build`
   - Output Directory: `dist`
   - Install Command: `npm install`

4. **Variables de entorno**
   - En la sección *Environment Variables* agrega:

     | Name           | Value                                                                 |
     |----------------|-----------------------------------------------------------------------|
     | `VITE_API_URL` | `https://pichangapp-microservicios-production.up.railway.app`         |

   - Ajusta el valor si tu gateway de Railway tiene otra URL.

5. **Desplegar**
   - Pulsa **Deploy** y espera a que termine el build.
   - Vercel te entregará una URL pública, por ejemplo:
     `https://pichangapp-admin.vercel.app`

6. **Verificar**
   - Abre la URL, inicia sesión con la cuenta de administrador.
   - Si el login responde "Acceso denegado", la cuenta no es ADMIN; usa `admin@pichangapp.cl`.

---

## Opción B — Despliegue con Vercel CLI

```bash
# 1. Instalar la CLI (una sola vez)
npm install -g vercel

# 2. Posicionarse en el directorio del panel
cd Producto/admin-panel

# 3. Instalar dependencias y verificar el build localmente
npm install
npm run build

# 4. Iniciar sesión y desplegar
vercel login
vercel            # despliegue de previsualización
vercel --prod     # despliegue a producción
```

Durante `vercel`, cuando pregunte por la variable de entorno, define:

```
VITE_API_URL = https://pichangapp-microservicios-production.up.railway.app
```

---

## SPA Routing

El archivo [`vercel.json`](./vercel.json) ya incluye la reescritura necesaria para que
las rutas del cliente (`/users`, `/events`, `/users/:id`, etc.) funcionen al recargar la
página:

```json
{
  "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
}
```

---

## CORS

El API Gateway ya permite el origen de Vercel (`https://*.vercel.app`) además de `*`
para desarrollo. Si usas un dominio personalizado, agrégalo a la lista de orígenes
permitidos en `api-gateway/.../config/CorsConfig.java` y vuelve a desplegar el backend.

---

## Solución de problemas

| Síntoma                                   | Causa probable / Solución                                            |
|-------------------------------------------|----------------------------------------------------------------------|
| 404 al recargar `/users`                  | Falta `vercel.json` o no se aplicó el rewrite. Revisa el archivo.    |
| Error de CORS en consola                  | Agrega el dominio de Vercel a `CorsConfig` del gateway.             |
| "Network Error" al iniciar sesión         | `VITE_API_URL` incorrecta o el backend de Railway está caído.       |
| "Acceso denegado" tras login              | La cuenta no tiene rol ADMIN. Usa `admin@pichangapp.cl`.            |
| El karma aparece como "—"                 | Ese usuario aún no tiene registro de karma (es normal).             |
