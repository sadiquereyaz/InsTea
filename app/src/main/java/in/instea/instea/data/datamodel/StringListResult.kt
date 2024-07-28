package `in`.instea.instea.data.datamodel

data class StringListResult(
    val stringList: List<String> = emptyList(),
    val errorMessage: String? = null
)