
data class Message(
    val message:String,
    val timeStamp: String = TimeOfMessage().format(),
    val senderId: String
){
    constructor(): this("",TimeOfMessage() .format(),"")
}

