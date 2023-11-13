package ca.josue_lubaki.users.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.josue_lubaki.common.domain.model.User
import ca.josue_lubaki.users.presentation.components.UserItem
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

@Composable
fun ContactsScreen(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: ContactViewModel = koinViewModel(),
    onNavigateToProfile : () -> Unit,
    onNavigateToChat : (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ContactEvent.OnLoadData)
    }

    Scaffold(
        topBar = {
            TopBar(
                state = state,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) {
        Content(
            state = state,
            paddingValues = it,
            onNavigateToChat = onNavigateToChat
        )
    }
}

@Composable
fun TopBar(
    state: ContactState,
    onNavigateToProfile : () -> Unit
) {
    // Back Icon with title
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .size(64.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(end = 8.dp)
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = "Contacts",
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight(600)
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        ProfileImage(
            imageUrl = (state as? ContactState.Success)?.me?.profileImage ?: "",
            onClick = onNavigateToProfile
        )
    }
}

@Composable
fun ProfileImage(
    imageUrl: String?,
    onClick : () -> Unit
) {
    if (imageUrl.isNullOrEmpty()) {
        Image(
            imageVector = Icons.Filled.Person,
            contentDescription = "User Image",
            modifier = Modifier
                .size(36.dp)
                .clip(shape = CircleShape)
                .clickable {
                    onClick()
                }
        )
    } else {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = "User Image",
            modifier = Modifier
                .size(36.dp)
                .clip(shape = CircleShape)
                .clickable {
                    onClick()
                },
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun Content(
    state: ContactState,
    paddingValues : PaddingValues,
    onNavigateToChat : (String) -> Unit
) {

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {

        AnimatedContent(targetState = state, label = "animate users list") { targetState ->
            when (targetState) {
                is ContactState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = targetState.data,
                            key = { user -> user.userId }
                        ) { user ->
                            UserItem(
                                username = user.username,
                                profileImage = user.profileImage,
                                message = targetState.lastMessages[user.userId] ?: "",
                                onClick = { onNavigateToChat(user.userId) }
                            )
                        }
                    }
                }
                is ContactState.Error -> {
                    targetState.error.message?.let {
                        Text(text = it)
                    }
                }
                ContactState.Loading -> {
                    CircularProgressIndicator()
                }
                else -> Unit
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun UsersPreview() {
    Content(
        state = ContactState.Success(
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
        paddingValues = PaddingValues(),
        onNavigateToChat = {}
    )
}