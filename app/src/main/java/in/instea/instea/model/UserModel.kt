package `in`.instea.instea.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey
    val userId: Int = 12345,
    val username: String = "sadique",
    val about: String = "Instea is really great app"
)