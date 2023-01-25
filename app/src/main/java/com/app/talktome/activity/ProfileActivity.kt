package com.app.talktome.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.app.talktome.MainActivity
import com.app.talktome.R
import com.app.talktome.databinding.ActivityProfileBinding
import com.app.talktome.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding

    //craete variable for FirebaseAuth,database,storage,image,dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedimg: Uri
    var imageNotUpload = true

    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        //initialize variable for FirebaseAuth,database,storage,image,dialog
        auth = FirebaseAuth.getInstance()
        //craete variable for alert dialog
        val builder = AlertDialog.Builder(this)
        //set alert dialog properties
        builder.setMessage("Updating profile......")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        //initialize alert diualog
        // dialog = builder.create()
        // dialog.show()
        //initialize variable for database
        database = FirebaseDatabase.getInstance()
        //initialize variable for storage
        storage = FirebaseStorage.getInstance()


        //set profile image
        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        //save data of profile
        binding.ContinueButton.setOnClickListener {
            if (binding.username.text!!.isEmpty()) {
                Toast.makeText(applicationContext, "Enter user name", Toast.LENGTH_SHORT)
                    .show()
            } else if (imageNotUpload ) {
                Toast.makeText(applicationContext, "Please Image upload", Toast.LENGTH_SHORT)
                    .show()

            } else {
                uploadData()
            }
        }


    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedimg).addOnCompleteListener{
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }
        //function for name image and image profile
    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(auth.uid.toString(), binding.username.text.toString(),
            auth.currentUser!!.phoneNumber.toString(), imgUrl)
        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Data inserted successfully", Toast.LENGTH_SHORT)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                imageNotUpload = false
                selectedimg = data.data!!
                binding.userImage.setImageURI(selectedimg)
            }
        }


    }
}