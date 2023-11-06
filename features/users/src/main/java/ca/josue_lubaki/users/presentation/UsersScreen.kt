package ca.josue_lubaki.users.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

@Composable
fun DashboardScreen(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: UsersViewModel = koinViewModel(),
    onNavigateToRoute: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(UsersEvent.OnLoadData)
    }

    Content(
        state = state,
        onNavigateToRoute = onNavigateToRoute
    )
}

@Composable
private fun Content(
    state: UsersState,
    onNavigateToRoute: (String) -> Unit
) {

    Box(contentAlignment = Alignment.Center) {
        Text(text = "Dashboard")
    }

}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    Content(
        state = UsersState.Idle,
        onNavigateToRoute = {}
    )
}