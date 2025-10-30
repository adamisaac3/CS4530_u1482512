package com.example.funfacts.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FunFactResponse(
    @SerialName("id")
    val apiId: String,
    val text: String,
    val source: String,
    @SerialName("source_url")
    val sourceUrl: String,
    val language: String,
    val permalink: String
)