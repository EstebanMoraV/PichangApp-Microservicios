# 🏟️ PichangApp - Gestión Deportiva Inteligente

![Estado del Proyecto](https://img.shields.io/badge/Estado-En%20Desarrollo-green)
![Java](https://img.shields.io/badge/Backend-Java%2017-orange)
![Kotlin](https://img.shields.io/badge/Frontend-Kotlin-blue)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot%203-brightgreen)

**PichangApp** es una solución integral diseñada para fomentar la actividad física en Chile, atacando directamente la problemática del sedentarismo y la falta de compromiso en encuentros deportivos recreativos. A través de una arquitectura de microservicios y geolocalización, conectamos a deportistas de fútbol, tenis y pádel.

---

## 🚀 Propuesta de Valor
A diferencia de otras plataformas, PichangApp implementa un **Sistema de Karma**. Este algoritmo de reputación garantiza que los usuarios asistan a sus compromisos, reduciendo la tasa de deserción y mejorando la experiencia comunitaria.

## 🛠️ Stack Tecnológico
- **Frontend:** Mobile Nativo con Kotlin y Android Studio.
- **Backend:** Microservicios con Java 17 y Spring Boot.
- **Base de Datos:** MySQL (Relacional).
- **Infraestructura:** Despliegue en la nube mediante Railway (PaaS).
- **APIs de Terceros:** Google Maps Platform para Geofencing.

---

## 🏗️ Arquitectura del Sistema
El sistema se basa en un patrón de **Microservicios** para garantizar escalabilidad y alta disponibilidad:
* **Identity-Service:** Gestión de autenticación (JWT) y perfiles.
* **Event-Service:** Lógica de creación, búsqueda y unión a eventos deportivos.
* **Reputation-Service:** Motor de cálculo para el sistema de Karma.



---

## 📋 Estructura del Repositorio
Siguiendo los lineamientos de la asignatura:
* `/documentation`: Informes, Diagramas UML, Carta Gantt y Wireframes (Figma).
* `/product`: Código fuente de los microservicios y la aplicación móvil.
* `/database`: Scripts de creación de tablas y datos de prueba.

## 👥 Integrantes - Equipo de Desarrollo
- **Esteban Mora** - *Arquitecto - Programador*
- **David Salazar** - *Product Owner - Desarrollador*
- **Martín Villegas** - *Scru Master - Desarrollador*

---

## 📅 Planificación (Hitos Principales)
1. **Semana 4:** Finalización de Diseño y Estrategia (Experiencia 1).
2. **Semana 11:** MVP funcional desplegado en Railway (Experiencia 2).
3. **Semana 15:** Control de Calidad y Pruebas Unitarias/Integración (Experiencia 3).

---
*Este proyecto es parte de la asignatura Taller Aplicado de Programación (TPY1101) - Duoc UC.*
