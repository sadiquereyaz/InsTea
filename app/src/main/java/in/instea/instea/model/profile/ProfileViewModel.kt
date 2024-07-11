package `in`.instea.instea.model.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.PostRepository
import `in`.instea.instea.data.UserRepository
import `in`.instea.instea.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow


class ProfileViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val userId: Int = 12345
//    private val _uiState = MutableStateFlow(ProfileUiState())
//    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /** The mutable State that stores the status of the most recent request */
//    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
//        private set


//    init {
//        getSavedPosts()
//    }

//    private fun getSavedPosts() {
//        viewModelScope.launch {
//            profileUiState = ProfileUiState.Loading
//            try {
//                profileUiState = ProfileUiState.Success(postRepository.getAllSavedPostsStream())
//            } catch (e: IOException) {
//                profileUiState = ProfileUiState.Error
//            }
//        }
//    }
companion object{
    private const val TIMEOUT_MILLIS = 5_000L
}

//    val profileUiState: StateFlow<ProfileUiState> =
//        postRepository.getAllSavedPostsStream().map { ProfileUiState(savedPosts = it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = ProfileUiState()
//            )

    val profileUiState: StateFlow<ProfileUiState> = combine(
        userRepository.getUserById(userId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserModel()
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
    // ... other functions (onUserNameChanged, etc.) can remain here

//    fun onUserNameChanged(newName: String) {
////        Log.d("user change", "new name is $newName")
//        _uiState.update { currentState ->
//            currentState.copy(userName = newName)
//        }
//    }
//
//    fun onDepartmentChange(newDepartment: String) {
//        _uiState.value = _uiState.value.copy(selectedDepartment = newDepartment)
//    }
//
//    fun onSemesterChange(newSemester: String) {
//        _uiState.value = _uiState.value.copy(selectedSemester = newSemester)
//    }
}