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

package `in`.instea.instea.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import `in`.instea.instea.data.datamodel.RoomPostModel
import `in`.instea.instea.data.datamodel.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(user: UserModel)

    @Delete
    suspend fun deletePost(post: RoomPostModel)

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getUserById(userId: Int): Flow<UserModel>
    //insert into user values (12345, 'sadiquereyaz', 'If you omit the WHERE clause, all rows in the table will be updated with the new value in the SET clause. This should be used with caution to avoid unintended data modifications.');
}
