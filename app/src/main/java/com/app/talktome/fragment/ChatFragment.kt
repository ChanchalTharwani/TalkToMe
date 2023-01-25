package com.app.talktome.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.talktome.R
import com.app.talktome.adapter.ChatAdapter
import com.app.talktome.databinding.FragmentChatBinding
import com.app.talktome.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatFragment : Fragment() {

    lateinit var binding:FragmentChatBinding
    private  var database: FirebaseDatabase?=null
    lateinit var userlist:ArrayList<UserModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        database = FirebaseDatabase.getInstance()
        userlist = ArrayList()

        //databse se value get krege
        database!!.reference.child("users")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        userlist.clear()
                    for (snapshot1 in snapshot.children ){
                        val users = snapshot1.getValue(UserModel::class.java)

                        if (users!!.uid != FirebaseAuth.getInstance().uid)
                            userlist.add(users)

                    }
                    Log.e("TAGGGGGGGGGGG", "onDataChange: ---" + userlist.toString(), )
                    binding.userListRecyclerView.layoutManager  = LinearLayoutManager(requireActivity())
                    binding.userListRecyclerView.adapter = ChatAdapter(requireActivity(),userlist)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
          return binding.root

    }


}