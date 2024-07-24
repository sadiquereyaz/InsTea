package `in`.instea.instea.data.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Whatsapp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.PostRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.profile.ProfileDestination
import `in`.instea.instea.screens.profile.ProfileUiState
import `in`.instea.instea.screens.profile.SocialModel
import `in`.instea.instea.screens.schedule.EditScheduleDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val currentUserId = Firebase.auth.currentUser?.uid.toString()
//    private val argUserId = "XjyI0mZ4XbXb6paiz11QFjMQuYJ2"
//    private val argUserId =currentUserId
    private val argUserId: String = checkNotNull(savedStateHandle[ProfileDestination.USERID_ARG])


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        fetchInitialData()
//        checkProfileType()
        getUserData()
//        getAllProfilePost()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            val myProfile = argUserId == currentUserId
            _uiState.update { currState ->
                currState.copy(
                    isSelfProfile = myProfile
                )
            }
            if (myProfile) {
                postRepository.getAllSavedPostsStream().collect { posts ->
                    _uiState.update { currState ->
                        currState.copy(savedPosts = posts)
                    }
                }
            }
            //TODO: get profile posts
            postRepository.getPostsByUser(argUserId).collect { posts ->
                _uiState.update { currState ->
                    currState.copy(profilePosts = posts)
                }
            }
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            userRepository.getUserById(argUserId).collect { user ->
                _uiState.update { currState ->
                    currState.copy(userData = user)
                }
                getAllSocialList(user)
            }
        }
    }

    /* private suspend fun getAllProfilePost() {
             if (_uiState.value.isSelfProfile) {
                 postRepository.getAllSavedPostsStream().collect { posts ->
                     _uiState.update { currState ->
                         currState.copy(savedPosts = posts)
                     }
                 }
             }
             //TODO: get profile posts
             postRepository.getPostsByUser(currentUserId).collect { posts ->
                 _uiState.update { currState ->
                     currState.copy(profilePosts = posts)
                 }
             }
     }*/

    /*val profileUiState: StateFlow<ProfileUiState> = combine(
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
        )*/


    // Handle social item click based on the link
    fun handleSocialItemClick(link: String, context: Context) {
        Log.d("LINK", link)
        // Implement logic to open the link in browser or app
        // You can use libraries like Intent or deep linking to achieve this

        // Example using Intent:
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        context.startActivity(intent)
//        Log.d("Link", "handleSocialItemClick: $link")
    }

    private fun getAllSocialList(user: User) {
        viewModelScope.launch {
            val socialList = listOf(
                SocialModel(
                    title = user.whatsappNo ?: "",
                    linkHead = "https://wa.me/+91",
                    icon = Icons.Default.Whatsapp
                ),
                SocialModel(
                    title = user.instaId ?: "",
                    linkHead = "https://instagram.com/",
                    icon = R.drawable.insta
                ),
                SocialModel(
                    title = user.linkedinId ?: "",
                    linkHead = "https://www.linkedin.com/in/",
                    icon = R.drawable.linked
                ),
                SocialModel(
                    title = user.email ?: "",
                    linkHead = "mailto:",
                    icon = Icons.Default.AlternateEmail
                ),
            )
            _uiState.update {
                it.copy(socialList = socialList)
            }
        }
    }
}