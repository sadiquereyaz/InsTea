package `in`.instea.instea.domain.repo

import `in`.instea.instea.domain.datamodel.StringListResult
import kotlinx.coroutines.flow.Flow

interface AcademicRepository {
    fun getAllUniversity(): Flow<StringListResult>
    fun getAllDepartment(university: String): Flow<StringListResult>
//    fun getAllSemester(university: String, department: String): Flow<StringListResult>
//    suspend fun addClassDetail(semester: String, department: String, university: String)
    suspend fun upsertInstitutes(university: String, dept: String): Result<String?>
}
