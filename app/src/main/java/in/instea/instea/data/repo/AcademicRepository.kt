package `in`.instea.instea.data.repo

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AcademicRepository {
    fun getAllUniversity(): Flow<List<String>>
    fun getAllDepartment(university: String): Flow<List<String>>
    fun getAllSemester(university: String, department: String): Flow<List<String>>
    suspend fun addClassDetail(semester: String, department: String, university: String)
}

class NetworkAcademicRepository(
    private val firebaseInstance: FirebaseDatabase
) : AcademicRepository {
    override fun getAllUniversity(): Flow<List<String>> = flow {
        val list = listOf("AMU", "JMI")
        emit(list)
    }

    override fun getAllDepartment(university: String): Flow<List<String>> = flow {
        val depart = listOf("AMU CSE", "EEE", "ECE", "MECH")
        emit(depart)
    }

    override fun getAllSemester(university: String, department: String): Flow<List<String>> = flow {

        if (university == "JMI") {
            emit(listOf("JMI SEM"))
        } else {
            emit(listOf("not jmi SEM"))
        }
    }

        override suspend fun addClassDetail(
            semester: String,
            department: String,
            university: String
        ) {
            TODO("Not yet implemented")
        }

    }