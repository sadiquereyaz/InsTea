package `in`.instea.instea.data.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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


        val universitiesRef = firebaseInstance.getReference("academic")
        var isResumed = false // Flag to track if the continuation has been resumed

        val universityList = suspendCancellableCoroutine<List<String>> { continuation ->
            val universityListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isResumed) {
                        val list = mutableListOf<String>()
                        for (universitySnapshot in snapshot.children) {
                            val universityName =
                                universitySnapshot.key
                            universityName?.let { list.add(it) }
                        }
                        continuation.resume(list)
                        isResumed = true // Set the flag to true after resuming the continuation
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isResumed) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException())
                        continuation.resume(emptyList()) // Resume with an empty list on error
                        isResumed = true // Set the flag to true after resuming the continuation
                    }
                }
            }
            universitiesRef.addValueEventListener(universityListener)
            continuation.invokeOnCancellation {
                universitiesRef.removeEventListener(universityListener)
            }
        }
        emit(universityList)
    }

    override fun getAllDepartment(university: String): Flow<List<String>> = flow {
        val depart = listOf("AMU CSE", "EEE", "ECE", "MECH")
        /*val deptRef =
            firebaseInstance.getReference("academic").child(university).child("classDetails")
        var isResumed = false // Flag to track if the continuation has been resumed
        val departList = suspendCancellableCoroutine<List<String>> { continuation ->
            val departmentListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isResumed) {
                        val list = mutableListOf<String>()
                        for (departmentSnapshot in snapshot.children) {
                            val departmentName =
                                departmentSnapshot.key
                            departmentName?.let { list.add(it) }
                        }
                        continuation.resume(list)
                        isResumed = true // Set the flag to true after resuming the continuation
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isResumed) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException())
                        continuation.resume(emptyList()) // Resume with an empty list on error
                        isResumed = true // Set the flag to true after resuming the continuation
                    }
                }
            }*/

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
            //Add the new academic details to firebase
            val universitiesRef = firebaseInstance.getReference("academic").child(university)
            val details = classDetails(semester, department)
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
        classDetails: classDetails
    )

    class classDetails(
        val semester: String,
        val department: String
    )