package com.ss.haat.Data

data class register(
        val name:String? = null,
        val phone:String? = null,
        val password:String? = null,
        val address:String? = null
)

data class ret(
        val name:String? = null
)

data class login(
        val phone: String?,
        val password: String?
)