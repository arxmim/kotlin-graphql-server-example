package ru.lito.graphql.provider

import graphql.schema.DataFetcher
import org.springframework.beans.factory.annotation.Autowired
import ru.lito.graphql.provider.model.Author
import ru.lito.graphql.provider.model.Book
import ru.lito.graphql.provider.repository.BooksRepository


class GraphQLDataFetchers {

    @Autowired
    private lateinit var bookRepository: BooksRepository

    fun getBookByIdDataFetcher(): DataFetcher<List<Book>> {
        return DataFetcher { environment ->
            val bookId: String? = environment.getArgument("id")
            if (bookId != null) {
                listOf(bookRepository.findById(bookId).orElse(null))
            } else {
                bookRepository.findAll().toList()
            }
        }
    }

    fun getAuthorBookCountFetcher(): DataFetcher<Int> {
        return DataFetcher { environment ->
            val author: Author = environment.getSource()
            bookRepository.countByAuthorId(author.id)
        }
    }
}
