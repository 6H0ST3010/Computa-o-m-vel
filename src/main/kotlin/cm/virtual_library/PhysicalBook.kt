package org.example.cm.virtual_library

class PhysicalBook(
    title: String,
    author: String,
    publicationYear: Int,
    availableCopies: Int,
    val weight: Int,
    val hardCover: Boolean
) : Book(title, author, publicationYear, availableCopies) {

    override fun getStorageInfo(): String {
        return "Physical book: $weight g, HardCover: $hardCover"
    }

    override fun toString(): String {
        return super.toString() + getStorageInfo()
    }
}