package ca.josue_lubaki.register.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.josue_lubaki.common.navigation.ScreenTarget
import ca.josue_lubaki.register.R
import org.koin.androidx.compose.koinViewModel

/**
 * created by Josue Lubaki
 * date : 2023-10-26
 * version : 1.0.0
 */

@Composable
fun RegisterScreen(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: RegisterViewModel = koinViewModel(),
    onNavigateToRoute: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state){
        when (state) {
            is RegisterState.Success -> {
                onNavigateToRoute(ScreenTarget.Login.route)
            }
            else -> Unit
        }
    }

    val onSubmit = { username : String, email : String, password : String ->
        viewModel.onEvent(RegisterEvent.OnRegister(
            username = username,
            email = email,
            password = password
        ))
    }

    Content(
        state = state,
        onNavigateToRoute = onNavigateToRoute,
        onSubmit = onSubmit
    )
}

@Composable
private fun Content(
    state: RegisterState,
    onNavigateToRoute: (String) -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    val username = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordConfirmed = rememberSaveable { mutableStateOf("") }
    val formValidated = remember {
        derivedStateOf {
            username.value.isNotEmpty()
                    && email.value.isNotEmpty()
                    && password.value.isNotEmpty()
                    && passwordConfirmed.value.isNotEmpty()
                    && password.value == passwordConfirmed.value
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 54.dp, vertical = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.signup),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight(600)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 54.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = stringResource(R.string.username_input)) }
            )

            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(R.string.email_input)) }
            )

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = stringResource(R.string.password_input)) }
            )

            TextField(
                value = passwordConfirmed.value,
                onValueChange = { passwordConfirmed.value = it },
                label = { Text(text = stringResource(R.string.password_confirm_input)) }
            )
            if (state is RegisterState.Error) {
                Text(text = state.error?.message ?: "")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 56.dp),
                onClick = {
                    if (formValidated.value) {
                        onSubmit(
                            /* username */ username.value,
                            /* email */ email.value,
                            /* password */ password.value
                        )
                    }
                },
                enabled = formValidated.value,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text = stringResource(R.string.register))
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .clickable { onNavigateToRoute(ScreenTarget.Login.route) },
                text = stringResource(R.string.already_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(600),
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    Content(
        state = RegisterState.Idle,
        onNavigateToRoute = {},
        onSubmit = { _, _, _ -> }
    )
}