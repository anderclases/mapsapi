# 📍 Maps API Integration

Proyecto educativo enfocado en la integración de servicios de mapas para aplicaciones móviles.

---

## 📝 Introducción
Este repositorio contiene un programa demo diseñado para explorar y experimentar con las funcionalidades de la **API de Google Maps**. 

> [!WARNING]
> **Seguridad de la API Key:** La clave se obtiene a través de [Google Cloud Platform](https://console.cloud.google.com/). Es fundamental **no publicar la API Key en GitHub** para evitar cargos inesperados o mal uso por terceros. Por lo que mucho cuidado con donde la ponemos y el gitignore.

---

## 📱 Pantallas Desarrolladas

### 🗺️ Mapa 1: Estética y Marcadores
Enfoque en la personalización visual y control de la interfaz:
* **Iconos:** Uso de un **marcador con icono personalizado**.
* **Animaciones:** Incluye dos marcadores; uno estático y un `FloatingMarker` animado.
* **Interactividad:**
    * Un marcador funciona como botón mediante un evento `onClick`.
    * El mapa inicia con el **movimiento bloqueado** (solo visual) y dispone de un botón para habilitar la interacción.

### 🛰️ Mapa 2: Ubicación en Tiempo Real
Enfoque en la gestión de hardware y permisos del dispositivo:
* **Flujo de Permisos:** Comprobación y solicitud automática de permisos de ubicación.
* **Lógica:** Almacenamiento de latitud y longitud en variables dinámicas.
* **Cámara:** Botón de acceso rápido para centrar el mapa en la posición del usuario.

### 📍 Mapa 3: Marcadores Dinámicos
Enfoque en la renderización de datos múltiples:
* **Listas:** Generación de un mapa basado en una lista de coordenadas.
* **Personalización:**
    * Función para marcadores estándar.
    * Función para **marcadores numerados personalizados**.

---

## 🌍 Alternativas Open Source

### 🗺️ OsmMap
* Implementación de la lógica del **Mapa 3** utilizando la herramienta de código abierto [Open Street Map](https://www.openstreetmap.org/).
* Ideal para proyectos que buscan independencia de servicios de pago.

---
*Desarrollado con fines educativos.*