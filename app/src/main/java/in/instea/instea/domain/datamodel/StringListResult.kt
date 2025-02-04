package `in`.instea.instea.domain.datamodel

data class StringListResult(
    val stringList: List<String> = emptyList(),
    val errorMessage: String? = null
)