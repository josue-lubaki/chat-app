package ca.josue_lubaki.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.josue_lubaki.common.domain.model.User
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import java.io.IOException

/**
 * created by Josue Lubaki
 * date : 2023-11-08
 * version : 1.0.0
 */

@Composable
fun ProfileScreen(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToRoute: (String) -> Unit,
    onBackPressed: () -> Boolean
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val imageUser = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.OnLoadData)
    }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if(uri == null) {
            Log.d("PhotoPicker", "No media selected")
            return@rememberLauncherForActivityResult
        }

        try {
            imageUser.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            viewModel.onEvent(ProfileEvent.OnUploadImage(uri))
        }
        catch (e: IOException) {
            Log.d("PhotoPicker", "Error: $e")
        }
    }

    val uploadImageRequest = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    Scaffold(
        topBar = {
            TopBar(
                onBackPressed = onBackPressed
            )
        }
    ) {
        Content(
            state = state,
            onNavigateToRoute = onNavigateToRoute,
            paddingValue = it,
            imageUser = imageUser.value,
            chooseImage = uploadImageRequest,
        )
    }
}

@Composable
fun TopBar(onBackPressed: () -> Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
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
            text = "Profile",
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight(600)
        )
    }
}

@Composable
private fun Content(
    state: ProfileState,
    onNavigateToRoute: (String) -> Unit,
    paddingValue : PaddingValues,
    imageUser : Bitmap?,
    chooseImage : () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
    ){
        AnimatedContent(targetState = state, label = "animate profile content"){ targetState ->
            when(targetState) {
                is ProfileState.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ){
                        ContentSuccess(
                            state = state,
                            imageUser = imageUser,
                            chooseImage = chooseImage,
                        )
                    }
                }
                is ProfileState.Error -> {
                    Box(contentAlignment = Alignment.Center){
                        Text(
                            text = targetState.error.localizedMessage ?: "Error",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                ProfileState.Loading -> {
                    Box(contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                else -> Unit
            }
        }

    }
}

@Composable
fun ContentSuccess(
    state: ProfileState,
    imageUser : Bitmap?,
    chooseImage : () -> Unit,
) {
    with(state as ProfileState.Success){
        ProfileContent(
            user = data,
            imageUser = imageUser,
            chooseImage = chooseImage
        )
    }
}

@Composable
fun ProfileContent(
    user: User?,
    imageUser : Bitmap?,
    chooseImage : () -> Unit
) {
   Column(
       modifier = Modifier.fillMaxSize(),
       verticalArrangement = Arrangement.spacedBy(8.dp),
       horizontalAlignment = Alignment.CenterHorizontally
   ){
       if(user == null){
           Image(
               imageVector = Icons.Filled.Person,
               contentDescription = "User Image",
               modifier = Modifier
                   .size(96.dp)
                   .clip(shape = CircleShape)
           )
       }
       else {
           val (_, _, profileImage) = user
           if (profileImage.isBlank()) {
               if(imageUser != null){
                   Image(
                       bitmap = imageUser.asImageBitmap(),
                       contentDescription = "User Image",
                       modifier = Modifier
                           .size(96.dp)
                           .clip(shape = CircleShape)
                           .clickable {
                               chooseImage()
                           }
                   )
               }
               else{
                   Image(
                       imageVector = Icons.Filled.Person,
                       contentDescription = "User Image",
                       modifier = Modifier
                           .size(96.dp)
                           .clip(shape = CircleShape)
                           .clickable {
                               chooseImage()
                           }
                   )
               }
           } else {
               SubcomposeAsyncImage(
                   model = ImageRequest.Builder(LocalContext.current)
                       .data(profileImage)
                       .crossfade(true)
                       .build(),
                   loading = {
                       CircularProgressIndicator()
                   },
                   contentDescription = "User Image",
                   modifier = Modifier
                       .size(96.dp)
                       .clip(shape = CircleShape)
                       .clickable {
                           chooseImage()
                       }
                   ,
                   contentScale = ContentScale.Crop
               )
           }

           Text(
               text = user.username,
               style = MaterialTheme.typography.titleLarge
           )
       }
   }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    Content(
        state = ProfileState.Success(
            data = User(
                userId = "1",
                username = "Josue Lubaki",
                profileImage = ""
            )
        ),
        onNavigateToRoute = {},
        paddingValue = PaddingValues(),
        chooseImage = {},
        imageUser = null,
    )
}