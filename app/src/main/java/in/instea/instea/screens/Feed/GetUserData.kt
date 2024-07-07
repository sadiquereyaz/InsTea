package `in`.instea.instea.screens.Feed

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.database
import `in`.instea.instea.data.User
import kotlinx.coroutines.tasks.await


@Composable
fun GetUserData():MutableList<User> {
    val mAuth = Firebase.auth
    val db = Firebase.database.reference
    val coroutineScope = rememberCoroutineScope()
    var userData by remember { mutableStateOf<DataSnapshot?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }

    // Fetch data when the composable enters the composition
    LaunchedEffect(Unit) {
        try {
            userData = db.child("user").child(mAuth.currentUser!!.uid).get().await()
            Log.d("userss", "GetUserData: ${mAuth.currentUser!!.uid}")

            showSnackbar = true // Show Snackbar when data is fetched
        } catch (e: Exception) {
            // Handle errors, e.g., log the error or show anerror message
            println("Error fetching user data: ${e.message}")
        }
    }
    val userlist = mutableListOf(User())
    try {

        for (i in userData!!.children){
            val user = i.getValue(User::class.java)
            userlist.add(user!!)

        }
        Log.d("userData", "GetUserData: ${userlist}")
    } catch (e: Exception) {

        Log.e("TAG", "GetUserData: ${e}", )
    }

    return userlist
    // Display Snackbar if data is available
//    if (showSnackbar && userData != null) {
//        Snackbar(
//
//            action = {
//                TextButton(onClick = { showSnackbar = false }) {
//                    Text(mAuth.currentUser!!.uid)
//                }
//            },
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//        ) {
//            Text(text = userData.toString()) // Display user data in Snackbar
//        }
//    }
}