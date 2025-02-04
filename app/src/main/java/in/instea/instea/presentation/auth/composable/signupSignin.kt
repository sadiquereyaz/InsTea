package `in`.instea.instea.presentation.auth.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.presentation.navigation.InsteaScreens

@Composable
fun signUp(modifier: Modifier = Modifier,navController: NavController) {

    val email = remember {
        mutableStateOf("faraday@gmail.com")
    }
    val password = remember {
        mutableStateOf("12345678")
    }

    val auth = Firebase.auth
    auth.signInWithEmailAndPassword(email.value, password.value)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information

                navController.navigate(InsteaScreens.Feed.name)
            } else {
                // If sign in fails, display a message to the user.

            }
        }
}

@Composable
fun signin(modifier: Modifier = Modifier,navController: NavController) {

}