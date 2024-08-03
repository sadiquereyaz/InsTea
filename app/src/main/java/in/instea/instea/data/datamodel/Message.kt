import `in`.instea.instea.data.datamodel.DateAndHour

data class Message(
    val message:String,
    val timeStamp: DateAndHour,
    val senderId: String
){
    constructor(): this("",DateAndHour() ,"")
}

