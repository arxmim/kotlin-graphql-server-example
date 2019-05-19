package ru.lito.graphql.provider.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "book")
data class Book(
    @Id
    val id: String,
    val name: String,
    val pageCount: Int,
    @OneToOne
    val author: Author,
    @OneToOne
    val prevBook: Book? = null
)