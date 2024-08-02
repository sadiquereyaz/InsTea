import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.data.repo.CombinedUserRepository
import `in`.instea.instea.data.repo.NetworkUserRepository
import `in`.instea.instea.data.repo.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull

interface ChatRepository {
    fun getAllMesaages(senderRoom: String, receiverRoom: String): Flow<List<Message>>
    suspend fun insertMessage(message: Message, senderRoom: String, receiverRoom: String)
    suspend fun updateChatPartners(senderId: String, receiverId: String)
}

class NetwrokChatRepository(
    val userRepository: NetworkUserRepository,
) : ChatRepository {
    val db = Firebase.database.reference

    override fun getAllMesaages(senderRoom: String, receiverRoom: String): Flow<List<Message>> =
        callbackFlow {

            val messageLister = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = mutableListOf<Message>()
                    for (message in snapshot.children) {
                        val message = message.getValue(Message::class.java)
                        if (message != null) {
                            messageList.add(message)
                        }
                    }
                    trySend(messageList).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            db.child("chat").child(senderRoom).addValueEventListener(messageLister)
//        db.child("chat").child(receiverRoom).addValueEventListener(messageLister)

            awaitClose { db.removeEventListener(messageLister) }
        }

    override suspend fun insertMessage(message: Message, senderRoom: String, receiverRoom: String) {
        db.child("chat").child(senderRoom).push()
            .setValue(message).addOnSuccessListener {
                db.child("chat").child(receiverRoom).push()
                    .setValue(message)
            }
    }

    override suspend fun updateChatPartners(senderId: String, receiverId: String) {
        val senderUser = userRepository.getUserById(senderId).firstOrNull()
        val receiverUser = userRepository.getUserById(receiverId).firstOrNull()

        if (senderUser != null && !senderUser.chatPartners.contains(receiverId)) {
            val updatedSender = senderUser.copy(chatPartners = senderUser.chatPartners + receiverId)
            userRepository.updateUser(updatedSender)
        }

        if (receiverUser != null && !receiverUser.chatPartners.contains(senderId)) {
            val updatedReceiver = receiverUser.copy(chatPartners = receiverUser.chatPartners + senderId)
            userRepository.updateUser(updatedReceiver)
        }
    }
}