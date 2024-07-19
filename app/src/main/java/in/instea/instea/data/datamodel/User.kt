package `in`.instea.instea.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "user")
@TypeConverters(StringListConverter::class)
data class User(
    @PrimaryKey
   val userId:String = "",
    val email: String?=null,
    val username: String?=null,
    val password: String?=null,
    val university: String?=null,
    val dept: String?=null,
    val userPosts:MutableList<String?> = mutableListOf(),
    val sem: String?=null,
    val instaId: String?=null,
    val linkedinId: String?=null,
    val hostel: String?=null,
    val roomNo: String?=null
)