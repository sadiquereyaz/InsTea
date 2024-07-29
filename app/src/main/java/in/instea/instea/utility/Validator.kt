package `in`.instea.instea.utility

data class ValidationResult(
    val errorMessage: String? = null
)

object Validator {
    /* fun validateName(name:String):ValidationResult{
         return ValidationResult(
             (name.isNotEmpty())
         )
     }
     fun validateEmail(email: String): ValidationResult {
         val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

         return ValidationResult(
             email.isNotEmpty() && email.matches(emailRegex)
         )
     }*/
    fun validateUsername(username: String): String? {
        return when {
            username.isEmpty() -> "Username cannot be empty"
            username.length > 15 -> "Maximum 15 characters are allowed"
            username.any { it.isWhitespace() } -> "Username should not contain spaces"
            username.any { !it.isLowerCase() && it != '.' } -> "Only lowercase letters and dots are allowed"
            else -> null
        }
    }
}