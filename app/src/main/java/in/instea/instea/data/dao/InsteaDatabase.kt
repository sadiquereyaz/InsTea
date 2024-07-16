package `in`.instea.instea.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import `in`.instea.instea.data.datamodel.RoomPostModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel
import `in`.instea.instea.data.datamodel.UserModel

@Database(
    entities = [RoomPostModel::class, UserModel::class, ScheduleModel::class, TaskAttendanceModel::class],
    version = 16,
    exportSchema = false
)
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
