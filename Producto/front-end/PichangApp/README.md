# PichangApp - Frontend (Android)

PichangApp es una aplicación Android desarrollada para organizar eventos deportivos recreativos en Chile. Este proyecto está construido utilizando **Kotlin**, **Jetpack Compose**, **Clean Architecture** (MVVM) e interactúa con un backend de microservicios.

## Tecnologías y Herramientas

*   **Lenguaje:** Kotlin 2.1.0
*   **UI:** Jetpack Compose (Material 3 con Material You)
*   **Inyección de Dependencias:** Hilt
*   **Redes:** Retrofit 2 + OkHttp
*   **Navegación:** Navigation Compose
*   **Persistencia Local:** DataStore Preferences (Para JWT y estado de sesión) y Room (Para futura caché local)
*   **Carga de Imágenes:** Coil
*   **Arquitectura:** Clean Architecture (UI -> ViewModel -> UseCase -> Repository -> DataSource)

## Arquitectura del Proyecto

El código está estructurado bajo `cl.duoc.pichangapp` en los siguientes paquetes principales:

*   `core/`: Configuraciones transversales como cliente de red, interceptores (JWT) y DataStore.
*   `data/`: Implementaciones de Repositorios, Modelos de Datos (DTOs) y clientes de Retrofit (APIs).
*   `domain/`: Lógica de negocio encapsulada en Casos de Uso (UseCases).
*   `di/`: Módulos de Hilt para proveer dependencias de red, repositorios, etc.
*   `ui/`: Todas las pantallas de Jetpack Compose, ViewModels, Navegación y Tema visual (Colores, Tipografía).

## Configuración y Ejecución

Sigue estos pasos para compilar y ejecutar el proyecto en tu entorno local:

### Prerrequisitos

*   **Android Studio Ladybug** (o versión más reciente que soporte AGP 8.3.x).
*   **Emulador de Android** o un dispositivo físico con Android 8.0 (API 26) o superior.

### Instrucciones

1.  Abre **Android Studio**.
2.  Selecciona **File > Open...** (o **Open** en la pantalla de bienvenida).
3.  Navega hasta la carpeta `Producto/front-end/PichangApp` y selecciona esa carpeta para abrir el proyecto.
4.  Espera a que **Gradle Sync** finalice. Android Studio descargará todas las dependencias definidas en `libs.versions.toml`.
5.  Una vez finalizada la sincronización, selecciona tu emulador o dispositivo físico en el menú superior.
6.  Haz clic en el botón de **Run** (Play verde) o presiona `Shift + F10` para compilar y lanzar la aplicación.

### Integración con el Backend

La aplicación está configurada para conectarse al entorno de producción desplegado en Railway:
`https://pichangapp-microservicios-production.up.railway.app`

Los endpoints interactúan con:
*   **users-service:** Perfil de usuario.
*   **karma_service:** Puntos, historial y penalizaciones.
*   **notification-service:** Historial de notificaciones y registro de FCM (Mockeado en UI temporalmente).
*   **api-gateway:** Autenticación y enrutamiento (JWT).

> **Nota:** La persistencia del token se realiza a través de DataStore en la clase `TokenDataStore`. Este token se adjunta de forma automática en cada petición subsiguiente mediante el `AuthInterceptor`.
