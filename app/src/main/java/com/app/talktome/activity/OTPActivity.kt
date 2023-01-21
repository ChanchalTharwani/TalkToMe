package com.app.talktome.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.app.talktome.R
import com.app.talktome.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtpactivityBinding

    //craete variable for FirebaseAuth,varificationId,dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var varificationId: String
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)

        //Initialize variable for FirebaseAuth,varificationId,dialog
        auth = FirebaseAuth.getInstance()

       //craete variable for alert dialog
        val builder = AlertDialog.Builder(this)
        //set alert dialog properties
        builder.setMessage("Please Wait......")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        //initialize alert diualog
        dialog = builder.create()
        dialog.show()

        //get phone number from previous activity(Phone number activity)
        val phonenumber = "+91"+intent.getStringExtra("number")


        //Send a verification code to the user's phone
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            // OnVerificationStateChangedCallbacks
            .setCallbacks(object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                Toast.makeText(applicationContext,"Plase try again !! ${p0}",Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    varificationId = p0
                }

            })

            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.buttonotp.setOnClickListener{
            dialog.show()
            if (binding.otpCode.text!!.isEmpty())
            {
                Toast.makeText(applicationContext,"Plese enter otp"   ,Toast.LENGTH_SHORT).show()
            }
            else{
                val credential = PhoneAuthProvider.getCredential(varificationId,binding.otpCode.text!!.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(){
                        if (it.isSuccessful){
                            dialog.dismiss()
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()
                        }
                        else{
                            dialog.dismiss()
                            Toast.makeText(applicationContext,"Error ${it.exception}",Toast.LENGTH_SHORT).show()
                        }

            }
            }
        }

    }
}