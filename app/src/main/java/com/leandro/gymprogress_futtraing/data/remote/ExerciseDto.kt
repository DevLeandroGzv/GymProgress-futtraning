package com.leandro.gymprogress_futtraing.data.remote

import com.leandro.gymprogress_futtraing.core.ExerciseTranslator

data class ExerciseDto(
    val name: String,
    val muscle: String,
    val equipment: String?, // <--- Agrega el ? aquí
    val difficulty: String?, // <--- Agrega el ? por si acaso
    val instructions: String
){

}
