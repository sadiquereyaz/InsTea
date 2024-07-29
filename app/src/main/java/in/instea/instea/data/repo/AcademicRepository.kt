package `in`.instea.instea.data.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.data.datamodel.StringListResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface AcademicRepository {
    fun getAllUniversity(): Flow<StringListResult>
    fun getAllDepartment(university: String): Flow<StringListResult>
    fun getAllSemester(university: String, department: String): Flow<StringListResult>
    suspend fun addClassDetail(semester: String, department: String, university: String)
}

class NetworkAcademicRepository(
    private val firebaseInstance: FirebaseDatabase
) : AcademicRepository {
    override fun getAllUniversity(): Flow<StringListResult> = flow {
        // Get the list of universities from Firebase
        val universityList = mutableListOf<String>()
        val universitiesRef = firebaseInstance.getReference("academic")
        emit(
            try {
                val snapshot = universitiesRef.get().await()
                for (universitySnapshot in snapshot.children) {
                    val universityName = universitySnapshot.key
                    universityName?.let { universityList.add(it) }
                }
//                Log.d(TAG, "getAllUniversity: fetched universities successfully")
                StringListResult(stringList = universityList)
            } catch (e: Exception) {
//                Log.e(TAG, "Failed to fetch universities", e)
                StringListResult(errorMessage = e.localizedMessage)
            }
        )
    }

    override fun getAllDepartment(university: String): Flow<StringListResult> = flow {
        val deptRef =
            firebaseInstance.getReference("academic").child(university).child("classDetails")
        val deptSet = mutableSetOf<String>()
        emit(
            try {
                val snapshot = deptRef.get().await()
                for (deptSnapshot in snapshot.children) {
                    val deptName = deptSnapshot.child("department").getValue(String::class.java)
                    deptName?.let { deptSet.add(it) }
                }
                StringListResult(stringList = deptSet.toList())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch departments", e)
                StringListResult(errorMessage = e.localizedMessage)
            }
        )
    }

    override fun getAllSemester(university: String, department: String): Flow<StringListResult> =
        flow {

            val semRef =
                firebaseInstance.getReference("academic").child(university).child("classDetails")
            val semSet = mutableSetOf<String>()
            emit(
                try {
                    val snapshot = semRef.get().await()
                    for (deptSnapshot in snapshot.children) {
                        val semName = deptSnapshot.child("semester").getValue(String::class.java)
                        semName?.let { semSet.add(it) }
//                    Log.d(TAG, "getAllSemester: fetched department successfully $semName")
                    }
                    StringListResult(stringList = semSet.toList())
                } catch (e: Exception) {
//                    Log.e(TAG, "Failed to fetch departments", e)
                    StringListResult(errorMessage = e.localizedMessage)
                }
            )
        }

    override suspend fun addClassDetail(
        semester: String,
        department: String,
        university: String
    ) {
        //Add the new academic details to firebase
        val universitiesRef = firebaseInstance.getReference("academic").child(university)
        val details = ClassDetails(semester, department)
        val newUniversity = University(university, details)
        universitiesRef.child("classDetails").push().setValue(details)
            .addOnSuccessListener {
                Log.d(TAG, "Class details added successfully")

            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to add class details", exception)

            }


    }
}

class University(
    val name: String,
    classDetails: ClassDetails
)

data class ClassDetails(
    val semester: String,
    val department: String
)