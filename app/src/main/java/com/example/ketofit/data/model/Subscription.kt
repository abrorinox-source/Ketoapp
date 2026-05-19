package com.example.ketofit.data.model

data class Subscription(
    val active: Boolean,
    val planName: String,
    val endsAt: String? = null,
)

