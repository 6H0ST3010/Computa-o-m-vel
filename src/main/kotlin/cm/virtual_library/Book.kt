package org.example.cm.virtual_library

abstract class Book(
    val title: String,
    val author: String,
    val publicationYear: Int,
    var availableCopies: Int
) {

    val publicationCategory: String
        get() {
            return if (publicationYear < 1980) {
                "Classic"
            } else if (publicationYear in 1980..2010) {
                "Modern"
            } else {
                "Contemporary"
            }
        }

    val g_publicationYear: Int
        get() = publicationYear

    var s_availableCopies: Int
        get() = availableCopies
        set(value) {
            if (value < 0) {
                println("Negative copies its invalid")
                return
            }
            if (value == 0) {
                println("No available copies")
            }
            availableCopies = value
        }

    init {
        println("Book '$title' by $author has been added to the library.")
    }

    abstract fun getStorageInfo(): String

    override fun toString(): String {
        return "Title: $title | Author: $author | Year: $g_publicationYear | Available Copies: $availableCopies"
    }
}
