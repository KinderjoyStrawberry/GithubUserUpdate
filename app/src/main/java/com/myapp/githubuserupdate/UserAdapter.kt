package com.myapp.githubuserupdate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(private val listUser: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var clickItem: ClickItem

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imAvatar: ImageView = itemView.findViewById(R.id.iv_avatar_org)
        var tvName: TextView = itemView.findViewById(R.id.tv_name_org)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_username_org)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_location_org)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, location, _, _, _, _, avatar) = listUser[position]
        Glide.with(holder.itemView.context).load(avatar).circleCrop().into(holder.imAvatar)
        holder.tvName.text = name
        holder.tvUsername.text = username
        holder.tvLocation.text = location

        holder.itemView.setOnClickListener { clickItem.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    fun setClickItem(clickItem: ClickItem) {
        this.clickItem = clickItem
    }

    interface ClickItem {
        fun onItemClicked(data: User)
    }
}