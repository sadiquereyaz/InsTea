/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package `in`.instea.instea.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import `in`.instea.instea.model.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPost(post: PostData)

    @Update
    suspend fun updatePost(post: PostData)

    @Delete
    suspend fun deletePost(post: PostData)

    @Query("SELECT * from posts WHERE postId = :postId")
    fun getPostByPostIdPostById(postId: Int): Flow<PostData>

    @Query("SELECT * from posts ORDER BY name ASC")
    fun getAllSavedPosts(): Flow<List<PostData>>

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getUserById(userId: Int): Flow<UserModel>
    //insert into user values (12345, 'sadiquereyaz', 'If you omit the WHERE clause, all rows in the table will be updated with the new value in the SET clause. This should be used with caution to avoid unintended data modifications.');
}
