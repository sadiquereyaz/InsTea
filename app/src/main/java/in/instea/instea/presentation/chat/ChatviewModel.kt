package `in`.instea.instea.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.domain.datamodel.Message
import `in`.instea.instea.domain.repo.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun reduceMultipleSpaces(input: String): String {
    return input.replace("\\s+".toRegex(), " ").trim()
}
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
                _chatUiState.value = (messages)
                // Update _chatUiState only once after collecting from both rooms

            }
        }
    }

    fun insertMessages(message: Message, receiverRoom: String, senderRoom: String) {
        viewModelScope.launch {
            netWorkChatRepository.insertMessage(message, receiverRoom, senderRoom)
            netWorkChatRepository.updateChatPartners(message.senderId,receiverRoom.removeSuffix(message.senderId))

        }
    }
}

data class ChatUiState(
    val chatstate:List<Message> = emptyList()
)