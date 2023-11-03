package ca.josue_lubaki.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineDispatcher

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

class LoginViewModel(
//    private val useCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            //==== if you need execute actions ====//
            is LoginEvent.OnLoadData -> getAllData()
        }
    }

    private fun getAllData() {
        _state.value = LoginState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                //==== if your useCase is a flow ====/
                /*
                    useCase().collect { response ->
                        when (response) {
                            is LoginStatus.Success -> {
                                _state.value = LoginState.Success(response.data)
                            }

                            is LoginStatus.Error -> {
                                _state.value = LoginState.Error(response.error)
                            }
                        }
                    }
                */

                //==== if it's not a flow ====//
                /*
                    when (val response = useCase()) {
                        is LoginStatus.Success -> {
                            _state.value = LoginState.Success(response.data)
                        }
                        is LoginStatus.Error -> {
                            _state.value = LoginState.Error(response.error)    
                        }
                    }
                */
            }
        } catch (e: Exception) {
            // handle errors
            _state.value = LoginState.Error(e)
        }
    }
}