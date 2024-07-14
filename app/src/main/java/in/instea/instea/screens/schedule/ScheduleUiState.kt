package `in`.instea.instea.screens.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import java.time.Month
import java.util.Calendar


data class ScheduleUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val classList: List<ScheduleModel> = listOf(),
    val dayDateList: List<DayDateModel> = listOf(),
    var selectedDateIndex: Int = 15,
//    val currentDate: String="04",
//    val currentTime: String = "10:00",
    val month: String,
)
