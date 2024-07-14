package `in`.instea.instea.data.dao

import androidx.room.Dao
import androidx.room.Query
import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
@Query("SELECT * FROM class")
fun getAllClasses(): Flow<List<ScheduleModel>>
}

//INSERT INTO class (subject, task, attendance) VALUES ('Mathematics', 'Complete Chapter 1 Homework', 'Present'),('Science', 'Prepare for Lab Experiment', 'Absent'), ('History', 'Read Chapter 3', 'Present'),('Geography', 'Complete Map Assignment', 'Cancelled'),('English', 'Submit Essay', 'Present');
