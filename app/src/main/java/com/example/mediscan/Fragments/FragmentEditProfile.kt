package com.example.mediscan.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mediscan.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_saved.*
import java.text.SimpleDateFormat
import java.util.*



class EditProfileFragment : Fragment() {

    // private lateinit var binding: FragmentEditProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var firebaseStorage: FirebaseStorage

    //Image uri
    private var imageUri: Uri? = null

    val profileFragment = ProfileFragment()
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        //init progress bar
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wiat")
        progressDialog.setCanceledOnTouchOutside(false)

        loaduserinfo()
        edit_button.setOnClickListener {
            validateData()

        }
        upload_img.setOnClickListener {
            editImage()
        }

    }


    private var email = ""
    private var firstName = ""
    private var lastName = ""
    private var phone = ""
    private var dob = ""
    private fun validateData() {
        email = edit_email_input.text.toString().trim()
        firstName = edit_first_name_input.text.toString().trim()
        lastName = edit_last_name_input.text.toString().trim()
        phone = edit_phone_input.text.toString().trim()
        dob = edit_dob_input.text.toString().trim()

        //Validate Data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                context,
                "Invalid email",
                Toast.LENGTH_SHORT
            ).show()
        } else if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            Toast.makeText(
                context,
                "Please fill in all fields.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if(imageUri == null){
                updateUserInfo("")
            }else{
                uploadImg()
            }
        }
    }

    private fun uploadImg() {
        progressDialog.setMessage("Uploading Image")
        progressDialog.show()
        //image path
        val filePathName = "images/"+firebaseAuth.uid

        //storage ref
        val refs = FirebaseStorage.getInstance().getReference(filePathName)
        refs.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateUserInfo(uploadedImageUrl)


            }.addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(context,"Failed To Upload Image",Toast.LENGTH_SHORT).show()

            }
    }



    private fun updateUserInfo(uploadImageUrl: String) {
        //save user info
        progressDialog.setMessage("Updating Profile")
        progressDialog.show()
        //setup data to add in db
        val hashmap: HashMap<String, Any?> = HashMap()
        hashmap["email"] = email
        hashmap["firstName"] = firstName
        hashmap["lastName"] = lastName
        hashmap["phone"] = phone
        hashmap["dob"] = dob
        //hashmap["profileImage"] = "" //add manully later
        //hashmap["timestamp"] = timeStamp
        //hashmap["profileImage"] = uploadImageUrl
        if(imageUri != null){
            hashmap["profileImage"] = uploadImageUrl
        }
        //set data in db
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashmap)
            .addOnSuccessListener {
                //user info saved
                Toast.makeText(
                    context,
                    "User info saved",
                    Toast.LENGTH_SHORT
                ).show()

                loadProfile()


            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    context,
                    "Failed saving User Info due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
    private fun loaduserinfo() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    //user info
                    val firstName ="${snapshot.child("firstName").value}"
                    val lastName ="${snapshot.child("lastName").value}"
                    val email ="${snapshot.child("email").value}"
                    val phone ="${snapshot.child("phone").value}"
                    val dob ="${snapshot.child("dob").value}"
                    val pimg ="${snapshot.child("profileImage").value}"

                    //val formatDate = MyApplication.formatTimeStamp(timestamp.toLong())
                    edit_first_name_input.setText(firstName)
                    edit_last_name_input.setText(lastName)
                    edit_email_input.setText(email)
                    edit_dob_input.setText(dob)
                    edit_phone_input.setText(phone)


                    try {
                        Glide.with(this@EditProfileFragment).load(pimg).placeholder(R.drawable.prof).into(edit_profile_img)
                    }catch (e: Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun loadProfile() {
        val fragment = ProfileFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }




    private fun editImage() {
        val popupMenu = PopupMenu(context,edit_profile_img)
        popupMenu.menu.add(Menu.NONE,0,0,"Camera")
        popupMenu.menu.add(Menu.NONE,1,1,"Gallery")
        popupMenu.show()

        //handle item click
        popupMenu.setOnMenuItemClickListener{item->
            val id = item.itemId

            if(id == 0){
                //Camera clicked
                pickImageCamera()
            } else if (id==1){
                pickImageGallery()
            }
            true
        }
    }

    private fun pickImageCamera() {
        val context = this as Context
        //val result = context!!.contentResolver as ContentResolver
        val vaules = ContentValues()
        vaules.put(MediaStore.Images.Media.TITLE,"Temp_Title")
        vaules.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,vaules)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(intent)

    }
    private fun pickImageGallery(){
        //intent for galleryview
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    //camera
    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            //handles result of camera
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                //imageUri = data!!.data

                edit_profile_img.setImageURI(imageUri)
            } else{
                Toast.makeText(context,"Failed Camera",Toast.LENGTH_SHORT).show()

            }
        }
    )

    //Gallery
    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                edit_profile_img.setImageURI(imageUri)
                //Toast.makeText(context," Uploaded Image",Toast.LENGTH_SHORT).show()

            } else{
                Toast.makeText(context,"Failed Upload",Toast.LENGTH_SHORT).show()

            }
        }
    )

}


