package `in`.instea.instea.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.dao.PostDao
import `in`.instea.instea.data.dao.UserDao
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.UserModel

@Database(entities = [PostData::class, UserModel::class, ScheduleModel::class], version = 7, exportSchema = false)
abstract class InsteaDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun classDao(): ScheduleDao

    companion object {
        @Volatile
        private var Instance: InsteaDatabase? = null

        fun getDatabase(context: Context): InsteaDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InsteaDatabase::class.java, "instea_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
