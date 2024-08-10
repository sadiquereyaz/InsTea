package `in`.instea.instea.screens.more

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import `in`.`in`.instea.instea.screens.more.composable.classmates
import `in`.instea.instea.composable.Loader
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.MoreViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.navigation.NavigationDestinations
import `in`.instea.instea.screens.more.composable.AccountComp
import `in`.instea.instea.screens.more.composable.AllTask
import `in`.instea.instea.screens.more.composable.AttendanceComp
import `in`.instea.instea.screens.more.composable.Developers
import `in`.instea.instea.screens.more.composable.report
import kotlinx.coroutines.launch

object MoreDestination : NavigationDestinations {
    override val route: String = InsteaScreens.More.name
    override val title: String = InsteaScreens.More.title
    const val INDEX_ARG = "expandedIndex"
    val routeWithArg = "${route}/{$INDEX_ARG}"
}

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MoreViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snackBarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var expandedIndex by remember { mutableStateOf(uiState.expandedIndex) }
    val items =
        listOf("Developers", "Attendance Record", "All Task", "Classmates", "Account", "Report/Feedback")

    if (uiState.moveToAuth)
        navController.navigate(InsteaScreens.Authenticate.name) {
            popUpTo(0) { inclusive = true }
        }

    LaunchedEffect(uiState.showSnackBar) {
        coroutineScope.launch {
            val message = uiState.errorMessage
            if (message != null) {
                snackBarHostState.showSnackbar(message = message)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (!uiState.isLoading) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(items.size) { index ->
                    ExpandableItem(
                        uiState = uiState,
                        viewModel = viewModel,
                        navController = navController,
                        title = items[index],
                        isExpanded = (index == expandedIndex),
                        onExpandChange = {
                            expandedIndex = if (expandedIndex == index) null else index
                        }
                    )
                }
            }
        } else {
            Loader(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ExpandableItem(
    title: String,
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: MoreUiState,
    viewModel: MoreViewModel,
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
//            verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DeveloperMode,
                contentDescription = null,
                modifier = Modifier
//                        .size(15.dp)
            )
            Text(text = title, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onExpandChange() }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.RemoveCircleOutline else Icons.Default.AddCircleOutline,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
        }
        if (isExpanded) {
            // Add the content to display when expanded
            Column(
                modifier = Modifier
//                    .padding(bottom = 16.dp)
                    .heightIn(max = 400.dp, min = 75.dp)
            ) {
                Divider(modifier = Modifier)

                when (title) {
                    "Developers" -> {
                        Developers()
                    }

                    "Attendance Record" -> {
                        AttendanceComp(
                            uiState = uiState,
                            onDateSelected = { selectedDate ->
                                viewModel.onTimestampSelected(selectedDate)
                            }
                        )
                    }

                    "All Task" -> {
                        AllTask(uiState=uiState,
                            onDeleteTask = {task->
                                viewModel.onDeleteTaskClicked(task)
                            })
                    }

                    "Classmates" -> {
                        classmates()
                    }

                    "Account" -> {
                        AccountComp(
                            logout = {
                                viewModel.onSignOutClick()
                            },
                            deleteAccount = {
                                viewModel.onDeleteAccountClick()
                            },
                        )
                    }

                    "Report" -> {
                        report()
                    }
                }
            }
        }
    }
}
//}

@Preview
@Composable
private fun morePrev() {
//    MoreScreen()
}