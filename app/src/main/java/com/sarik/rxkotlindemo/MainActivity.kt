package com.sarik.rxkotlindemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarik.rxkotlindemo.data.rest.ServiceGenerator
import com.sarik.rxkotlindemo.model.Post
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


private var disposable: CompositeDisposable = CompositeDisposable()
private val TAG = MainActivity::class.java.simpleName + " TagToDebug :"
private var adapter: RecyclerAdapter? = null
private var recyclerView: RecyclerView? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //test()

        initRv()

        getPostObservable()
            ?.observeOn(Schedulers.io())
            ?.flatMap {
                // WE GOT EACH POST, SO CALL COMMENT API TO GET COMMENT NUMBER FOR A SPECIFIC POST
                getCommentObservable(it)
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<Post> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: Post) {
                    Log.d(TAG, "onNext: ${t.title}")
                    updatePost(t)
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError: $e")
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete:")
                }

            })


        createMethod()
        bufferMethod()

    }

    private fun bufferMethod() {
        val taskObservable = Observable
            .fromIterable(getList())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

        taskObservable.buffer(2)
            .subscribe {
                Log.d(TAG, "bufferMethod: called -------------------------")
                for (item in it) {
                    Log.d(TAG, "bufferMethod: $item")
                }
            }
    }

    /*
    * GET NUMBER OF COMMENT FROM A SINGLE POST
     * */
    private fun getCommentObservable(post: Post): Observable<Post> {
        return ServiceGenerator.getRequestApi()
            .getComments(post.id)
            .subscribeOn(Schedulers.io())
            .map {
                post.comments = it
                post
            }

    }

    private fun updatePost(post: Post) {
        adapter?.updatePost(post)
    }

    /*
    * GET ALL POST FROM API
    * */
    private fun getPostObservable(): Observable<Post>? {
        return ServiceGenerator
            .getRequestApi()
            .getPosts() // Get pos api called
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //flat map parse each post observable and return it
            .flatMap {
                adapter?.setPosts(it as MutableList<Post>)
                Observable.fromIterable(it)
                    .subscribeOn(Schedulers.io())
            }
    }

    private fun initRv() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter()
        recyclerView?.adapter = adapter
    }

    private fun getList(): List<String> {
        val list = ArrayList<String>()
        list.add("1")
        list.add("2")
        list.add("32")
        list.add("4")
        list.add("5")
        list.add("50")
        list.add("5")
        list.add("100")
        list.add("950")
        list.add("34")
        return list
    }

    /*
    * SIMPLE FUNCTION USING CREATE OPERATOR
    * */
    private fun createMethod() {
        val list = getList()
        Observable.create<String> {
            for (item in list) {
                if (!it.isDisposed && item.length <= 2) {
                    it.onNext(item)
                }
            }
            if (!it.isDisposed) {
                it.onComplete()
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: String) {
                    Log.d(TAG, "onNext: ${t}")
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {
                }

            })

        /*
        *   Observable.just(someList) will give you 1 emission - a List.
        *   Observable.from(someList) will give you N emissions - each item in the list.
        * */

        //USE OF JUST
        Observable.just(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<String>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<String>) {

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })

        //USE OF FROM ARRAY
        Observable.fromArray(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            }

        /// RANGE
        Observable.range(0, 100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .repeat(3) // use repeat for repeating same task 3 times
            .subscribe {
                Log.d(TAG, "createMethod: range $it")
            }
    }

    private fun filterMethod() {
        val list = getList()

        val taskObservable = Observable
            .fromIterable(list) // pass the iterator
            .subscribeOn(Schedulers.io()) // specify the scheduler and this task will work in background thread like doInBackground
            .filter { it.length == 1 } // pass the observer if the condition is true
            .observeOn(AndroidSchedulers.mainThread()) // thread where data will emit like onPostExecute


        taskObservable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
                Log.d(TAG, "onSubscribe: called")
            }

            override fun onNext(t: String) {
                Log.d(TAG, "onNext: $t")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError: $e")
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: called")
            }
        })

        // Another way to add in disposable
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            disposable.add(taskObservable.subscribe {
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear() // clear just clear all the observer
        disposable.dispose() // dispose disable to re use

        /*
        *
        * Using clear will clear all, but can accept new disposable disposables. clear();
        * Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable disposables
        *
        * */
    }
}