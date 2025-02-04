package `in`.instea.instea.utility

import androidx.room.TypeConverter
import `in`.instea.instea.domain.datamodel.AttendanceType

class AttendanceTypeConverter {
    @TypeConverter
    fun fromAttendanceType(value: AttendanceType): Int {
        return value.ordinal // Store the ordinal (position) of the enum value
    }

    @TypeConverter
    fun toAttendanceType(value: Int): AttendanceType {
        return AttendanceType.values()[value] // Convert the ordinal back to the enum value
    }
}
