package model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val msg: String,
    val statusCode: Int,
    val path: String
)
