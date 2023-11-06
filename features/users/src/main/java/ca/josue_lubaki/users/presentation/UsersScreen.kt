package ca.josue_lubaki.users.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.josue_lubaki.users.domain.model.User
import ca.josue_lubaki.users.presentation.components.UserItem

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {

        AnimatedContent(targetState = state, label = "animate users list") { targetState ->
            when (targetState) {
                is UsersState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(targetState.data) { user ->
                            UserItem(
                                username = user.username,
                                profileImage = user.profileImage,
                            )
                        }
                    }
                }
                is UsersState.Error -> {
                    targetState.error.message?.let {
                        Text(text = it)
                    }
                }
                UsersState.Loading -> {
                    CircularProgressIndicator()
                }
                else -> Unit
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    Content(
        state = UsersState.Success(
            data = listOf(
                User(
                    userId = "1",
                    username = "Josue Lubaki",
                    profileImage = ""
                ),
                User(
                    userId = "2",
                    username = "Josue Lubaki",
                    profileImage = ""
                )
            )
        ),
        onNavigateToRoute = {}
    )
}