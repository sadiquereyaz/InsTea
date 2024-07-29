import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface ChatRepository {
    fun getAllMesaages(senderRoom: String,receiverRoom: String): Flow<List<Message>>
    suspend fun insertMessage(message: Message, senderRoom: String, receiverRoom: String)
}

class NetwrokChatRepository : ChatRepository {
    val db = Firebase.database.reference

    override fun getAllMesaages(senderRoom: String,receiverRoom: String): Flow<List<Message>> = callbackFlow {

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
}