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
//            username.isEmpty() -> "Username cannot be empty"
            username.length > 15 -> "Maximum 15 characters are allowed"
            username.length < 3 -> "Minimum 3 characters are allowed"
            username.any { it.isWhitespace() } -> "Username should not contain spaces"
            username.any { it.isUpperCase()} -> "Only lowercase letters are allowed"
            !username.matches("^[a-zA-Z0-9.]+\$".toRegex())-> "Special characters except dot(.) are not allowed"
            username.all { it == username.first() } -> "Repeated characters found"
            else -> null
        }
    }
    fun validateInstagram(ig: String): String? {
        return when {
//            username.isEmpty() -> "Username cannot be empty"
            ig.length > 20 -> "Maximum 15 characters are allowed"
            ig.any { it.isWhitespace() } -> "Should not contain spaces"
            ig.any { it.isUpperCase()} -> "Only lowercase letters are allowed"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return when {
            email.any { it.isUpperCase() } -> "Only lowercase characters allowed"
            email.length > 25 -> "Character length exceeded"
            email.any { it.isWhitespace() } -> "Should not contain spaces"
//            !(email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[a-zA-Z]{2,4}$".toRegex()))  -> "Invalid email format"
            else -> null
        }
    }

    fun validateWhatsapp(no: String ):String?  {
        return when {
            no.length > 10 -> "Only 10 integers are allowed"
            no.any { !it.isDigit() } -> "Invalid"

            else -> null
        }
    }
}