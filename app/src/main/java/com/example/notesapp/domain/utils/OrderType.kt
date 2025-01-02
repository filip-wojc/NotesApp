package com.example.notesapp.domain.utils

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}