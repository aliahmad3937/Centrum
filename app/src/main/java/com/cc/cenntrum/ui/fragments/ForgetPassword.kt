package com.cc.cenntrum.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentForgetPasswordBinding
import com.cc.cenntrum.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.util.ToastUtil
import java.util.regex.Pattern


@AndroidEntryPoint
class ForgetPassword : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentForgetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false)


        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.signinButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if(email.isNotEmpty() && isEmailValid(email)){
                binding.etEmail.setText("")
                viewModel.senForgotPasswordEmail(email)
                ToastUtil.showToast("You will Soon Receive Password Reset Email!")
            }else{
                binding.etEmail.error = "Enter Valid Email!"
            }
        }




        return binding.root
    }


    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}