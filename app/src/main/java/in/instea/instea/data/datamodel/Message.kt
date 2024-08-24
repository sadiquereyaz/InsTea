package `in`.instea.instea.data.datamodel


data class Message(
    val message: String,
    val timeStamp: DateAndHour,
    val senderId: String,
) {
    constructor(message: String, senderId: String) : this(message, DateAndHour(), senderId)
}

