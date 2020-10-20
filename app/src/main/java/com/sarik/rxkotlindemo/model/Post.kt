package com.sarik.rxkotlindemo.model

/**
 * Created by Mehedi Hasan on 10/20/2020.
 */
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
    var comments: List<Comment>
)
