package com.app.talktome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.talktome.activity.PhoneNumberActivity
import com.app.talktome.adapter.ViewPagerAdapter
import com.app.talktome.databinding.ActivityMainBinding
import com.app.talktome.fragment.CallFragment
import com.app.talktome.fragment.ChatFragment
import com.app.talktome.fragment.StatusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //create a variable for auth
    private  lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())
        //initialize a variable for auth
        auth = FirebaseAuth.getInstance()
        //check current is or not
        if (auth.currentUser == null) {
            startActivity(Intent(this, PhoneNumberActivity::class.java))
            finish()
        }


        val adapter = ViewPagerAdapter(this, supportFragmentManager, fragmentArrayList)

        binding.ViewPager.adapter = adapter

        binding.tabs.setupWithViewPager(binding.ViewPager)

    }
}