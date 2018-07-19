package com.prance.teacher.features.match.view

fun generateKeyPadId(id: String): String {
    return String.format("%04d", id.toInt())
}