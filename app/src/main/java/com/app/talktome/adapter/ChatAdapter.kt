package com.app.talktome.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.talktome.R
import com.app.talktome.activity.ChatActivity
import com.app.talktome.databinding.ChatUsersItemLayoutBinding
import com.app.talktome.model.UserModel
import com.bumptech.glide.Glide

class ChatAdapter(var context: Context,var list: ArrayList<UserModel>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {


    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var binding :ChatUsersItemLayoutBinding = ChatUsersItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from((parent.context))
            .inflate(R.layout.chat_users_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        var user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.userImage)
        holder.binding.username.text = user.name
        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("uid",user.uid)
            context.startActivity(intent)
            Toast.makeText(context, "Message sent!!", Toast.LENGTH_SHORT).show()


        }


    }

    override fun getItemCount(): Int {
        return list.size
    }



}