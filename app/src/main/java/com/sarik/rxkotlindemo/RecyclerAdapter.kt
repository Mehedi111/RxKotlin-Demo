package com.sarik.rxkotlindemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.sarik.rxkotlindemo.model.Post


/**
 * Created by Mehedi Hasan on 10/20/2020.
 */
class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder?>() {
    private var posts: MutableList<Post> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_list_item, null, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setPosts(posts: MutableList<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    fun updatePost(post: Post) {
        posts[posts.indexOf(post)] = post
        notifyItemChanged(posts.indexOf(post))
    }

    fun getPosts(): List<Post> {
        return posts
    }

    inner class MyViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var numComments: TextView
        var progressBar: ProgressBar
        fun bind(post: Post) {
            title.text = post.title
            if (post.comments == null) {
                showProgressBar(true)
                numComments.text = ""
            } else {
                showProgressBar(false)
                numComments.text = post.comments.size.toString()
            }
        }

        private fun showProgressBar(showProgressBar: Boolean) {
            if (showProgressBar) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        init {
            title = itemView.findViewById(R.id.title)
            numComments = itemView.findViewById(R.id.num_comments)
            progressBar = itemView.findViewById(R.id.progress_bar)
        }
    }

    companion object {
        private const val TAG = "RecyclerAdapter"
    }
}