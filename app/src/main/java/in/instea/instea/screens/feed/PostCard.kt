package `in`.instea.instea.screens.feed


import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.FeedViewModel
import `in`.instea.instea.navigation.InsteaScreens
import kotlinx.coroutines.launch

@Composable
fun PostCard(
    post: PostData,
    feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
    userList: List<User>,
    index: Int,
    isVisible: Boolean,
    onClose: () -> Unit,
    onSwiped: () -> Unit,

    ) {
    var isExpanded by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) } // State for showing/hiding CommentCard
    var expandDropdown by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val moreList = listOf("Report", "Delete", "Edit")
    var user: User = User()
    val mContext = LocalContext.current
    val profileImageRes = post.profileImage ?: R.drawable.ic_launcher_foreground

//    val mCustomLinkifyText = remember { TextView(mContext) }


//    Log.d("PostCard", "userlist: $userList")
    for (u in userList) {
        if (u.userId == post.postedByUser) {
            user = u
            break
        }
    }
    var userName = user.username
    Box {
        Box(
            modifier = Modifier
                .pointerInput(Unit) { detectTapGestures { if (isVisible) onClose() } }
                .padding(start = 3.dp, end = 3.dp, bottom = 0.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(InsteaScreens.OtherProfile.name + "/${if (user.userId != null) user.userId else " "}")
//                        navController.navigate(InsteaScreens.Inbox.name+"/${post.postedByUser}")
                        }
                ) {
//                    (if (post.profileImage != null) post.profileImage
//                    else R.drawable.ic_launcher_foreground)?.let {
                        Image(
                            painter = painterResource(id = profileImageRes),
                            modifier = Modifier
                                .padding(3.dp)
                                .size(50.dp)
                                .border(
                                    width = 1.dp, brush = Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primaryContainer
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clip(CircleShape),
                            contentDescription = "Profile"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {

                            if (post.isAnonymous)
                                userName = "UnderCover"
                            Text(
                                text = if (userName != null) {
                                    userName!!
                                } else "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,

                                )

                            Text(
                                text = post.timestamp.format(),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Thin,
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f)) // This pushes the Box to the end

                        Box(
                            modifier = Modifier.padding(end = 8.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(onClick = { expandDropdown = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More")
                            }

                            DropdownMenu(
                                modifier = Modifier.height(100.dp),
                                expanded = expandDropdown,
                                onDismissRequest = { expandDropdown = false }) {
                                moreList.forEach { type ->
                                    if (
                                        feedViewModel.currentuser == post.postedByUser
                                        && type != "Report"
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = {
                                                if (type == "Delete") {
                                                    showDialog = true
                                                }
                                                if (type == "Edit") {
                                                    navController.navigate(InsteaScreens.EditPost.name + "/${post.postid}")
                                                }
                                                expandDropdown = false // Close the dropdown menu
                                            }
                                        )

                                    } else if (
                                        feedViewModel.currentuser != post.postedByUser &&
                                        type != "Edit" && type != "Delete"
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = {
                                                expandDropdown = false // Close the dropdown menu
                                                if (!post.reports.reportByUser.contains(
                                                        feedViewModel.currentuser
                                                    )
                                                ) {
                                                    post.reports.hasReport =
                                                        post.reports.hasReport + 1
                                                    post.reports.reportByUser.add(feedViewModel.currentuser!!)
                                                    feedViewModel.updateVotes(post)
                                                }

                                            }
                                        )
                                        if (post.reports.hasReport > 2) {
                                            Toast.makeText(
                                                LocalContext.current,
                                                "We will check it",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }
                                }
                            }
//                        }
                    }
                }

                DeletePostAlertDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onConfirm = {

                        feedViewModel.DeletePost(post)
                        showDialog = false
                    })
                // Post Description
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(8.dp)
                ) {

                    val displayText = if (isExpanded) post.postDescription!!
                    else post.postDescription?.take(100)
                    val annotatedString = buildAnnotatedString {
                        append(displayText)

                        // Find all URLs in thetext
                        val matcher = Patterns.WEB_URL.matcher(displayText)
                        while (matcher.find()) {
                            var url = matcher.group()
                            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                                url = "https://$url"
                            }
                            addStringAnnotation(
                                tag = "URL",
                                annotation = url,
                                start = matcher.start(),
                                end = matcher.end()
                            )
                            addStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                start = matcher.start(),
                                end = matcher.end()
                            )
                        }
                    }
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let { annotation ->
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                    startActivity(mContext, intent, null)
                                }
                        },
                        style = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer)
                    )
//                    AndroidView(factory = { mCustomLinkifyText }) { textView ->
//                        textView.text = displayText
//                        textView.textSize = 20F
//
//                        LinkifyCompat.addLinks(textView, Linkify.ALL)
//                        textView.movementMethod = LinkMovementMethod.getInstance()
//                    }


                    if (post.postDescription?.length!! > 100) {
                        TextButton(onClick = { isExpanded = !isExpanded }) {
                            Text(
                                text = if (isExpanded) "Show Less" else "Read More",
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (post.edited) {
                        Text(
                            text = "Edited",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(2.dp)
                                .align(Alignment.Start)
                        )
                    }

                }

                // Post Image
                if (post.postImage != null) {
                    Image(
                        painter = painterResource(id = post.postImage!!),
                        contentDescription = "Post Image"
                    )
                }

                // Comment Button and Up/Down Vote Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    UpAndDownVoteButtons(
                        post,
                        isVisible = isVisible,
                        onSwiped = onSwiped,
                        navController = navController
                    )

                }

            }
        }

    }
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp, brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
    )
//    if (showComments) {
//        CommentList(post, feedViewModel)
//    }
}


//@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun UpAndDownVoteButtons(
    post: PostData,
    isVisible: Boolean,
    navController: NavController,
    onSwiped: () -> Unit,
) {
    val isUpVoted = rememberSaveable { mutableStateOf(false) }
    val isDownVoted = rememberSaveable { mutableStateOf(false) }
    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val coroutineScope = rememberCoroutineScope()
//    val swipeableState = rememberSwipeableState(initialValue = 0)
    val swiped = remember { mutableStateOf(false) }
    // Obtain the current density to convert dp to px

    var isOpen by remember { mutableStateOf(false) }

    var userDislikeCurrentPost by rememberSaveable {
        mutableStateOf(post.userDislikedCurrentPost.contains(feedViewModel.currentuser))
    }
    var userlikeCurrentPost by rememberSaveable {
        mutableStateOf(post.userLikedCurrentPost.contains(feedViewModel.currentuser))
    }

    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .padding(8.dp)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = if (!isOpen) Icons.Filled.Comment else Icons.Default.ArrowForward,
                contentDescription = "",
                modifier = Modifier.clickable { isOpen = !isOpen },
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (isOpen) {

            CommentList(post = post, feedViewModel = feedViewModel, navController = navController)
        }
    }


//    Box(contentAlignment = Alignment.BottomStart){
//        SwipeableActionsBox(
//            startActions = listOf(
//                SwipeAction(
//                    onSwipe = onSwiped,
//                    icon =  {
//                        Icon(painter = painterResource(id = R.drawable.chatbubble), contentDescription = null ,)
//                    },
//                    background = MaterialTheme.colorScheme.onBackground
//                )
//            ),
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .padding(8.dp)
//        ) {
//           if(!isVisible) Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
//        else Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null,
//            modifier = Modifier.clickable {  })
//
//        }
//        androidx.compose.animation.AnimatedVisibility(
//            visible = isVisible,
//            enter = slideInHorizontally(
//                initialOffsetX = { -it }, // Slide in from the left
//
//            ),
//            exit = slideOutHorizontally(
//                targetOffsetX = { -it }, // Slide out to the right
//
//            ),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            CommentList(post = post, feedViewModel = feedViewModel, navController = navController)
//        }
//
//    }

    Box(contentAlignment = Alignment.BottomStart) {
        Button(
            onClick = {
                feedViewModel.inserLocal(post)
                post.saved = !post.saved
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified
            ),
            modifier = Modifier.padding(0.dp)
        ) {
            Icon(
                imageVector = if (post.saved) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
    }
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(3.dp, end = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)// Custom spacing between elements
        ) {
//
//            Button(
//
//
//                onClick = {
//                    onCommentClick(!showComments) // Toggle comment visibility
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Transparent,
//                    contentColor = Color.Unspecified
//
//                ),
//                modifier = Modifier
//                    .indication(indication = rememberRipple(
//                        bounded = true,
//                        color = Color.LightGray
//                    ), interactionSource = remember {
//                        MutableInteractionSource()
//                    })
//                    .align(Alignment.CenterVertically)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.chatbubble),
//                    contentDescription = "comment",
//                    modifier = Modifier.size(20.dp),
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            }

            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(1.dp),
                border = BorderStroke(
                    1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,


                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
                    ) {

                        Icon(
                            painter = painterResource(id = if (userDislikeCurrentPost) R.drawable.arrowdownfilled else R.drawable.arrowdownoutline),
                            contentDescription = "Downvote",
                            modifier = Modifier


                                .height(16.dp)
                                .clickable {
                                    if (post.postedByUser != feedViewModel.currentuser) {
                                        isDownVoted.value = !isDownVoted.value
                                        if (isDownVoted.value) {
                                            isUpVoted.value = false
                                        }

                                        coroutineScope.launch {
                                            if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                                post.userDislikedCurrentPost.remove(feedViewModel.currentuser)
                                            } else if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                                post.userLikedCurrentPost.remove(feedViewModel.currentuser)
                                                post.userDislikedCurrentPost.add(feedViewModel.currentuser)
                                            } else {
                                                post.userDislikedCurrentPost.add(feedViewModel.currentuser)
                                            }

                                            feedViewModel.updateVotes(post)
                                            isDownVoted.value = !isDownVoted.value
                                            // Update the local state to reflect changes
                                            userDislikeCurrentPost =
                                                post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
                                            userlikeCurrentPost =
                                                post.userLikedCurrentPost.contains(feedViewModel.currentuser)
                                        }
                                    }
                                },
                            tint = (if (userDislikeCurrentPost) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)

                        )
                        Text(
                            text = (post.userDislikedCurrentPost.size - 1).toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(0.dp)
                        )
                    }


                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(16.dp)
                            .border(
                                border = BorderStroke(
                                    1.dp,
                                    brush = Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primaryContainer
                                        )
                                    )
                                )
                            )
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
                    ) {

                        Text(
                            text = (post.userLikedCurrentPost.size - 1).toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(0.dp)
                        )
                        Icon(
                            painter = painterResource(id = if (userlikeCurrentPost) R.drawable.uparrowfilled else R.drawable.arrowupoutlined),
                            contentDescription = "Upvote",
                            modifier = Modifier
                                .height(16.dp)
                                .clickable {
                                    if (feedViewModel.currentuser != post.postedByUser) {
                                        isUpVoted.value = !isUpVoted.value

                                        if (isUpVoted.value) {
                                            isDownVoted.value = false
                                        }

                                        coroutineScope.launch {
                                            if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                                post.userLikedCurrentPost.remove(feedViewModel.currentuser)
                                            } else if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                                post.userDislikedCurrentPost.remove(feedViewModel.currentuser)
                                                post.userLikedCurrentPost.add(feedViewModel.currentuser)
                                            } else {
                                                post.userLikedCurrentPost.add(feedViewModel.currentuser)
                                            }

                                            feedViewModel.updateVotes(post)

                                            // Update the local state to reflect changes
                                            isUpVoted.value = !isUpVoted.value
                                            userDislikeCurrentPost =
                                                post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
                                            userlikeCurrentPost =
                                                post.userLikedCurrentPost.contains(feedViewModel.currentuser)
                                        }
                                    }
                                },
                            tint = (if (userlikeCurrentPost) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun DeletePostAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Delete Post") },
            text = { Text("Are you sure you want to delete this post?") },
            confirmButton = {
                Button(
                    onClick = onConfirm, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        "Confirm", color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text("Dismiss", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}
