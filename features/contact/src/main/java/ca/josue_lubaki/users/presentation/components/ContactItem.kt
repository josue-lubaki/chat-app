package ca.josue_lubaki.users.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * created by Josue Lubaki
 * date : 2023-11-06
 * version : 1.0.0
 */

@Composable
fun UserItem(
    username : String,
    profileImage : String?,
    onClick : () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        if (profileImage.isNullOrBlank()) {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = CircleShape)
            )
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
                    .size(64.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = username,
                fontWeight = FontWeight(600)
            )

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(
                    alpha = .6f
                )
            ) {
                Text(
                    text = "Dummy"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserItemPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(16.dp)
    ){
        MaterialTheme {
            UserItem(
                username = "Josue Lubaki",
                profileImage = ""
            )
        }
    }
}