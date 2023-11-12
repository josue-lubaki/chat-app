package ca.josue_lubaki.common.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * created by Josue Lubaki
 * date : 2023-11-11
 * version : 1.0.0
 */

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