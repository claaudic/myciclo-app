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

---

## 2026-10-08 · Registro diario con MVVM, validaciones y guardado en Room

**Objetivo:** que el formulario "Registro del día" guarde en la base de datos, con las validaciones fuera de la pantalla (MVVM), mensajes con ícono y una animación en los errores.

### Archivos

| Archivo | Cambio |
|---|---|
| `viewmodel/RegistroViewModel.kt` (nuevo) | `RegistroUiState` (datos del formulario + mensajes de error) en un `StateFlow`. Una función `onXChange` por campo y `guardar(onGuardado)`, que valida, guarda en Room y luego vuelve a Inicio. |
| `utils/Validaciones.kt` | Se agregaron `MAX_SINTOMAS` (100), `MAX_BIOMARCADOR` (50), `MAX_NOTAS` (300), `validarLargoTexto()` y `validarFlujo()`. |
| `ui/screens/RegistroScreen.kt` | Mismo diseño. La sección "Otro biomarcador" pasó a llamarse **"Otros síntomas · Opcional"** (más fácil de entender; en la base de datos sigue siendo `biomarcador`). Se quitaron los `remember { mutableStateOf }`: ahora muestra el `uiState` del ViewModel. El flujo usa el enum `Flujo`. Nuevos `CampoTexto` (borde rojo, ícono y mensaje de error) y `MensajeError` (aparece con `AnimatedVisibility`). El parámetro `onGuardar` pasó a llamarse `onGuardado`. |
| `navigation/Navigation.kt` | La ruta `registro_diario` crea el `RegistroViewModel` con `viewModel()` y, al guardar, vuelve a Inicio. |
| `data/room/CicloDao.kt` | `insertar` usa `OnConflictStrategy.IGNORE`: si ya hay un ciclo con esa fecha de inicio, no se duplica. |
| `res/drawable/ic_error.xml` (nuevo) | Ícono de error (vector de Material), para no agregar la librería de íconos. |

### Validaciones (se revisan al presionar "Guardar")

| Campo | Regla |
|---|---|
| Formulario | Debe tener al menos un dato |
| Flujo | Si "¿Comenzó tu período hoy?" = Sí, es obligatorio y no puede ser "Sin" |
| Síntomas / otros síntomas / notas | Opcionales; máximo 100 / 50 / 300 caracteres |

### Qué se guarda
- Un `RegistroDiarioEntity` con la fecha de hoy. Si ya existía uno de hoy, se reemplaza (corregir).
- Si "¿Comenzó tu período hoy?" = **Sí**, también se crea un ciclo con inicio hoy. Por eso Inicio pasa a mostrar el día 1.

### También va en este commit
Cambios anteriores sin commit: `BienvenidaScreen` y `LoginScreen` nuevas, rediseño de `RegistroScreen`, `Color.kt`, `Theme.kt` y `Navigation.kt`.

**Cómo probar**
1. Inicio → **Nuevo registro** → **Guardar** sin llenar nada: aparece el error con ícono.
2. Elegir **Sí** y flujo **Sin** → Guardar: error de flujo. Elegir **Leve** y guardar de nuevo.
3. Escribir más de 50 caracteres en "Otros síntomas" → Guardar: el campo queda en rojo con ícono.
4. Con datos válidos, Guardar: vuelve a Inicio con el día 1. Cerrar y abrir la app: el dato sigue ahí.

**Verificado:** `./gradlew :app:assembleDebug` compila sin errores. Falta probar en el emulador.

---

## 2026-10-08 · Paleta de colores de myciclo.cl y fuente Poppins

**Objetivo:** que toda la app use los colores de la página de MyCiclo, como pidió Carolina. Se eligió la versión **"Blanco y dorado"**: fondo blanco y el dorado solo para lo importante.

| Archivo | Cambio |
|---|---|
| `ui/theme/Color.kt` | Nuevos valores, con los mismos nombres (las pantallas no cambian): botones en dorado oscuro `#8A6A2C`, dorado de la web `#B68D40` para detalles, texto `#181210`, fondo blanco. Se agregaron `MyCicloPlum` (ciruela, para EVA) y `MyCicloBorder`. |
| `ui/theme/Theme.kt` | `outline` y `outlineVariant` usan `MyCicloBorder`. |
| `ui/theme/Type.kt` | Títulos (`headline`, `titleLarge`, `titleMedium`) en Poppins; el texto normal sigue en Roboto. |
| `res/font/` (nuevo) | `poppins_medium.ttf`, `poppins_semibold.ttf`, `poppins_bold.ttf` (Google Fonts, licencia libre OFL). |

Cuando llegue el manual de marca, los tonos se ajustan solo en `Color.kt`.

**Cómo probar:** ejecutar la app; los botones deben verse dorado oscuro y los títulos en Poppins.

**Verificado:** `./gradlew :app:assembleDebug` compila sin errores.

---

## 2026-10-09 · Sistema visual MyCiclo (tarjeta Trello)

**Objetivo:** aplicar el sistema visual de los mockups oficiales "myCiclo · Pantallas de la app" y dejar componentes reutilizables para todo el equipo. Reemplaza la paleta dorada y la fuente Poppins del 2026-10-08.

**Archivos modificados (compartidos)**

| Archivo | Cambio |
|---|---|
| `ui/theme/Color.kt` | Paleta oficial: morado marca `#2D055B`, morado suave `#F3EEF9`, rosado período `#D6457A`, violeta EVA `#6A4BA8`, lavanda `#EEE7F7`, dorado `#C4963C`, crema `#FBF7EE`, texto `#1E1430`, borde `#ECE6F1`, error, éxito y deshabilitado. Se mantuvieron los nombres anteriores (`MyCicloPrimary`, `MyCicloGold`, etc.) para no romper otras pantallas; `MyCicloPlum` pasó a llamarse `MyCicloEva`. |
| `ui/theme/Theme.kt` | El esquema de Material usa la paleta nueva. Solo tema claro y **sin color dinámico** (el teléfono no cambia los colores de la app). |
| `ui/theme/Type.kt` | **Outfit** para títulos (Light 34 bienvenida, 28 título de pantalla, Medium 17 título de tarjeta) y **DM Sans** para el texto (15 principal, 13 secundario, 11 etiquetas en mayúscula, 16 negrita en botones). |

**Archivos nuevos**

| Archivo | Para qué |
|---|---|
| `res/font/outfit_*.ttf`, `res/font/dmsans_*.ttf` | Fuentes del mockup (se eliminaron `poppins_*.ttf`). |
| `ui/theme/Medidas.kt` | Medidas comunes: margen lateral 20 dp, separación 16 dp, radio de tarjeta 22 dp, botón 56 dp, chip 40 dp, área táctil 44 dp. |
| `ui/components/Botones.kt` | `BotonPrincipal` (normal, deshabilitado, cargando) y `BotonSecundario`. |
| `ui/components/Tarjetas.kt` | `TarjetaMyCiclo`: tarjeta con borde y fondo configurable. |
| `ui/components/Chips.kt` | `ChipMyCiclo`: seleccionado = borde 2 dp + fondo suave + ✓, animado en 150 ms. Colores cambiables para período o EVA. |
| `ui/components/Estados.kt` | `MensajeError` y `MensajeExito` con ícono y `AnimatedVisibility` de 200 ms. |
| `ui/components/VistaPreviaComponentes.kt` | `@Preview` con todos los componentes, para revisarlos en Android Studio. |
| `res/drawable/ic_check.xml` | Ícono ✓. |

**Para el equipo:** Cristopher y Nicolas pueden usar estos componentes en sus pantallas (`BotonPrincipal`, `ChipMyCiclo`, `TarjetaMyCiclo`, `MensajeError`). No se tocó ninguna pantalla de ellos; solo cambian colores y letra porque vienen del tema.

**Cómo probar**
1. Abrir `VistaPreviaComponentes.kt` en Android Studio y ver la pestaña *Split*: deben verse los botones, chips, tarjetas y mensajes con los colores del mockup.
2. Ejecutar la app en el emulador: los textos usan las fuentes nuevas y los botones son morados.

**Verificado:** `./gradlew assembleDebug` y `./gradlew test` sin errores.

---

## 2026-10-09 · Bienvenida (tarjeta Trello)

**Objetivo:** dejar la pantalla de Bienvenida igual al mockup oficial, usando el sistema visual nuevo.

**Archivos**

| Archivo | Cambio |
|---|---|
| `ui/screens/BienvenidaScreen.kt` | Reescrito según el mockup: logo "myCiclo" ("my" rosado, "Ciclo" morado), etiqueta "HECHO EN CHILE", ilustración del ciclo hecha con `Canvas` (círculo fino con arco dorado, anillo con arcos rosado y violeta, gota al centro), título "Conoce tu ciclo, a tu manera", descripción, `BotonPrincipal` "Comenzar" y mensaje de privacidad con candado. Se agregó `@Preview`. |
| `res/drawable/ic_candado.xml` | Ícono de candado (nuevo). |
| `res/drawable/ic_gota.xml` | Ícono de gota del período (nuevo; también servirá en Hoy). |

**Adaptabilidad:** la ilustración está dentro de un `Box` con `weight(1f)`: usa el espacio que sobra y se achica en pantallas bajas, así el botón y el mensaje de privacidad siempre se ven. Probado en el emulador a 1080×2400 y simulando una pantalla de 1080×1600.

**Pendiente:** "Comenzar" sigue llevando a Inicio. Pasará al Onboarding en la tarjeta *Onboarding · Paso 1*, cuando exista esa pantalla (así esta tarjeta no toca `Navigation.kt`).

**Cómo probar**
1. Ejecutar la app (Run ▶): la primera pantalla debe verse como el mockup `1 · Bienvenida`.
2. En Android Studio, abrir `BienvenidaScreen.kt` → *Split* para ver la vista previa.

**Verificado:** `./gradlew assembleDebug` y `./gradlew test` sin errores.
