package org.example.cm.virtual_library

data class LibraryMember (
    val name : String,
    val memberId: Int,
    val borrowedbooks: MutableList<String> = mutableListOf(),
)