package com.example.myciclo.data

import com.example.myciclo.data.room.AppDatabase
import com.example.myciclo.data.room.CicloEntity
import com.example.myciclo.data.room.RegistroDiarioEntity
import com.example.myciclo.data.room.RegistroEvaEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

// Único punto de acceso a los datos para los ViewModel.
// Las pantallas nunca llaman a los DAO directamente.
class MyCicloRepository(database: AppDatabase) {

    private val cicloDao = database.cicloDao()
    private val registroDiarioDao = database.registroDiarioDao()
    private val registroEvaDao = database.registroEvaDao()

    // Ciclos
    val ciclos: Flow<List<CicloEntity>> = cicloDao.obtenerTodos()
    val ultimoCiclo: Flow<CicloEntity?> = cicloDao.obtenerUltimo()

    suspend fun registrarInicioPeriodo(fecha: LocalDate): Long =
        cicloDao.insertar(CicloEntity(fechaInicio = fecha))

    suspend fun eliminarCiclo(ciclo: CicloEntity) = cicloDao.eliminar(ciclo)

    // Registro diario
    fun registroDiarioDe(fecha: LocalDate): Flow<RegistroDiarioEntity?> =
        registroDiarioDao.obtenerPorFecha(fecha)

    fun registrosDiariosEntre(desde: LocalDate, hasta: LocalDate): Flow<List<RegistroDiarioEntity>> =
        registroDiarioDao.obtenerEntre(desde, hasta)

    suspend fun guardarRegistroDiario(registro: RegistroDiarioEntity) =
        registroDiarioDao.guardar(registro)

    suspend fun eliminarRegistroDiario(registro: RegistroDiarioEntity) =
        registroDiarioDao.eliminar(registro)

    // Registro EVA
    val observacionesConHelechos: Flow<List<RegistroEvaEntity>> = registroEvaDao.obtenerConHelechos()

    fun observacionesEvaDe(fecha: LocalDate): Flow<List<RegistroEvaEntity>> =
        registroEvaDao.obtenerEntre(fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay())

    suspend fun guardarObservacionEva(registro: RegistroEvaEntity): Long =
        registroEvaDao.insertar(registro)

    suspend fun actualizarObservacionEva(registro: RegistroEvaEntity) =
        registroEvaDao.actualizar(registro)

    suspend fun eliminarObservacionEva(registro: RegistroEvaEntity) =
        registroEvaDao.eliminar(registro)
}
