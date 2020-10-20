package com.sarik.rxkotlindemo.data.rest

import com.sarik.rxkotlindemo.model.Comment
import com.sarik.rxkotlindemo.model.Post
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by Mehedi Hasan on 10/20/2020.
 */
interface RequestApi {
    @GET("posts")
    fun getPosts(): Observable<List<Post>>

    @GET("posts/{id}/comments")
    fun getComments(
            @Path("id") id: Int
    ): Observable<List<Comment>>
}