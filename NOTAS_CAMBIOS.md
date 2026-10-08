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

---

## 2026-10-06 · Navegación, base de datos Room, MVVM y reglas en utils

**Objetivo:** dejar la estructura base para que cada integrante pueda implementar su pantalla.

### Estructura resultante

```
com.example.myciclo/
├── MainActivity.kt                 (modificado)
├── navigation/Navigation.kt        (nuevo)
├── data/
│   ├── MyCicloRepository.kt        (nuevo)
│   └── room/                       (nuevo)
│       ├── AppDatabase.kt, Converters.kt
│       ├── CicloEntity.kt, RegistroDiarioEntity.kt, RegistroEvaEntity.kt
│       ├── CicloDao.kt, RegistroDiarioDao.kt, RegistroEvaDao.kt
│       └── Flujo.kt, ResultadoEva.kt
├── viewmodel/InicioViewModel.kt    (nuevo)
├── ui/screens/
│   ├── InicioScreen.kt             (modificado)
│   ├── RegistroScreen.kt           (sin cambios)
│   ├── PlaceholderScreen.kt        (nuevo)
│   └── RegistroEvaScreen.kt, CalendarioScreen.kt, HistorialScreen.kt, AprenderScreen.kt (nuevos, provisorios)
└── utils/ReglasCiclo.kt, Validaciones.kt (nuevos)
```

### Archivos modificados

| Archivo | Cambio |
|---|---|
| `gradle/libs.versions.toml`, `app/build.gradle.kts` | Se agregó `lifecycle-viewmodel-compose` (para usar `viewModel()` en Compose). |
| `MainActivity.kt` | Se reemplazó el `String` `pantalla` y el `when` por `rememberNavController()` + `Navigation(navController)`. La lógica de pantallas pasó a `Navigation.kt`. |
| `InicioScreen.kt` | Se agregaron los parámetros `diaDelCiclo`, `onRegistroEva`, `onCalendario`, `onHistorial` y `onAprender`. Los botones que estaban vacíos (`onClick = { }`) ahora navegan. Se agregó un botón "Registro EVA (opcional)" y un texto con el día del ciclo. El resto del diseño no cambió. |

### Base de datos (`myciclo.db`, versión 1)

| Tabla | Campos | Notas |
|---|---|---|
| `ciclos` | `id`, `fechaInicio` (única) | El fin del ciclo y el día del ciclo **no se guardan**; se calculan en `utils`. |
| `registros_diarios` | `id`, `fecha` (única), `flujo?`, `emociones`, `sintomas`, `energia?`, `biomarcador?`, `notas?` | Uno por día; guardar de nuevo la misma fecha lo reemplaza (corregir). `emociones` y `sintomas` son listas. |
| `registros_eva` | `id`, `fechaHora`, `resultado` (`CON_HELECHOS` / `SIN_HELECHOS`), `fotoUri?` | `fechaHora` no es única: se permiten varias observaciones por día. |

Relación entre tablas: **por fecha**, sin claves foráneas. Un registro pertenece al ciclo cuyo `fechaInicio` es la más reciente que no sea posterior a su fecha (`buscarInicioDelCiclo` en utils).

### Rutas (`Rutas` en `Navigation.kt`)

`inicio` (inicio), `registro_diario`, `registro_eva`, `calendario`, `historial`, `aprender`.
`registro_diario` usa por ahora el `RegistroScreen` existente sin cambios.

### Reglas en utils

- `calcularDiaDelCiclo(inicio, fecha)`: día 1 = inicio del período; null si la fecha es anterior.
- `buscarInicioDelCiclo(inicios, fecha)`: a qué ciclo pertenece una fecha.
- `calcularFinDelCiclo(siguienteInicio)` y `calcularDuracionCiclo(inicio, siguienteInicio)` (RN-01).
- `determinarVentanaEva(fechasConHelechos, inicioCiclo, siguienteInicio?)`: desde el primer `CON_HELECHOS` del ciclo, 8 días (`DIAS_VENTANA_EVA`), sin pasar el fin del ciclo.
- `validarFechaRegistro`, `esFechaFutura`, `validarInicioPeriodo` (no futura, no repetida).

### Pendiente (no se hizo en este cambio)

- `RegistroScreen` todavía tiene Alto/Medio/Nulo y el día del ciclo manual: se corrige al implementar el Registro diario real.
- Las pantallas EVA, Calendario, Historial y Aprender son provisorias.

**Cómo probar**
1. *Sync Project with Gradle Files* y ejecutar la app.
2. En Inicio debe aparecer "Registra el inicio de tu período para ver el día de tu ciclo" (aún no hay datos).
3. Cada botón de Inicio debe abrir su pantalla, y "← Volver" (o el botón atrás del teléfono) debe volver a Inicio.

**Verificado:** `./gradlew :app:assembleDebug` compila sin errores. Las reglas de utils se probaron con una prueba temporal (3/3 correctas).
