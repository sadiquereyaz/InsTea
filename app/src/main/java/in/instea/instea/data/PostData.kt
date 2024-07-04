package `in`.instea.instea.data

import android.location.Location
import androidx.compose.runtime.Immutable

@Immutable
data class PostData(
    val name: String?,
    val location: String?,
    val profileImage: Int?,
    val postDescription: String?,
    val postImage: Int?,
)
