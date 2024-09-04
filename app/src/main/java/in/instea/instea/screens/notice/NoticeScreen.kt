import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import `in`.instea.instea.composable.Loader
import `in`.instea.instea.data.viewmodel.AppViewModelProvider

@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NoticeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val mContext = LocalContext.current

    LaunchedEffect(selectedTabIndex) {
        viewModel.fetchNoticesForTab(selectedTabIndex)
    }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp
        ) {
            viewModel.tabConfigs.forEach {
                val index = it.key
                Tab(
                    text = {
                        Text(
                            text = it.value.tabName,
                            color = if (selectedTabIndex == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loader(
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else if (selectedTabIndex==8){
            // Use AndroidView to embed the WebView in Compose
            val webView = remember { WebView(mContext) }

            // Configure the WebView
            DisposableEffect(webView) {
                webView.webViewClient = WebViewClient()
                webView.settings.javaScriptEnabled = true
                webView.loadUrl(viewModel.tabConfigs[selectedTabIndex]?.url ?: "")

                onDispose {
                    webView.stopLoading()
                }
            }

            AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize())
        }else {
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(uiState.notices) { notice ->
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.url))
                                startActivity(mContext, intent, null)
                            },
                        text = notice.title
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        }
    }
}