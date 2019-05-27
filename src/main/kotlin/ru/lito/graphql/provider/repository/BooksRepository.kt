package ru.lito.graphql.provider.repository

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.lito.graphql.provider.model.Book

@Repository
interface BooksRepository : CrudRepository<Book, String> {
    fun countByAuthor(authorId: String): Int
    fun findAllByAuthor(authorId: String): List<Book>
    @Query("select b from Book b order by popularity desc")
    fun findTop100OrderByPopularity(pageable: Pageable): List<Book>

}