package ca.josue_lubaki.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineDispatcher

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

class DashboardViewModel(
//    private val useCase: DashboardUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            //==== if you need execute actions ====//
            is DashboardEvent.OnLoadData -> getAllData()
        }
    }

    private fun getAllData() {
        _state.value = DashboardState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                //==== if your useCase is a flow ====/
                /*
                    useCase().collect { response ->
                        when (response) {
                            is DashboardStatus.Success -> {
                                _state.value = DashboardState.Success(response.data)
                            }

                            is DashboardStatus.Error -> {
                                _state.value = DashboardState.Error(response.error)
                            }
                        }
                    }
                */

                //==== if it's not a flow ====//
                /*
                    when (val response = useCase()) {
                        is DashboardStatus.Success -> {
                            _state.value = DashboardState.Success(response.data)
                        }
                        is DashboardStatus.Error -> {
                            _state.value = DashboardState.Error(response.error)    
                        }
                    }
                */
            }
        } catch (e: Exception) {
            // handle errors
            _state.value = DashboardState.Error(e)
        }
    }
}