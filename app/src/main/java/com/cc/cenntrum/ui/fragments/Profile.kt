package com.cc.cenntrum.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentProfileBinding
import com.cc.cenntrum.models.PasswordResetRequest
import com.cc.cenntrum.models.User
import com.cc.cenntrum.models.UserResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.GlobalData
import com.cc.cenntrum.utils.MyDatePickerDialogFragment
import com.cc.cenntrum.utils.SavedPreference
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.viewmodels.MainViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import gun0912.tedimagepicker.builder.TedImagePicker
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*



class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mContext: MainActivity

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var dob: String
    private lateinit var gender: String
    private lateinit var phone: String

    private lateinit var viewModel: MainViewModel
    private var userData: UserResponse.Data? = null

    private var uri: Uri? = null

    private lateinit var bitmap: Bitmap

    private var encodeImageString: String = ""

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                uri = result.data!!.data!!
                binding.profileImage.setImageURI(uri)

            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewModel = ViewModelProvider(mContext).get(MainViewModel::class.java)
        updateViews()


        binding.profileImage.setOnClickListener {
            chooseImage()
        }

        binding.btnReset.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            val viewGroup: ViewGroup = mContext.findViewById(android.R.id.content)
            val dialogView: View =
                LayoutInflater.from(mContext).inflate(R.layout.dialog_password, viewGroup, false)
            builder.setView(dialogView)
            val alertDialog: AlertDialog = builder.create()


            val button = dialogView.findViewById<TextView>(R.id.btn_submit)
            val prevPass = dialogView.findViewById<TextInputLayout>(R.id.layout_prev_password)
            val newPass = dialogView.findViewById<TextInputLayout>(R.id.layout_new_password)
            val confPass = dialogView.findViewById<TextInputLayout>(R.id.layout_confirm_password)

            button.setOnClickListener {
                if (newPass.editText?.text.toString()
                        .isNotEmpty() && confPass.editText?.text.toString()
                        .isNotEmpty() && prevPass.editText?.text.toString().isNotEmpty()
                ) {
                    if (newPass.editText?.text.toString() == confPass.editText?.text.toString()) {
                        viewModel.resetPassword(
                            PasswordResetRequest(
                                newPass.editText?.text.toString(),
                                prevPass.editText?.text.toString(),
                                GlobalData.user?.id!!
                            )
                        )


                        alertDialog.dismiss()
                    } else {
                        newPass.error = "Password not match!"
                    }
                } else {
                    if (prevPass.editText?.text.toString().isEmpty()) {
                        prevPass.error = "Enter Previous Password!"
                    }
                    if (newPass.editText?.text.toString().isEmpty()) {
                        newPass.error = "Enter New Password!"
                    }
                    if (confPass.editText?.text.toString().isEmpty()) {
                        confPass.error = "Confirm New Password!"
                    }
                }

            }



            alertDialog.show()
        }





        binding.drawer.setOnClickListener {
            mContext.toggleLeftDrawer(binding.drawerLayout, binding.leftDrawerMenu.root)
        }

        binding.back.setOnClickListener {
            mContext.toggleRightDrawer(binding.drawerLayout, binding.rightDrawerMenu.root)
        }


        binding.dobTextview.setOnClickListener(View.OnClickListener { v: View? ->
            val datePickerDialogFragment = MyDatePickerDialogFragment()

            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.dobTextview.text = "$year-$month1-$day1"
                }

                show(requireActivity().fragmentManager, "DatePickerDialogFragment")
            }
        })


        binding.ccp.registerCarrierNumberEditText(binding.editTextCarrierNumber)

//        binding.ccp.setPhoneNumberValidityChangeListener {
//            ToastUtils.showToast(requireContext(),"Number validation call")
//        }


//        binding.updateButton.setOnClickListener {
//
//            firstName = binding.firstnameEdittext.text.toString()
//            lastName = binding.lastnameEdittext.text.toString()
//            dob = binding.dobTextview.text.toString()
//            if (binding.ccp.isValidFullNumber) {
//                // upload data
//              //  phone = binding.ccp.fullNumber
//
//                var input: String = binding.editTextCarrierNumber.text.toString()
//                input = input.replace(" ", "")
//                phone = "${binding.ccp.selectedCountryCode} $input"
//
//            //    ToastUtils.showToast(requireContext(), "phone :${binding.ccp.selectedCountryName}")
//                val selectedId: Int = binding.radioGrp.checkedRadioButtonId
//                val radioSexButton = binding.root.findViewById<RadioButton>(selectedId)
//                // add another part within the multipart request
//
//                if(this.uri == null){
//                    viewModel.updateProfile(
//                        userID = userData?.id.toString()
//                            .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        firstname = firstName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        lastname = lastName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        dob = dob.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        gender = radioSexButton.text.toString()
//                            .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        phone = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        country = binding.ccp.selectedCountryName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        pic = null
//                    )
//                }
//                else {
//
//                    val file = getRealPathFromURI(this.uri!!)?.let { File(it) }
//
//                    val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                        "pic",
//                        file?.name,
//                        RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
//                    )
//
//                    viewModel.updateProfile(
//                        userID = userData?.id.toString()
//                            .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        firstname = firstName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        lastname = lastName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        dob = dob.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        gender = radioSexButton.text.toString()
//                            .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        phone = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        country = binding.ccp.selectedCountryName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//                        pic = filePart
//                    )
//
//                }
//                ToastUtils.showToast(requireContext(), "Profile updated successfully!")
//            } else {
//                binding.editTextCarrierNumber.error = getString(R.string.valid_number)
//                // ToastUtils.showToast(requireContext(),"Enter Valid Phone Number!")
//            }
//
//        }


        binding.updateButton.setOnClickListener {

            firstName = binding.firstnameEdittext.text.toString()
            lastName = binding.lastnameEdittext.text.toString()
            dob = binding.dobTextview.text.toString()
            if (binding.ccp.isValidFullNumber) {
                // upload data
                //  phone = binding.ccp.fullNumber

                var input: String = binding.editTextCarrierNumber.text.toString()
                input = input.replace(" ", "")
                phone = "${binding.ccp.selectedCountryCode} $input"

                //    ToastUtils.showToast(requireContext(), "phone :${binding.ccp.selectedCountryName}")
                val selectedId: Int = binding.radioGrp.checkedRadioButtonId
                val radioSexButton = binding.root.findViewById<RadioButton>(selectedId)
                // add another part within the multipart request
                val user: User = User(
                    userid = userData?.id,
                    firstname = firstName,
                    lastname = lastName,
                    dob = dob,
                    gender = radioSexButton.text.toString(),
                    phone = phone,
                    country = binding.ccp.selectedCountryName,
                    pic = encodeImageString
                )
                viewModel.updateProfile(user)

                ToastUtils.showToast(requireContext(), "Profile updated successfully!")
            } else {
                binding.editTextCarrierNumber.error = getString(R.string.valid_number)
                // ToastUtils.showToast(requireContext(),"Enter Valid Phone Number!")
            }

        }


        // initialize left and right drawer menu
        mContext.leftDrawerClickListener(
            binding.leftDrawerMenu.root,
            binding.drawerLayout,
            binding.leftDrawerMenu.root
        )
        mContext.rightDrawerClickListener(
            binding.rightDrawerMenu.root,
            binding.drawerLayout,
            binding.rightDrawerMenu.root
        )





        return binding.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                showSingleImage(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }



    private fun chooseImage() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    ImagePicker.with(requireActivity())
                        .cameraOnly()	//User can only capture image using Camera
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }



            }).check()
//        TedImagePicker.with(requireContext())
//            .start { uri -> showSingleImage(uri) }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (resultCode == Activity.RESULT_OK) {
//            //Image Uri will not be null for RESULT_OK
//            val uri: Uri = data?.data!!
//            showSingleImage(uri)
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    private fun showSingleImage(uri: Uri) {

        this.uri = uri
//        Glide.with(requireContext()).load(uri).into(mBinding.ivProfile)

        val inputStream: InputStream =
            requireActivity().contentResolver.openInputStream(this.uri!!)!!
        bitmap = BitmapFactory.decodeStream(inputStream)

        binding.profileImage.setImageBitmap(bitmap)

        encodeBitmapImage(bitmap)

    }


    private fun encodeBitmapImage(bitmap: Bitmap?) {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytesOfImage: ByteArray = byteArrayOutputStream.toByteArray()
        encodeImageString = Base64.encodeToString(bytesOfImage, Base64.DEFAULT)

        Log.i("TAG", "signUp --> encodeBitmapImage: $encodeImageString")

    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = activity?.contentResolver
            ?.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }


    fun updateViews() {
        userData = SavedPreference.getUserData(mContext)
        binding.firstnameEdittext.setText(userData?.firstname)
        binding.lastnameEdittext.setText(userData?.lastname)
        binding.dobTextview.text = userData?.dob


        binding.editTextCarrierNumber.setText(userData?.phone.toString().split(" ").get(1))

        binding.ccp.setCountryForPhoneCode(userData?.phone.toString().split(" ").get(0).toInt())


//        Picasso.get().load("https://cenntrum.codecoyapps.com/storage/app/${userData?.pic}")
//            .placeholder(R.drawable.logo)
//            .into(binding.profileImage)

        if (userData?.pic != null && userData?.pic!!.isNotEmpty())
           binding.profileImage.decodeBase64Image(userData?.pic.toString())

        if (userData?.gender == "Male") {
            binding.maleRadioBtn.isChecked = true
        } else {
            binding.femaleRadioBtn.isChecked = true
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    private fun isValid(): Boolean {

        var result = true
        if (TextUtils.isEmpty(firstName)) {
            binding.firstnameEdittext.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(lastName)) {
            binding.lastnameEdittext.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(dob)) {
            binding.dobTextview.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(phone)) {
            // binding.phoneEdittext.error = getString(R.string.required)
            ToastUtils.showToast(requireContext(), "Phone Number is Required!")
            result = false
        }
        return result
    }


}