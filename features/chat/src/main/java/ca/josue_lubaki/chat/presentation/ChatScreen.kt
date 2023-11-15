package ca.josue_lubaki.chat.presentation

import android.util.Log
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.josue_lubaki.common.domain.model.Message
import ca.josue_lubaki.common.domain.model.User
import ca.josue_lubaki.common.presentation.ProfileImage
import ca.josue_lubaki.common.ui.theme.ChatAppTheme
import org.koin.androidx.compose.koinViewModel

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

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(ChatEvent.OnLoadData(userId = userId))
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
    onSendMessage : (String) -> Unit
) {
    AnimatedContent(
        modifier = Modifier.fillMaxSize(),
        targetState = state,
        label = "animate chat list"
    ){ targetState ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(targetState){
                is ChatState.Loading -> {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ChatState.Success -> {
                    ChatList(
                        messages = targetState.data,
                        me = targetState.me,
                        modifier = Modifier
                            .wrapContentSize(
                                align = Alignment.BottomStart,
                            )
                            .weight(1f)
                    )
                }
                is ChatState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = targetState.error.message ?: "Error while loading messages",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                else -> Unit
            }

            ChatInput(
                onSendMessage = onSendMessage,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ChatInput(
    modifier: Modifier = Modifier,
    onSendMessage : (String) -> Unit
) {
    val message = rememberSaveable { mutableStateOf("") }
    val isValidate = remember { derivedStateOf { message.value.isNotBlank() }}
    val keyboardController = LocalSoftwareKeyboardController.current

    // Box for Message and icon send button
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
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
                        if (isValidate.value) {
                            onSendMessage(message.value)
                            message.value = ""
                            keyboardController?.show()
                        }
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
                keyboardController?.show()
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

@Composable
private fun ChatList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    me : User? = null,
) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = messages.size)
    LaunchedEffect(key1 = messages) {
        if (messages.isNotEmpty()) {
            scrollState.scrollToItem(
                index = messages.size - 1
            )
        }
    }

    Box(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopStart
    ){
        // Messages
        LazyColumn(state = scrollState) {
            items(
                items = messages,
                key = { it.messageId }
            ){
                MessageItem(
                    isMe = it.senderId == me?.userId,
                    message = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MessageItem(
    isMe: Boolean,
    message: Message,
    modifier: Modifier
) {
    val backgroundColor = if (isMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (isMe) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    val alignment = if (isMe) {
        Alignment.CenterEnd
    } else {
        Alignment.CenterStart
    }

    val padding = if (isMe) {
        PaddingValues(start = 64.dp, end = 0.dp, top = 4.dp, bottom = 4.dp)
    } else {
        PaddingValues(start = 0.dp, end = 64.dp, top = 4.dp, bottom = 4.dp)
    }

    val shape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = modifier,
        contentAlignment = alignment
    ){
        Card(
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = textColor
            ),
            shape = shape,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(padding)
        ) {
            Text(
                text = message.message,
                color = textColor,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatPreview() {
    ChatAppTheme {
        Content(
            state = ChatState.Success(
                data = listOf(
                    Message(
                        senderId = "1",
                        receiverId = "2",
                        message = "Hello"
                    ),
                    Message(
                        senderId = "2",
                        receiverId = "1",
                        message = "Hi"
                    ),
                    Message(
                        senderId = "1",
                        receiverId = "2",
                        message = "How are you?"
                    ),
                    Message(
                        senderId = "2",
                        receiverId = "1",
                        message = "I am fine"
                    ),
                    Message(
                        senderId = "1",
                        receiverId = "2",
                        message = "How about you?"
                    ),
                    Message(
                        senderId = "2",
                        receiverId = "1",
                        message = "I am fine too"
                    ),
                    Message(
                        senderId = "1",
                        receiverId = "2",
                        message = "Good to hear that"
                    ),
                    Message(
                        senderId = "2",
                        receiverId = "1",
                        message = "Yeah"
                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "are you begin with jetpack compose ?"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "Yes"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "How is it?"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "It's awesome"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "Yeah"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "I am loving it, it's so easy to use"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "Yeah"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "How about you ? you are expert in jetpack compose now 😄"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "No, I am still learning"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "I am not expert yet, I learn everyday"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "Cool"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "We can learn together, I'm free on weekend"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "Yeah, that's great idea"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "Cool"
//                    ),
//                    Message(
//                        senderId = "2",
//                        receiverId = "1",
//                        message = "See you on weekend"
//                    ),
//                    Message(
//                        senderId = "1",
//                        receiverId = "2",
//                        message = "Yeah, see you"
//                    )
                ),
                me = User(
                    userId = "1",
                    username = "Josue",
                    profileImage = ""
                )
            ),
            paddingValues = PaddingValues(),
            onSendMessage = {}
        )
    }
}