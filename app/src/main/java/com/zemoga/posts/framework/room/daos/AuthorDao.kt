package com.zemoga.posts.framework.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemoga.posts.framework.room.entities.Author

@Dao
interface AuthorDao {

    @Query("SELECT * FROM author WHERE id == :authorId")
    suspend fun getAuthor(authorId: Int): Author

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(authors: List<Author>)
}