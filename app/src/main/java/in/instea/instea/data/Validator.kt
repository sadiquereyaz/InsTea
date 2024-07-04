package `in`.instea.instea.data

object Validator {
    fun validateName(name:String):ValidationResult{
        return ValidationResult(
            (name.isNotEmpty())
        )
    }
    fun validateEmail(email:String):ValidationResult{
        return ValidationResult(
            (email.isNotEmpty())
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

data class ValidationResult (
    val status:Boolean=false
)