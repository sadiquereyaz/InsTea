import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import `in`.instea.instea.composable.Loader
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.NoticeViewModel

@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier, navController: NavHostController,
    viewModel: NoticeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var notices by remember { mutableStateOf(uiState.scrollingNoticeList) }
    val tabs = mutableListOf("Urgent", "Admission", "Admission 2", "Hostels","Examinations", "Academics" )
    val mContext = LocalContext.current

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
//            containerColor = Color.Red,
//            contentColor = Color.Blue,
            edgePadding = 0.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier,
                    text = {
                        Text(
                            text = title,
//                            textAlign = TextAlign.Center,
                            color = if (selectedTabIndex == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        when (index) {
                            0 -> {
//                                viewModel.getNotice()
                                notices = uiState.scrollingNoticeList
//                                Log.d("NOTICE_ SCREEN", "Notice: $notices")
                            }

                            1 -> {
//                                viewModel.fetchAdmissionNotices()
                                notices = uiState.admissionNoticeList
                            }

                            2 -> {
//                                viewModel.fetchNewWebsiteNotices()
                                notices = uiState.newWebsiteNoticeList
                            }
                        }
                    }
                )
            }
        }

        if (!uiState.isLoading) {
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(notices) { notice ->
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(notice.second)
                                )
                                startActivity(mContext, intent, null)
                            },
                        text = notice.first
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        } else {
            Loader(modifier = Modifier.fillMaxSize())
        }
    }
}
