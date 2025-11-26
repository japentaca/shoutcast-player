# Shoutcast Player - Aplicación Android

Una aplicación moderna para Android que permite buscar y escuchar miles de emisoras de radio por internet. Utiliza la **Radio Browser API** como directorio de emisoras (una alternativa abierta y gratuita a Shoutcast).

## 📱 Características

*   **Buscador Potente:** Busca emisoras por nombre, género o etiquetas (ej. "Rock", "News", "Jazz").
*   **Reproductor Robusto:** Streaming de audio estable utilizando **ExoPlayer (Media3)**.
*   **Reproducción en Segundo Plano:** La música sigue sonando aunque salgas de la app o apagues la pantalla.
*   **Favoritos:** Guarda tus emisoras preferidas para acceder a ellas rápidamente (base de datos local).
*   **Estado de Conexión:** Indicador visual del estado del reproductor (Cargando, Reproduciendo, Error).
*   **Interfaz Moderna:** Diseñada con **Jetpack Compose** y Material Design 3.

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **UI:** Jetpack Compose (Material3)
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **Inyección de Dependencias:** Hilt
*   **Red:** Retrofit + OkHttp
*   **Base de Datos:** Room
*   **Audio:** AndroidX Media3 (ExoPlayer)
*   **Carga de Imágenes:** Coil

## 🚀 Cómo Ejecutar el Proyecto

1.  **Clonar/Abrir:** Abre la carpeta del proyecto en **Android Studio**.
2.  **Sincronizar:** Deja que Gradle descargue las dependencias necesarias.
3.  **Ejecutar:** Conecta tu dispositivo Android o inicia un emulador y presiona "Run" (el triángulo verde).

> **Nota:** El proyecto incluye un `gradle-wrapper.jar` instalado manualmente para facilitar la compilación desde la línea de comandos si es necesario (`./gradlew assembleDebug`).

## 📄 Licencia y Créditos

Este proyecto utiliza la API pública de [Radio Browser](https://www.radio-browser.info/).
