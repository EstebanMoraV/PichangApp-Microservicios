# Variables de entorno Railway — PichangApp

## Variables compartidas (configurar en TODOS los servicios)
| Variable | Valor |
|---|---|
| SPRING_PROFILES_ACTIVE | prod |
| JWT_SECRET | (mismo valor seguro en todos) |

## users-service
| Variable | Valor |
|---|---|
| DATABASE_URL | jdbc:mysql://mysql.railway.internal:3306/pichangapp_users?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC |
| DB_USERNAME | root |
| DB_PASSWORD | (password de Railway) |

## karma_service
| Variable | Valor |
|---|---|
| DATABASE_URL | jdbc:mysql://mysql.railway.internal:3306/pichangapp_karma?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC |
| DB_USERNAME | root |
| DB_PASSWORD | (password de Railway) |

## notification-service
| Variable | Valor |
|---|---|
| DATABASE_URL | jdbc:mysql://mysql.railway.internal:3306/pichangapp_notifications?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC |
| DB_USERNAME | root |
| DB_PASSWORD | (password de Railway) |
| FIREBASE_CREDENTIALS_JSON | (JSON completo de Firebase) |

## api-gateway
| Variable | Valor |
|---|---|
| USERS_SERVICE_URL | (URL de Railway del users-service) |
| KARMA_SERVICE_URL | (URL de Railway del karma-service) |
| NOTIFICATION_SERVICE_URL | (URL de Railway del notification-service) |
