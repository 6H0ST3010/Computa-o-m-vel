package org.example.cm.exer_1

fun main() {
    //ex1a
    val arr1 = IntArray(50);
    for (i in arr1.indices){
        arr1[i] = (i + 1) * (i + 1);
    }
    println(arr1.joinToString());

    //ex1b
    val arr2 = (1..50).map { it * it }.toIntArray()
    println(arr2.joinToString());

    //ex1c
    val arr3 = Array(50) { i -> (i + 1) * (i + 1)}
    println(arr3.joinToString());
}