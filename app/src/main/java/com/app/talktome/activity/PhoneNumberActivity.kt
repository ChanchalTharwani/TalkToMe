package com.app.talktome.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.talktome.MainActivity
import com.app.talktome.R

import com.app.talktome.databinding.ActivityPhoneNumberBinding
import com.google.firebase.auth.FirebaseAuth

class PhoneNumberActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhoneNumberBinding

    //create a variable for auth
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_number)

        //initialize a variable for auth
        auth = FirebaseAuth.getInstance()

        //check current is or not
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        binding.button.setOnClickListener {
            if (binding.phoneNumber.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Plese enter your Number!!", Toast.LENGTH_SHORT).show()
            } else {
                var intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("number", binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }

        }
    }
}