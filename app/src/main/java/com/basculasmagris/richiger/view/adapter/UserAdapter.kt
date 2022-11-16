package com.basculasmagris.richiger.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.databinding.ItemUserLayoutBinding
import com.basculasmagris.richiger.model.entities.User

class UserAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var users: List<User> = listOf()

    class ViewHolder (view: ItemUserLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivUserAvatar = view.ivUserAvatar
        val tvUserFullName = view.tvUserFullname
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserLayoutBinding = ItemUserLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvUserFullName.text = user.name + ' ' + user.lastname
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun userList(list: List<User>){
        users = list
        notifyDataSetChanged()
    }

}