package `in`.instea.instea.data

data class ValidationResult (
    val isValid:Boolean=false
)
object Validator {
    fun validateName(name:String):ValidationResult{
        return ValidationResult(
            (name.isNotEmpty())
        )
    }
    fun validateEmail(email: String): ValidationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        return ValidationResult(
            email.isNotEmpty() && email.matches(emailRegex)
        )
    }
    fun validateUsername(username:String):ValidationResult{
        return ValidationResult(
            (username.isNotEmpty())
        )
    }
    fun validateUniversity(univ:String):ValidationResult{
        return ValidationResult(
            (univ.isNotEmpty())
        )
    }
    fun validateDept(dept:String):ValidationResult{
        return ValidationResult(
            (dept.isNotEmpty())
        )
    }
    fun validateSemester(sem:String):ValidationResult{
        return ValidationResult(
            (sem.isNotEmpty())
        )
    }
}

