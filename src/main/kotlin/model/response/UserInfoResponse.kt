package model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val id: Int,
    val username: String?,
    val email: String,
    val roles: List<String>
)