package ru.lito.graphql.provider.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.lito.graphql.provider.model.Author

@Repository
interface AuthorRepository : CrudRepository<Author, String>