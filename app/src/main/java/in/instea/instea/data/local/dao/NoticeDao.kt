package `in`.instea.instea.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import `in`.instea.instea.data.local.entity.NoticeModal

@Dao
interface NoticeDao {
    @Query(
        """
            INSERT INTO NoticeModal (title, url, type, timestamp)
            SELECT :title, :url, :type, :timestamp
            WHERE NOT EXISTS ( SELECT 1 FROM NoticeModal WHERE title = :title AND url = :url AND type = :type ) 
            LIMIT 1;
        """
    )
    suspend fun insertNotice(title: String, url: String, type: String, timestamp: Long)

    @Query("SELECT * FROM NoticeModal WHERE type =:type")
    fun getNotices(type: String): List<NoticeModal>

    @Query("DELETE FROM NoticeModal")
    suspend fun deleteAllNotices()

}