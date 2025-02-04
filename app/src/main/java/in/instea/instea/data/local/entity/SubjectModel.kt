package `in`.instea.instea.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_table")
data class SubjectModel(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0, // Foreign key referencing ScheduleModel
    val subject: String
)