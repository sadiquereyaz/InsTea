package `in`.instea.instea.domain.datamodel

data class Message(
    val message: String="",
    val timeStamp: DateAndHour = DateAndHour(),
    val senderId: String="",
)
{
    constructor(message: String, senderId: String) : this(message, DateAndHour(), senderId)
}

