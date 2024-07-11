package `in`.instea.instea.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import `in`.instea.instea.model.UserModel

@Database(entities = [PostData::class, UserModel::class], version = 1, exportSchema = false)
abstract class InsteaDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var Instance: InsteaDatabase? = null

        fun getDatabase(context: Context): InsteaDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InsteaDatabase::class.java, "instea_database")
                    .build().also { Instance = it }
            }
        }
    }
}
