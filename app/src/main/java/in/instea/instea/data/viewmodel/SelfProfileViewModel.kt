package `in`.instea.instea.data.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Whatsapp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.PostRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.profile.ProfileUiState
import `in`.instea.instea.screens.profile.SocialModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SelfProfileViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val userId = Firebase.auth.currentUser?.uid.toString()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val profileUiState: StateFlow<ProfileUiState> = combine(
        userRepository.getUserById(userId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = User()
        ),
        postRepository.getAllSavedPostsStream().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )
    ) { userDetails, savedPosts ->
        // This lambda now returns ProfileUiState directly
        ProfileUiState(savedPosts = savedPosts, userData = userDetails)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ProfileUiState()
        )


    // Handle social item click based on the link
    fun handleSocialItemClick(link: String, context: Context) {
        Log.d("LINK", link)
        // Implement logic to open the link in browser or app
        // You can use libraries like Intent or deep linking to achieve this

        // Example using Intent:
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Handle case where no app can handle the intent
            Log.e("LINK", "No application can handle this request. Please install a web browser or check your URL.")
        }
    }

    fun getAllSocialList(): List<SocialModel> {
        val user = profileUiState.value.userData
        return listOf(
            SocialModel(
                title = user?.whatsappNo ?: "",
                linkHead = "https://wa.me/",
                icon = Icons.Default.Whatsapp
            ),
            SocialModel(
                title = user?.instaId ?: "",
                linkHead = "https://wa.me/",
                icon = R.drawable.insta
            ),
            SocialModel(
                title = user?.linkedinId ?:"",
                linkHead = "https://www.linkedin.com/in/",
                icon = R.drawable.linked
            ),
            SocialModel(
                title = "mdsadique47@gmail.com",
                linkHead = "mailto:",
                icon = Icons.Default.AlternateEmail
            ),
        )
    }
}