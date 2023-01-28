package com.cc.cenntrum.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentSignUpBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.UserResponse
import com.cc.cenntrum.models.User
import com.cc.cenntrum.models.UserRequest
import com.cc.cenntrum.utils.LoadingUtils
import com.cc.cenntrum.utils.MyDatePickerDialogFragment
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.utils.ToastUtils.showToast
import com.cc.cenntrum.viewmodels.MainViewModel
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.regex.Pattern


@AndroidEntryPoint
class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val TAG = "SignUp"

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var dob: String
    private lateinit var gender: String
    private lateinit var phone: String
    private var encodeImageString:String =""

    private var uri: Uri? = null

    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        binding.tvDont.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_login)
        }


        binding.imageLayout.setOnClickListener {
            chooseImage()
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    showToast(requireContext(), "${it.message}")
                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                    val response = it.data as UserResponse

                    if (response.status == true) {
                        showToast(requireContext(), getString(R.string.register_successfully))
                        findNavController().navigate(R.id.action_signUp_to_login)
                        /// action perform

                    } else {
                       // showToast(requireContext(), getString(R.string.something_went_wrong))
                        showToast(requireContext(), response.message.toString())
                    }
                }
                APIResponse.Starting -> {}
            }
        })




        binding.dobTextview.setOnClickListener(View.OnClickListener { v: View? ->
            val datePickerDialogFragment = MyDatePickerDialogFragment()



            datePickerDialogFragment.run {
                setOnDateChooseListener { year, month, day ->
                    var day1 = day.toString()
                    var month1 = month.toString()
                    day1 = if (day1.length == 1) "0$day1" else day1
                    month1 = if (month1.length == 1) "0$month1" else month1
                    binding.dobTextview.setText("$year-$month1-$day1")
                }

                show(requireActivity().fragmentManager, "DatePickerDialogFragment")
            }
        })




        binding.ccp.registerCarrierNumberEditText(binding.editTextCarrierNumber);
        binding.signupButton.setOnClickListener {

            firstName = binding.firstnameEdittext.text.toString()
            lastName = binding.lastnameEdittext.text.toString()
            email = binding.emailEdittext.text.toString().trim()
            password = binding.passwordEdittext.text.toString()
            dob = binding.dobTextview.text.toString()
            if (binding.ccp.isValidFullNumber) {
           //     phone = binding.ccp.fullNumber
                var input: String = binding.editTextCarrierNumber.text.toString()
                input = input.replace(" ", "")
                phone = "${binding.ccp.selectedCountryCode} $input"
            } else {
                phone = ""
            }
            val selectedId: Int = binding.radioGrp.getCheckedRadioButtonId()
            val radioSexButton = binding.root.findViewById<RadioButton>(selectedId)
            val user = UserRequest(
                firstname = firstName,
                lastname = lastName,
                email = email,
                dob = dob,
                gender = radioSexButton.text.toString(),
                phone = phone,
                password = password,
                country = binding.ccp.selectedCountryName,
                pic = encodeImageString
            )

            if (isValid()) {
                if(binding.textView5.isChecked){
                viewModel.createUser(user)
                }else{
                    ToastUtils.showToast(requireContext(),getString(R.string.accept_privacy_policy))
                }
            }

        }





        return binding.root
    }

    private fun chooseImage() {

        TedImagePicker.with(requireContext())
            .start { uri -> showSingleImage(uri) }
    }

    private fun showSingleImage(uri: Uri) {

        this.uri = uri
//        Glide.with(requireContext()).load(uri).into(mBinding.ivProfile)

        val inputStream: InputStream =
            requireActivity().contentResolver.openInputStream(this.uri!!)!!
       val bitmap = BitmapFactory.decodeStream(inputStream)

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

    private fun isValid(): Boolean {

        var result = true
        if (TextUtils.isEmpty(firstName)) {
            binding.firstnameEdittext.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(lastName)) {
            binding.lastnameEdittext.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(email)) {
            binding.emailEdittext.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordEdittext.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(dob)) {
            binding.dobTextview.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(phone)) {
            binding.editTextCarrierNumber.error = getString(R.string.valid_number)
            result = false
        } else if (!isEmailValid(email)) {
            binding.emailEdittext.error = getString(R.string.invalid_email)
            result = false
        }
        return result
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}