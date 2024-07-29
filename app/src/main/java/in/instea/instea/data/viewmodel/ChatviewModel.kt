
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatviewModel(
    val netWorkChatRepository: ChatRepository
) : ViewModel() {
    private val _chatUiState = MutableStateFlow<List<Message>>(emptyList())
    val chatUiState: StateFlow<List<Message>> get() = _chatUiState


    fun getChats(senderRoom: String, receiverRoom: String) {
        viewModelScope.launch {
            // Combine messages from both rooms
            val allMessages = mutableListOf<Message>()
            netWorkChatRepository.getAllMesaages(senderRoom, receiverRoom).collect { messages ->
                allMessages.addAll(messages)
                // Update _chatUiState only once after collecting from both rooms
                _chatUiState.update { allMessages }
            }
        }
    }

    fun insertMessages(message: Message,receiverRoom: String, senderRoom: String) {
        viewModelScope.launch {
            netWorkChatRepository.insertMessage(message, receiverRoom, senderRoom)
        }
    }
}

data class ChatUiState(
    val chatstate:List<Message> = emptyList()
)