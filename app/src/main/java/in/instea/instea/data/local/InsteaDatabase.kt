package `in`.instea.instea.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import `in`.instea.instea.data.local.dao.NoticeDao
import `in`.instea.instea.data.local.dao.PostDao
import `in`.instea.instea.data.local.dao.ScheduleDao
import `in`.instea.instea.data.local.entity.NoticeModal
import `in`.instea.instea.data.local.entity.ScheduleModel
import `in`.instea.instea.data.local.entity.SubjectModel
import `in`.instea.instea.data.local.entity.TaskAttendanceModel
import `in`.instea.instea.domain.datamodel.PostData
import `in`.instea.instea.utility.AttendanceTypeConverter
import `in`.instea.instea.utility.TimeConverters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(
    entities = [PostData::class, ScheduleModel::class, NoticeModal::class, TaskAttendanceModel::class, SubjectModel::class],
    version =4, exportSchema = false
)
@TypeConverters(TimeConverters::class, AttendanceTypeConverter::class)
abstract class InsteaDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun classDao(): ScheduleDao
    abstract fun noticeDao(): NoticeDao

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
        suspend fun clearDatabase(context: Context) {
            withContext(Dispatchers.IO) {
                getDatabase(context).clearAllTables()
            }
        }
    }
}
