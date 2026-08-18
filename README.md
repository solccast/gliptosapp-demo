# 🦴 GliptosApp

Aplicación Android educativa e inclusiva sobre paleontología, enfocada en la megafauna del Pleistoceno de Sudamérica. El usuario explora un mapa de excavación, descubre fósiles, aprende sobre cada especie y pone a prueba lo aprendido con minijuegos, todo con foco fuerte en **accesibilidad**.

## ✨ Features

- 🗺️ **Mapa de excavación interactivo** con nodos accesibles (soporte para TalkBack)
- ⛏️ **Excavación de fósiles**
- 📚 **Colección**: fósiles descubiertos con información extra (peso, tamaño, datos curiosos)
- 🎮 **Minijuego comparativo**: preguntas que desbloquean información adicional al responder correctamente
- 🦖 **Visor 3D**: modelos `.glb` visualizables sin necesidad de cámara/RA
- 📖 **Recursos**: libros, documentales, links y redes sociales relacionados
- 🗣️ **Kira**: narración y mensajes de ayuda contextual
- ♿ **Accesibilidad**: alto contraste, cambio de tipografía (incluye OpenDyslexic), vibración configurable, narración por voz y soporte TalkBack (por el momento sólo excavación)
- 🎵 Música y efectos de sonido configurables

## 🛠️ Stack tecnológico

| Categoría | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| Arquitectura | MVVM |
| Inyección de dependencias | Dagger Hilt |
| Persistencia | Room |
| Navegación | Android Navigation Component (Safe Args) |
| Mapas | OSMDroid |
| Modelos 3D | SceneView (ARSceneView) |
| Animaciones | Lottie |
| UI | View Binding, Material Components |

**Requisitos:** `minSdk 24` · `targetSdk / compileSdk 36` · Java 17

## 📂 Estructura del proyecto

```
app/src/main/java/com/example/gliptosapp/
├── data/            # Entities, DAOs y AppDatabase (Room)
├── repository/      # Repositorios (fósiles, excavación, minijuego, settings)
└── ui/
    ├── home/            # Pantalla de inicio
    ├── mapa/            # Mapa de excavación
    ├── excavation/      # Pantalla de excavación
    ├── colection/        # Colección de fósiles descubiertos
    ├── detailFosile/    # Información extra del fósil
    ├── comparativeGameInfo/ # Minijuego comparativo
    ├── visor3d/         # Visor de modelos 3D
    ├── recursos/        # Libros, documentales y links
    ├── settings/        # Ajustes (tema, fuente, vibración, sonido)
    └── helper/          # Narración (Kira), diálogos y utilidades
```

## 🚀 Instalación

1. Clonar el repositorio
   ```bash
   git clone https://github.com/solccast/gliptosapp-demo.git
   ```
2. Abrir el proyecto en **Android Studio** (versión reciente recomendada)
3. Dejar que Gradle sincronice las dependencias
4. Ejecutar sobre un emulador o dispositivo físico con Android 7.0 (API 24) o superior

> No requiere claves de API ni configuración adicional.

## 👥 Equipo

Proyecto grupal desarrollado por [@solccast](https://github.com/solccast), [@JJuanVolpe](https://github.com/JJuanVolpe) y [@elvalsdelaluz](https://github.com/elvalsdelaluz).
