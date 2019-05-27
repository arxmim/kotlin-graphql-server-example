package ru.lito.graphql.provider

import graphql.schema.DataFetcher
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.lito.graphql.provider.model.Author
import ru.lito.graphql.provider.model.Book
import ru.lito.graphql.provider.repository.AuthorRepository
import ru.lito.graphql.provider.repository.BooksRepository
import java.util.concurrent.CompletableFuture


class GraphQLDataFetchers {

    @Autowired
    private lateinit var bookRepository: BooksRepository
    @Autowired
    private lateinit var authorRepository: AuthorRepository

    fun getBookByIdDataFetcher(): DataFetcher<List<Book>> {
        return DataFetcher { environment ->
            val bookId: String? = environment.getArgument("id")
                println("getBookById - ${bookId}")
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
            println("author book count extracting (real call) - ${author.id}")
            bookRepository.countByAuthor(author.id)
        }
    }

    fun getAuthor(): DataFetcher<CompletableFuture<Author>> {
        return DataFetcher { environment ->
            val book = environment.getSource<Book>()
            println("async author loading - ${book.author}")
            val dataLoader = environment.getDataLoader<String, Author>("batched")
            dataLoader.load(book.author)
        }
    }

    fun getMostPopularBooks(): DataFetcher<List<Book>> {
        return DataFetcher { environment ->
            val count: Int = environment.getArgument("count") ?: 1
            println("findTopOrderByPopularity - ${count}")
            bookRepository.findTop100OrderByPopularity(PageRequest.of(0, count))
        }
    }

    var authorBatchDataLoader: BatchLoader<String, Author> = BatchLoader { keys ->
        CompletableFuture.supplyAsync {
            println("authorBatchDataLoader for keys = ${keys}")
            authorRepository.findAllById(keys).toList()
        }
    }
    var authorDataLoader = DataLoader.newDataLoader(authorBatchDataLoader)
}
