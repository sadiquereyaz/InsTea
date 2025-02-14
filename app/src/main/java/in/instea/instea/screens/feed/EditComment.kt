package `in`.instea.instea.screens.feed

//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.Orientation
//import androidx.compose.foundation.gestures.ScrollableState
//import androidx.compose.foundation.gestures.scrollable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.ArrowRight
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Send
//import androidx.compose.material.icons.outlined.Create
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.snapshotFlow
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import `in`.instea.instea.R
//import `in`.instea.instea.data.datamodel.Comments
//import `in`.instea.instea.data.datamodel.PostData
//import `in`.instea.instea.data.viewmodel.AppViewModelProvider
//import `in`.instea.instea.data.viewmodel.FeedViewModel
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//@Composable
//fun EditComment(commentId:String) {
//
//    val postTypeOptions = listOf("Public", "Anonymous")
//    var selectedPostType by remember { mutableStateOf(postTypeOptions[0]) } // Initial selection
//    var expanded by remember { mutableStateOf(false) } // State for DropdownMenu
//    val coroutine = rememberCoroutineScope()
//    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
//
//    val post = feedViewModel.posts.collectAsState(initial = emptyList()).value
//    var comment = post[commentId.take(commentId.length-2)].comments[(commentId[commentId.length-1]).toInt()]
//    var textState by remember { mutableStateOf("") }
//    LaunchedEffect(comment) {
//       textState = comment.comment
//    }
//
//    val index = postData.comments.indexOf(comment)
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//
//    ) {
//        Column(verticalArrangement = Arrangement.SpaceBetween) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                    contentDescription = "profile",
//                    modifier = Modifier.size(30.dp)
//                )
//                Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "arrow right")
//
//                Box(
//                    modifier = Modifier
//                        .padding(start = 8.dp)
//                        .border(
//                            2.dp,
//                            MaterialTheme.colorScheme.onPrimaryContainer,
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .clickable { expanded = true } // Open DropdownMenu on click
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .padding(4.dp)
//                    ) {
//                        if (selectedPostType.equals("Public")) {
//
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = "public Icon"
//                            )
//                            Spacer(
//                                modifier = Modifier
//                                    .height(Icons.Default.Person.defaultHeight)
//                                    .width(2.dp)
//                            )
//                            Text(text = selectedPostType)
//
//                        } else {
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = "public Icon"
//                            )
//                            Spacer(
//                                modifier = Modifier
//                                    .height(Icons.Default.Person.defaultHeight)
//                                    .width(2.dp)
//                            )
//                            Text(text = selectedPostType)
//                        }
//                        Icon(
//                            imageVector = Icons.Default.ArrowDropDown,
//                            contentDescription = "down arrow"
//                        )
//                    }
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false }) {
//                        postTypeOptions.forEach { type ->
//                            DropdownMenuItem(
//                                text = { Text(type) },
//                                onClick = {
//                                    selectedPostType = type
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//
//            }
//
//            OutlinedTextField(
//                value = textState,
//                onValueChange = { textState = it },
//                label = { Text(text = "What do you want to ask") },
//                modifier = Modifier
//                    .scrollable(
//                        state = ScrollableState { 0f },
//                        orientation = Orientation.Vertical
//                    )
//                    .fillMaxWidth()
//                    .padding(3.dp)
//                    .heightIn(min = 50.dp, max = 100.dp),
//
//
//                textStyle = TextStyle(fontSize = 16.sp),
//                maxLines = 5,
//                trailingIcon = {
//                    if (textState.isNotEmpty()) {
//                        Icon(imageVector = Icons.Filled.Send,
//                            contentDescription = "send",
//                            modifier = Modifier.clickable {
//                                if(textState != post.comments[index].comment){
////                                    todo
//                                }
//
//                                textState.replace(" +".toRegex()," ")
//                                coroutine.launch {
//                                    post.comments[index].comment = textState
//
//                                    feedViewModel.updateComment(
//                                        post
//                                    )
//                                    textState = ""
//
//                                }
//
//
//
//                            }
//                        )
//                    }
//
//                }
//            )
//
//
//        }
//    }
//}