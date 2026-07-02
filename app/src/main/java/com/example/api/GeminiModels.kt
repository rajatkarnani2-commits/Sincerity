package com.example.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val responseMimeType: String? = null,
    val responseSchema: ResponseSchema? = null,
    val temperature: Float? = null
)

@JsonClass(generateAdapter = true)
data class ResponseSchema(
    val type: String,
    val properties: Map<String, ResponseSchema>? = null,
    val items: ResponseSchema? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>?
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content?
)

@JsonClass(generateAdapter = true)
data class SincerityReport(
    val sincerityScore: Int,
    val timelinessScore: Int,
    val consistencyScore: Int,
    val frictionPoints: List<String>,
    val strategies: List<String>,
    val advice: String
)

@JsonClass(generateAdapter = true)
data class MonthlyAnalysis(
    val monthName: String,
    val sincerityScore: Int,
    val timelinessScore: Int,
    val consistencyScore: Int,
    val shortSummary: String,
    val positivePoint: String,
    val negativePoint: String,
    val completedTasksCount: Int,
    val totalTasksCount: Int
)
