package `in`.instea.instea.data.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

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
                universityList
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch universities", e)
                emptyList()
            }
        )

    }

    override fun getAllDepartment(university: String): Flow<List<String>> = flow {
        val depart = listOf("AMU CSE", "EEE", "ECE", "MECH")
        val deptRef =
            firebaseInstance.getReference("academic").child(university).child("classDetails")
        val deptList = mutableListOf<String>()
        emit(
            try {
                val snapshot = deptRef.get().await()
                for (deptSnapshot in snapshot.children) {
                    val deptName = deptSnapshot.child("department").getValue(String::class.java)
                    deptName?.let { deptList.add(it) }
                    Log.d(TAG, "getAllDepartment: fetched department successfully $deptName")
                }

                deptList
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch departments", e)
                emptyList<String>()
            }
        )

    }

    override fun getAllSemester(university: String, department: String): Flow<List<String>> = flow {

        val semRef =
            firebaseInstance.getReference("academic").child(university).child("classDetails")
        val semList = mutableListOf<String>()
        emit(
            try {
                val snapshot = semRef.get().await()
                for (deptSnapshot in snapshot.children) {
                    val semName = deptSnapshot.child("semester").getValue(String::class.java)
                    semName?.let { semList.add(it) }
//                    Log.d(TAG, "getAllSemester: fetched department successfully $semName")
                }


                semList
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch departments", e)
                emptyList<String>()
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