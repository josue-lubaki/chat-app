package ca.josue_lubaki.chat.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.josue_lubaki.chat.domain.Message
import ca.josue_lubaki.common.presentation.ProfileImage
import ca.josue_lubaki.common.ui.theme.ChatAppTheme

/**
 * created by Josue Lubaki
 * date : 2023-11-11
 * version : 1.0.0
 */

@Composable
fun ChatScreen(
    userId : String,
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: ChatViewModel = koinViewModel(),
    onNavigateToRoute: (String) -> Unit,
    onNavigateToProfile : () -> Unit,
    onBackPressed : () -> Boolean
) {
    val state by viewModel.state.collectAsState()

    val onSendMessage = { message : String ->
        viewModel.onEvent(ChatEvent.OnSendMessage(
            receiverId = userId,
            message = message,
        ))
    }

    LaunchedEffect(key1 = Unit, key2 = onSendMessage) {
        Log.d("xxxx", "ChatScreen: $userId")
        viewModel.onEvent(ChatEvent.OnLoadData)
    }

    Scaffold(
        topBar = {
            TopBar(
                state = state,
                onNavigateToProfile = onNavigateToProfile,
                onBackPressed = onBackPressed
            )
        }
    ) {
        Content(
            state = state,
            onNavigateToRoute = onNavigateToRoute,
            paddingValues = it,
            onSendMessage = onSendMessage
        )
    }
}

@Composable
private fun TopBar(
    state: ChatState,
    onNavigateToProfile : () -> Unit,
    onBackPressed : () -> Boolean
) {
    // Back Icon with title
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(end = 8.dp)
            .padding(vertical = 8.dp)
    ) {
        IconButton(
            onClick = { onBackPressed() }
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = "Chat",
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
            imageUrl = (state as? ChatState.Success)?.me?.profileImage ?: "",
            onClick = onNavigateToProfile
        )
    }
}

@Composable
private fun Content(
    state: ChatState,
    paddingValues: PaddingValues,
    onNavigateToRoute: (String) -> Unit,
    onSendMessage : (String) -> Unit
) {

    val message = rememberSaveable { mutableStateOf("") }
    val isValidate = remember { derivedStateOf { message.value.isNotBlank() }}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ){
            // Messages
            Text(
                text = "Messages",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight(600)
            )
        }

        // Box for Message and icon send button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            // Message
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ){
                // TextField for message
                TextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = {
                        Text(
                            text = "Message",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            // Send message
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                )
            }

            // Send button
            IconButton(
                enabled = isValidate.value,
                onClick = {
                    onSendMessage(message.value)
                    message.value = ""
                },
                modifier = Modifier
                    .background(color = Color(0xff346734).copy(alpha = 0.9f), shape = CircleShape)
                    .size(48.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                        .rotate(-30f)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatPreview() {
    ChatAppTheme {
        Content(
            state = ChatState.Idle,
            onNavigateToRoute = {},
            paddingValues = PaddingValues(),
            onSendMessage = {}
        )
    }
}