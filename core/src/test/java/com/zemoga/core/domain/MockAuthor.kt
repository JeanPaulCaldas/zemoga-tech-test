package com.zemoga.core.domain

internal data class MockAuthor(val id: Int = 1) {
    fun create() = Author(
        id = id,
        name = "mock name $id",
        email = "mock email $id",
        phone = "mock phone $id",
        website = "mock website $id",
    )

    companion object {
        fun mockList(authorsNumber: Int): List<Author> {
            val authors = mutableListOf<Author>()
            for (i in 1..authorsNumber) {
                authors.add(MockAuthor(id = i).create())
            }
            return authors
        }
    }
}