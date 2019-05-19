package ru.lito.graphql.provider.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.lito.graphql.provider.model.Book

@Repository
interface BooksRepository : CrudRepository<Book, String> {
    fun countByAuthorId(authorId: String): Int
}