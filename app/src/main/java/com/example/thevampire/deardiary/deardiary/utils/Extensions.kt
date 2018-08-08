package com.example.thevampire.deardiary.deardiary.utils

import java.util.*

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start