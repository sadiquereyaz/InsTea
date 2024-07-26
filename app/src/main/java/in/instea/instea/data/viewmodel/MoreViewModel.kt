package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import `in`.instea.instea.data.repo.ScheduleRepository

class MoreViewModel(
 val scheduleRepository: ScheduleRepository
) : ViewModel(){
}