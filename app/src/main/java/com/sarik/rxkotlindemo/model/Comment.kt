package com.sarik.rxkotlindemo.model

/**
 * Created by Mehedi Hasan on 10/20/2020.
 */
data class Comment (
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
)