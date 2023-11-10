package ca.josue_lubaki.profile

import android.graphics.Bitmap
import android.net.Uri

/**
 * created by Josue Lubaki
 * date : 2023-11-08
 * version : 1.0.0
 */

sealed class ProfileEvent {
    data class OnUploadImage(val uri: Uri) : ProfileEvent()

    data object OnLoadData : ProfileEvent()
}