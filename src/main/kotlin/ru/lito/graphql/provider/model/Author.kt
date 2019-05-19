package ru.lito.graphql.provider.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "author")
data class Author(
    @Id
    val id: String,
    val firstName: String,
    val lastName: String
)