package org.example.cm.virtual_library

class Library (val name: String){
    private val books = mutableListOf<Book>()

    companion object {
        private var totalBooksAdded: Int = 0

        fun getTotalBooksAdded() : Int {
            return totalBooksAdded
        }
    }

    fun addBook(book: Book) {
        books.add(book)
        totalBooksAdded++
    }

    fun borrowBook(title: String){
        val book = books.find { it.title == title }
        if (book == null) {
            println("The book $book was not found")
        }
        else {
            if (book.availableCopies <= 0) {
                println("Warning: Book is now out of stock!")
            }
            else {
                book.availableCopies--
                println("Successfully borrowed '${book.title}. Copies remaining: ${book.availableCopies}")
            }
        }
    }

    fun returnBook(title: String){
        val book = books.find { it.title == title }
        if (book == null) {
            println("The book '$book' was not found")
        }
        else {
            book.availableCopies++
            println("Book '${book.title}' returned successfully. Copies available: ${book.availableCopies}")
        }
    }

    fun showBooks(){
        if (books.isEmpty()){
            println("The library is empty")
            return
        }
        println("--- Library Catalog ---")
        for (book in books) {
            println("Title: ${book.title}, Author: ${book.author}, Era: ${book.publicationCategory}, Available: ${book.availableCopies} copies")
            println("   Storage: ${book.getStorageInfo()}")
        }
    }

    fun searchByAuthor(author: String){
        val result = books.filter { it.author == author }
        if (result.isEmpty()){
            println("No books were found of this Author, $author")
            return
        }
        println("Books by $author:")
        for (book in result) {
            println("- ${book.title} (${book.publicationCategory}, ${book.availableCopies} copy/copies available)")
        }
    }
}