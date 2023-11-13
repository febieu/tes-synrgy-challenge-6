package com.anangkur.synrgychapter6.helper

import java.util.regex.Pattern



fun String.isEmailValid(): Boolean {
    val pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$")
    val matcher = pattern.matcher(this)
    if (!matcher.matches()) {
        return false
    }
    return true
}

fun String.isPasswordValid(): Boolean {
    return length > 8
}