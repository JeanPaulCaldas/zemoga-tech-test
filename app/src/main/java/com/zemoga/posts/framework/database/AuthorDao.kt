package com.zemoga.posts.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthorDao {

    @Query("SELECT * FROM Author WHERE id == :authorId")
    suspend fun getAuthor(authorId: Int): Author

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(authors: List<Author>)
}