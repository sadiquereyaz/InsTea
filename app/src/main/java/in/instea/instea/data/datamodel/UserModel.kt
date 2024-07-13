package `in`.instea.instea.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey
    val userId: Int = 12345,
    val username: String="username" ,
    val about: String="about"
)