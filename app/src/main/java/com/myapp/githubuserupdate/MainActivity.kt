package com.myapp.githubuserupdate

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.myapp.githubuserupdate.databinding.ActivityMainBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listUser = ArrayList<User>()
    private val adapter = UserAdapter(listUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's"

        val layoutManager = LinearLayoutManager(this)
        binding.listUSER.setLayoutManager(layoutManager)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listUSER.addItemDecoration(itemDecoration)

        getListUser()
        showRecyclerList()
    }

    private fun getListUser() {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, ) {
                binding.loadingProcess.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login = jsonObject.getString("login")
                        getDetailUserData(login)
                    }
                } catch (e: Exception) {
                    Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable, ) {
                binding.loadingProcess.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getDetailUserData(data: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$data"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username = jsonObject.getString("login")
                    val name = jsonObject.getString("name")
                    val location = jsonObject.getString("location")
                    val repository = jsonObject.getString("public_repos")
                    val company = jsonObject.getString("company")
                    val followers = jsonObject.getString("followers")
                    val following = jsonObject.getString("following")
                    val avatar = jsonObject.getString("avatar_url")
                    val user = User(username, name, location, repository, company, followers, following, avatar)
                    listUser.add(user)
                    binding.listUSER.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable, ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showClickedUser(user: User) {
        val int = Intent(this@MainActivity, DetailUser::class.java)
        int.putExtra(DetailUser.EXTRA_DATA, user)
        startActivity(int)
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.listUSER.layoutManager = GridLayoutManager(this, 2)
            binding.listUSER.adapter = adapter

            adapter.setClickItem(object : UserAdapter.ClickItem {
                override fun onItemClicked(data: User) {
                    showClickedUser(data)
                }
            })
        } else {
            binding.listUSER.layoutManager = LinearLayoutManager(this)
            binding.listUSER.adapter = adapter

            adapter.setClickItem(object : UserAdapter.ClickItem {
                override fun onItemClicked(data: User) {
                    showClickedUser(data)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.loadingProcess.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                searchUserData(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    fun searchUserData(query: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$query"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, ) {
                binding.loadingProcess.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    listUser.clear()
                    val getObject = JSONObject(result)
                    val userList = getObject.getJSONArray("items")
                    for (i in 0 until userList.length()) {
                        val jsonObject = userList.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        getDetailUserData(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable, ) {
                binding.loadingProcess.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}