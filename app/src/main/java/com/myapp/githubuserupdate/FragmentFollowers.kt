package com.myapp.githubuserupdate

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.myapp.githubuserupdate.databinding.FragmentFollowersBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FragmentFollowers(private val user: User) : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private val listUser = ArrayList<User>()
    private val adapter = UserAdapter(listUser)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listFOLLOWERS.apply {
            adapter = this@FragmentFollowers.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.listFOLLOWERS.setLayoutManager(layoutManager)
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.listFOLLOWERS.addItemDecoration(itemDecoration)
        showRecyclerList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataForRecycle()
    }

    private fun getDataForRecycle() {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${user.username}/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
            ) {
                binding.loading.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        getDetailUserData(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable,
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getDetailUserData(data: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$data"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
            ) {

                val result = String(responseBody)
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
                    binding.listFOLLOWERS.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable,
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showClickedUser(user: User) {
        val int = Intent(requireContext(), DetailUser::class.java)
        int.putExtra(DetailUser.EXTRA_DATA, user)
        startActivity(int)
    }

    private fun showRecyclerList() {
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.listFOLLOWERS.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.listFOLLOWERS.adapter = adapter

            adapter.setClickItem(object : UserAdapter.ClickItem {
                override fun onItemClicked(data: User) {
                    showClickedUser(data)
                }
            })
        } else {
            binding.listFOLLOWERS.layoutManager = LinearLayoutManager(requireContext())
            binding.listFOLLOWERS.adapter = adapter

            adapter.setClickItem(object : UserAdapter.ClickItem {
                override fun onItemClicked(data: User) {
                    showClickedUser(data)
                }
            })
        }
    }
}