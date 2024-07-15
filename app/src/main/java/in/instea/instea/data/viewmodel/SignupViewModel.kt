package `in`.instea.instea.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import `in`.instea.instea.data.datamodel.SignupUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

class signupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    var emailState by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set
    var usernameState by mutableStateOf("")
        private set
    var passwordState by mutableStateOf("")
        private set
    var departmentState by mutableStateOf("")
        private set
    var universityState by mutableStateOf("")
        private set
    var semesterState by mutableStateOf("")
        private set

    fun updateName(nameInput: String) {
        name = nameInput
        val valid=nameValid(nameInput)
        _uiState.update { it.copy(name = nameInput,
            nameError = !valid) }
    }

    fun updateEmail(email: String) {
        emailState = email
        val valid =mailValid(email)
        _uiState.update { it.copy(emailid = email,
            emailError = !valid) }
    }

    fun updateUsername(username: String) {
        usernameState = username
        val valid=usernameValid(username)
        _uiState.update { it.copy(username = username,
            usernameError = !valid) }
    }

    fun updateDepartment(department: String) {
        departmentState = department
        _uiState.update { it.copy(departmenr = department) }
    }

    fun updateUniversity(university: String) {
        universityState = university
        _uiState.update { it.copy(university = university) }
    }

    fun updateSemester(semester: String) {
        semesterState = semester
        _uiState.update { it.copy(semester = semester) }
    }

    fun updatePassword(password: String) {
        passwordState = password
        val valid=passValid(password.toString())
        _uiState.update { it.copy(password = password,
            passError = !valid) }
    }

    fun nameValid(name:String):Boolean{
        val text = name
        val inputCond =
            Pattern.compile("^[^.]{1,20}$")
        return text.isNotEmpty() && text.matches(inputCond.toRegex())
    }

    fun mailValid (mail: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )
        return mail.isNotEmpty() && mail.matches(emailPattern.toRegex())

    }
    fun usernameValid(username: String):Boolean{
        val inputCond =
            Pattern.compile("^[^\\s.]{1,9}$")

        return username.isNotEmpty() && username.matches(inputCond.toRegex())
    }
    fun passValid(pass:String):Boolean{

        val passwordRegex =
                Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")

        return pass.isNotEmpty() && pass.matches((passwordRegex).toRegex())

    }


}

