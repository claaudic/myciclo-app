package com.example.myciclo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myciclo.data.MyCicloRepository
import com.example.myciclo.data.room.AppDatabase
import com.example.myciclo.utils.calcularDiaDelCiclo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class InicioUiState(
    val ultimoPeriodo: LocalDate? = null,
    val diaDelCiclo: Int? = null
)

class InicioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MyCicloRepository(AppDatabase.getInstance(application))

    // El día del ciclo nunca se guarda: se calcula desde el último inicio de período.
    val uiState: StateFlow<InicioUiState> = repository.ultimoCiclo
        .map { ciclo ->
            InicioUiState(
                ultimoPeriodo = ciclo?.fechaInicio,
                diaDelCiclo = ciclo?.let { calcularDiaDelCiclo(it.fechaInicio) }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InicioUiState()
        )
}
