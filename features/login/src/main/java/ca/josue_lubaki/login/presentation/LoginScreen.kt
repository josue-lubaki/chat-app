package ca.josue_lubaki.login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import ca.josue_lubaki.common.navigation.ScreenTarget
import ca.josue_lubaki.login.R

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

@Composable
fun LoginScreen(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToRoute: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state){
        when (state) {
            is LoginState.Success -> {
                onNavigateToRoute(ScreenTarget.Contact.route)
            }
            else -> Unit
        }
    }

    Content(
        state = state,
        onNavigateToRoute = onNavigateToRoute,
        onSignIn = { email, password ->
            viewModel.onEvent(LoginEvent.OnSignIn(email, password))
        }
    )
}

@Composable
private fun Content(
    state: LoginState,
    onNavigateToRoute: (String) -> Unit,
    onSignIn : (String, String) -> Unit
) {
    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        val isSignInEnabled = remember {
            derivedStateOf {
                email.value.isNotBlank()
                    && password.value.isNotBlank()
            }
        }

        Column(
            modifier = Modifier.imePadding()
        ) {
            Text(
                text = stringResource(R.string.sign_in_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 32.dp,
                    bottom = 16.dp
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = stringResource(R.string.email_input)) },
                    modifier = Modifier.padding(bottom = 16.dp),
                    maxLines = 1,
                )

                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = stringResource(R.string.password_input)) },
                    modifier = Modifier.padding(bottom = 16.dp),
                    maxLines = 1,
                    // password visual transformation
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isSignInEnabled.value)
                                onSignIn(email.value, password.value)
                        }
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector  = image, description)
                        }
                    }
                )

                if (state is LoginState.Error) {
                    Text(text = state.error.message ?: "")
                }
            }

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .padding(bottom = 16.dp)
                        .clickable { onNavigateToRoute(ScreenTarget.Register.route) },
                    text = stringResource(R.string.have_not_an_account),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight(600),
                    textDecoration = TextDecoration.Underline,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                          if (isSignInEnabled.value)
                              onSignIn(email.value, password.value)
                    },
                    modifier = Modifier
                        .widthIn(min = 120.dp)
                        .padding(bottom = 16.dp),
                    enabled = isSignInEnabled.value,
                ) {
                    Text(
                        text = stringResource(R.string.login_button).uppercase(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight(600),
                        letterSpacing = 0.75.sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    Content(
        state = LoginState.Idle,
        onNavigateToRoute = {},
        onSignIn = { _, _ -> }
    )
}