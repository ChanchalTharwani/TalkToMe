package com.app.talktome.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.talktome.R
import com.app.talktome.adapter.MessageAdapter
import com.app.talktome.databinding.ActivityChatBinding
import com.app.talktome.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase

    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String


    private lateinit var list : ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        senderUid = FirebaseAuth.getInstance().uid.toString()

        receiverUid = intent.getStringExtra("uid")!!

        list = ArrayList()

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        //initialize variable for database
        database = FirebaseDatabase.getInstance()


        binding.imageView2.setOnClickListener {
            if (binding.messagebox.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your message", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val message =
                    MessageModel(binding.messagebox.text.toString(), senderUid, Date().time)
                val randomKey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("chats").child(receiverRoom).child("message")
                            .child(randomKey).setValue(message).addOnSuccessListener {
                            binding.messagebox.text = null
                           Toast.makeText(this, "Message sent!!", Toast.LENGTH_SHORT).show()
                        }
                    }


            }

        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children){
                        val data  =  snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    binding.RecyclerView.adapter = MessageAdapter(this@ChatActivity,list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"Error :$error",Toast.LENGTH_SHORT).show()

                }

            })
    }
}