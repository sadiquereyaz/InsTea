package `in`.instea.instea.domain.repo

import `in`.instea.instea.domain.datamodel.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllMesaages(senderRoom: String, receiverRoom: String): Flow<List<Message>>
    suspend fun insertMessage(message: Message, senderRoom: String, receiverRoom: String)
    suspend fun updateChatPartners(senderId: String, receiverId: String)
}