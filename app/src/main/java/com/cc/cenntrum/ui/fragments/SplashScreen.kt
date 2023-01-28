package com.cc.cenntrum.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentSplashScreenBinding


class SplashScreen : Fragment() {
    lateinit var binding: FragmentSplashScreenBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)


        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController



        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            navController.navigate(R.id.action_splashScreen_to_login)
        },3000)

        return binding.root

    }
}