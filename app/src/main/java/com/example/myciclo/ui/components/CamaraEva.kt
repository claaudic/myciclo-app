package com.example.myciclo.ui.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.myciclo.ui.theme.MyCicloPrimaryDark
import java.io.File

/**
 * Se ocupa SOLO de la camara: permiso, vista previa y foto temporal.
 * La ruta se entrega hacia arriba; Room se conectara en una tarjeta posterior.
 * La foto queda en cacheDir (carpeta privada), NO en la galeria.
 */
@Composable
fun CamaraEva(
    onFotoSeleccionada: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var permisoConcedido by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var permisoRechazado by rememberSaveable { mutableStateOf(false) }
    var rutaCapturada by rememberSaveable { mutableStateOf<String?>(null) }
    var fotoAceptada by rememberSaveable { mutableStateOf(false) }
    var sinFoto by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    // Si se vuelve desde Ajustes, se revisa otra vez el permiso.
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                permisoConcedido =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val pedirPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permitido ->
        permisoConcedido = permitido
        permisoRechazado = !permitido
        if (permitido) error = null
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when {
            rutaCapturada != null -> {
                // Una imagen tomada nunca es obligatoria: puede repetirse o descartarse.
                val ruta = rutaCapturada!!
                VistaPreviaFotoEva(ruta)
                Text(
                    if (fotoAceptada) "Fotografía seleccionada" else "Revisa tu fotografía",
                    color = Color.White
                )
                if (!fotoAceptada) {
                    BotonCamaraEva("Usar foto") {
                        fotoAceptada = true
                        onFotoSeleccionada(ruta)
                    }
                }
                OutlinedButton(onClick = {
                    File(ruta).delete()
                    rutaCapturada = null
                    fotoAceptada = false
                    sinFoto = false
                    error = null
                    onFotoSeleccionada(null)
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White, contentColor = MyCicloPrimaryDark
                )) {
                    Text("Repetir foto")
                }
                TextButton(onClick = {
                    File(ruta).delete()
                    rutaCapturada = null
                    fotoAceptada = false
                    sinFoto = true
                    onFotoSeleccionada(null)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Seguir sin foto", color = Color.White)
                }
            }
            sinFoto -> {
                Text("Continuarás sin fotografía.", color = Color.White)
                Text("Podrás registrar Con helechos o Sin helechos igualmente.", color = Color.White)
                BotonCamaraEva("Quiero tomar una foto") { sinFoto = false }
            }
            !permisoConcedido -> {
                Text("Necesitamos acceso a la cámara", color = Color.White)
                Text(
                    "Para fotografiar tu muestra con la cámara frontal. " +
                        "La foto se guarda solo en este teléfono.",
                    color = Color.White
                )
                if (permisoRechazado) {
                    Text("El permiso fue rechazado. Puedes habilitarlo en Ajustes.", color = Color.White)
                }
                BotonCamaraEva("Permitir cámara") {
                    pedirPermiso.launch(Manifest.permission.CAMERA)
                }
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White, contentColor = MyCicloPrimaryDark
                    )
                ) {
                    Text("Permitir en Ajustes")
                }
                TextButton(onClick = {
                    sinFoto = true
                    onFotoSeleccionada(null)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Seguir sin foto", color = Color.White)
                }
            }
            !context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) -> {
                Text("Este dispositivo no tiene cámara frontal disponible.", color = Color.White)
                BotonCamaraEva("Seguir sin foto") {
                    sinFoto = true
                    onFotoSeleccionada(null)
                }
            }
            else -> {
                CamaraEvaEnVivo(
                    onFotoTomada = { ruta ->
                        rutaCapturada = ruta
                        fotoAceptada = false
                        error = null
                        onFotoSeleccionada(null) // Debe confirmar con «Usar foto».
                    },
                    onError = { error = it }
                )
                TextButton(onClick = {
                    sinFoto = true
                    onFotoSeleccionada(null)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Seguir sin foto", color = Color.White)
                }
            }
        }
        if (error != null) {
            Text(error!!, color = Color(0xFFFFD7D7))
        }
        Text(
            "La fotografía es opcional y queda privada en este dispositivo.",
            color = Color.LightGray
        )
    }
}

@Composable
private fun CamaraEvaEnVivo(
    onFotoTomada: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var capturando by remember { mutableStateOf(false) }
    val informarError = onError

    val controller = remember(context) {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    // CameraX abre y cierra la camara segun el ciclo de vida de la pantalla.
    DisposableEffect(controller, lifecycleOwner) {
        try {
            controller.bindToLifecycle(lifecycleOwner)
        } catch (exception: Exception) {
            onError("No se pudo iniciar la cámara frontal: ${exception.localizedMessage ?: "error desconocido"}")
        }
        onDispose { controller.unbind() }
    }

    AndroidView(
        factory = { androidContext ->
            PreviewView(androidContext).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                this.controller = controller
            }
        },
        modifier = Modifier.fillMaxWidth().height(210.dp)
    )

    BotonCamaraEva(
        texto = if (capturando) "Tomando fotografía..." else "Tomar foto",
        habilitado = !capturando
    ) {
        try {
            val carpeta = File(context.cacheDir, "eva").apply { mkdirs() }
            val archivo = File.createTempFile("eva_", ".jpg", carpeta)
            val opciones = ImageCapture.OutputFileOptions.Builder(archivo).build()
            capturando = true
            controller.takePicture(
                opciones,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(resultado: ImageCapture.OutputFileResults) {
                        capturando = false
                        onFotoTomada(archivo.absolutePath)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        capturando = false
                        archivo.delete()
                        informarError("No se pudo tomar la foto: ${exception.localizedMessage ?: "intenta de nuevo"}")
                    }
                }
            )
        } catch (exception: Exception) {
            capturando = false
            onError("No se pudo iniciar la captura: ${exception.localizedMessage ?: "intenta de nuevo"}")
        }
    }
}

@Composable
private fun VistaPreviaFotoEva(ruta: String) {
    val bitmap = remember(ruta) { cargarFotoParaVistaPrevia(ruta) }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Vista previa de la muestra EVA",
            modifier = Modifier.fillMaxWidth().height(210.dp),
            contentScale = ContentScale.Fit
        )
    } else {
        Text("No se pudo mostrar la fotografía. Puedes repetirla.", color = Color.White)
    }
}

// Lee una version reducida para no llenar la memoria con fotos grandes.
private fun cargarFotoParaVistaPrevia(ruta: String): Bitmap? {
    val tamaño = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(ruta, tamaño)
    if (tamaño.outWidth <= 0 || tamaño.outHeight <= 0) return null
    var muestra = 1
    while (tamaño.outWidth / muestra > 1200 || tamaño.outHeight / muestra > 1200) {
        muestra *= 2
    }
    val bitmap = BitmapFactory.decodeFile(ruta, BitmapFactory.Options().apply {
        inSampleSize = muestra
    }) ?: return null
    val giro = try {
        when (ExifInterface(ruta).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
    } catch (_: Exception) { 0f }
    if (giro == 0f) return bitmap
    val matriz = Matrix().apply { postRotate(giro) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matriz, true).also {
        bitmap.recycle()
    }
}

@Composable
private fun BotonCamaraEva(
    texto: String,
    habilitado: Boolean = true,
    alPulsar: () -> Unit
) {
    Button(
        onClick = alPulsar,
        enabled = habilitado,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MyCicloPrimaryDark
        )
    ) {
        Text(texto, modifier = Modifier.padding(vertical = 3.dp))
    }
}
