# Notas de cambios – MyCiclo (Equipo 9)

Registro de las modificaciones al proyecto, para que todo el equipo sepa qué se cambió y cómo probarlo.

---

## 2026-10-06 · Etapa 1: dependencias de Gradle

**Objetivo:** dejar listas las librerías que necesitan las siguientes etapas (navegación y base de datos local).

**Archivos modificados**

| Archivo | Cambio |
|---|---|
| `gradle/libs.versions.toml` | Se agregaron las versiones `ksp = 2.2.10-2.0.2`, `room = 2.8.0` y `navigationCompose = 2.10.2`, las librerías `room-runtime`, `room-ktx`, `room-compiler` y `navigation-compose`, y el plugin `ksp`. |
| `build.gradle.kts` (raíz) | Se declaró el plugin `ksp` con `apply false`. |
| `app/build.gradle.kts` | Se aplicó el plugin `ksp` y se agregaron las dependencias de Room y Navigation Compose. |
| `gradle.properties` | Se agregó `android.disallowKotlinSourceSets=false`. Sin esta línea, KSP falla con AGP 9 ("Using kotlin.sourceSets DSL ... is not allowed with built-in Kotlin"). |
| `.gitignore` | Se ignoran `.idea/.name`, `.idea/codeStyles/`, `.idea/vcs.xml` y `app/.idea/`, que son ajustes locales de Android Studio. Los demás archivos de `.idea` que ya estaban en el repositorio no se tocaron. |

Las versiones son las mismas del ejercicio **MisNotasAppSQLite** de la profesora, que usa el mismo Kotlin (2.2.10).

**Cómo probar**
1. En Android Studio, hacer *File → Sync Project with Gradle Files*.
2. Ejecutar la app. Debe verse igual que antes, porque esta etapa no cambia pantallas.

**Verificado:** `./gradlew :app:assembleDebug` compila sin errores.
