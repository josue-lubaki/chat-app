package ca.josue_lubaki.register.domain.model

/**
 * created by Josue Lubaki
 * date : 2023-10-26
 * version : 1.0.0
 */

data class Register(
    val userId : String,
    val username : String,
    val email : String,
    val profileImage : String? = null
)