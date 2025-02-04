package `in`.instea.instea.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import `in`.instea.instea.domain.datamodel.PostData
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Upsert
    suspend fun insertPost(post: PostData)

    @Update
    suspend fun updatePost(post: PostData)

    @Delete
    suspend fun deletePost(post: PostData)

    @Query("SELECT * from posts WHERE postId = :postId")
    fun getPostByPostIdPostById(postId: Int): Flow<PostData>

    @Query("SELECT * from posts ORDER BY postedByUser ASC")
    fun getAllSavedPosts(): Flow<List<PostData>>

}
