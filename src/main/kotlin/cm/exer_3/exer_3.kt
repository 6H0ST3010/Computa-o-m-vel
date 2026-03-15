package org.example.cm.exer_3

fun main() {
    val bounces = generateSequence (100.0){
        if (it * 0.6 < 1){ //verifica se o resultado é inferior a 1
            null
        } else{
            it * 0.6}
    }
    println("Sequence: " +
            bounces.take(15). //apresenta apenas 15 resultados
            joinToString(" | "){"%.2f". //formata o resultado
            format(it)});
}