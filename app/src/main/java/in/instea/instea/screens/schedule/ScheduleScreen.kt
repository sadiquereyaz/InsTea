package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.ScheduleViewModel
import `in`.instea.instea.composable.AddClassButton
import `in`.instea.instea.composable.Calendar
import `in`.instea.instea.composable.ScheduleList

@Composable
fun ScheduleScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scheduleUiState by viewModel.scheduleUiState.collectAsState()
    val listState = rememberLazyListState()

    //scroll to the initial index
    LaunchedEffect(key1 = true) {
        val initialIndex = 13
        listState.scrollToItem(initialIndex - 1)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Calendar(scheduleUiState, listState)
        //add class
        AddClassButton(navController)

        // schedule list
        ScheduleList(scheduleUiState)
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleLayoutPreview() {
    ScheduleScreen(rememberNavController())
}

