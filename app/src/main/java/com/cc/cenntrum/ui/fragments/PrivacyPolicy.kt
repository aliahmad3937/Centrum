package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentPrivacyPolicyBinding
import com.cc.cenntrum.ui.activities.MainActivity

class PrivacyPolicy : Fragment() {
    private lateinit var binding: FragmentPrivacyPolicyBinding
    private lateinit var mContext: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false)
        binding.tvWritten.movementMethod = ScrollingMovementMethod()


        binding.drawer.setOnClickListener {
            mContext.toggleLeftDrawer(binding.drawerLayout, binding.leftDrawerMenu.root)
        }

        binding.back.setOnClickListener {
            mContext.toggleRightDrawer(binding.drawerLayout, binding.rightDrawerMenu.root)
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




    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

}