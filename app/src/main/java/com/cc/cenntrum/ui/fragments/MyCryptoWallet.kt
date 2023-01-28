package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.cenntrum.R
import com.cc.cenntrum.adapters.SpinnerAdapter

import com.cc.cenntrum.adapters.WalletAdapter
import com.cc.cenntrum.databinding.FragmentMyCryptoWalletBinding
import com.cc.cenntrum.models.*

import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.*

import com.cc.cenntrum.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.util.ToastUtil

@AndroidEntryPoint
class MyCryptoWallet : Fragment() {
    private lateinit var binding: FragmentMyCryptoWalletBinding
    private lateinit var mContext: MainActivity
    private val viewModel: MainViewModel by viewModels()
    private var mList: ArrayList<IncentiveResponse.Data>? = null

    private var count = 1
    var adapter1: SpinnerAdapter? = null
    var adapter2: SpinnerAdapter? = null
    var adapter3: SpinnerAdapter? = null
    var adapter4: SpinnerAdapter? = null
    var adapter5: SpinnerAdapter? = null

    private var selected1: Int? = null
    private var selected2: Int? = null
    private var selected3: Int? = null
    private var selected4: Int? = null
    private var selected5: Int? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_crypto_wallet, container, false)
        //   setUpRecyclerView()

        listeners()


        if (MyApp.incentiveList == null) {
            viewModel.getIncentiveList(
                IncentiveRequest(
                    SavedPreference.getUserData(mContext)?.id,
                    "24.4444",
                    "76.552552"
                )
            )
        } else {
            mList = MyApp.incentiveList!!
            setUpSpinners(MyApp.incentiveList!!)
        }




        viewModel.incentiveResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    ToastUtils.showToast(requireContext(), "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    val response = it.data as IncentiveResponse

                    if (response.status == true) {
                        MyApp.incentiveList = response.data
                        mList = response.data
                        setUpSpinners(response.data)
                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {
                    //       Log.v("TAG6","response starting")
                }
            }
        })

        viewModel.walletAddressResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    LoadingUtils.pauseLoading()
                    ToastUtils.showToast(requireContext(), "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    LoadingUtils.pauseLoading()
                    val response = it.data as WalletResponse
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    resetViews()

                }
                is APIResponse.Starting -> {
                    //       Log.v("TAG6","response starting")
                }
            }
        })



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

        updateViews()

        binding.addAnother.setOnClickListener {
            if (count < 5) {
                count++
                updateViews()
            } else {
                ToastUtil.showToast("You can add Only 5 Address!")
            }

        }


        binding.btnSave.setOnClickListener {
            when (count) {
                1 -> {
                    if (binding.addr1.text.toString().isNotEmpty()) {
                        viewModel.addCryptoAddress(
                            WalletAddressRequest(
                                userId = SavedPreference.getUserData(mContext)?.id,
                                wallet = arrayListOf(
                                    WalletAddressRequest.Wallet(
                                        incId = selected1,
                                        address = binding.addr1.text.toString()
                                    )
                                )
                            )
                        )
                    } else {
                        binding.addr1.error = "Enter address!"
                    }
                }
                2 -> {
                    if (binding.addr1.text.toString().isNotEmpty() && binding.addr2.text.toString()
                            .isNotEmpty()
                    ) {

                        viewModel.addCryptoAddress(
                            WalletAddressRequest(
                                userId = SavedPreference.getUserData(mContext)?.id,
                                wallet = arrayListOf(
                                    WalletAddressRequest.Wallet(
                                        incId = selected1,
                                        address = binding.addr1.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected2,
                                        address = binding.addr2.text.toString()
                                    )
                                )
                            )
                        )
                    } else {
                        if (binding.addr1.text.toString().isEmpty()) binding.addr1.error =
                            "Enter address!"
                        if (binding.addr2.text.toString().isEmpty()) binding.addr2.error =
                            "Enter address!"
                    }
                }
                3 -> {
                    if (binding.addr1.text.toString().isNotEmpty() && binding.addr2.text.toString()
                            .isNotEmpty() && binding.addr3.text.toString().isNotEmpty()
                    ) {

                        viewModel.addCryptoAddress(
                            WalletAddressRequest(
                                userId = SavedPreference.getUserData(mContext)?.id,
                                wallet = arrayListOf(
                                    WalletAddressRequest.Wallet(
                                        incId = selected1,
                                        address = binding.addr1.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected2,
                                        address = binding.addr2.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected3,
                                        address = binding.addr3.text.toString()
                                    )
                                )
                            )
                        )
                    } else {
                        if (binding.addr1.text.toString().isEmpty()) binding.addr1.error =
                            "Enter address!"
                        if (binding.addr2.text.toString().isEmpty()) binding.addr2.error =
                            "Enter address!"
                        if (binding.addr3.text.toString().isEmpty()) binding.addr3.error =
                            "Enter address!"
                    }
                }
                4 -> {
                    if (binding.addr1.text.toString().isNotEmpty()
                        && binding.addr2.text.toString().isNotEmpty()
                        && binding.addr3.text.toString().isNotEmpty()
                        && binding.addr4.text.toString().isNotEmpty()
                    ) {

                        viewModel.addCryptoAddress(
                            WalletAddressRequest(
                                userId = SavedPreference.getUserData(mContext)?.id,
                                wallet = arrayListOf(
                                    WalletAddressRequest.Wallet(
                                        incId = selected1,
                                        address = binding.addr1.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected2,
                                        address = binding.addr2.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected3,
                                        address = binding.addr3.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected4,
                                        address = binding.addr4.text.toString()
                                    )
                                )
                            )
                        )
                    } else {
                        if (binding.addr1.text.toString().isEmpty()) binding.addr1.error =
                            "Enter address!"
                        if (binding.addr2.text.toString().isEmpty()) binding.addr2.error =
                            "Enter address!"
                        if (binding.addr3.text.toString().isEmpty()) binding.addr3.error =
                            "Enter address!"
                        if (binding.addr4.text.toString().isEmpty()) binding.addr4.error =
                            "Enter address!"
                    }
                }
                5 -> {
                    if (binding.addr1.text.toString().isNotEmpty()
                        && binding.addr2.text.toString().isNotEmpty()
                        && binding.addr3.text.toString().isNotEmpty()
                        && binding.addr4.text.toString().isNotEmpty()
                        && binding.addr5.text.toString().isNotEmpty()
                    ) {
                        viewModel.addCryptoAddress(
                            WalletAddressRequest(
                                userId = SavedPreference.getUserData(mContext)?.id,
                                wallet = arrayListOf(
                                    WalletAddressRequest.Wallet(
                                        incId = selected1,
                                        address = binding.addr1.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected2,
                                        address = binding.addr2.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected3,
                                        address = binding.addr3.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected4,
                                        address = binding.addr4.text.toString()
                                    ),
                                    WalletAddressRequest.Wallet(
                                        incId = selected5,
                                        address = binding.addr5.text.toString()
                                    )
                                )
                            )
                        )
                    } else {
                        if (binding.addr1.text.toString().isEmpty())
                            binding.addr1.error = "Enter address!"
                        if (binding.addr2.text.toString().isEmpty())
                            binding.addr2.error = "Enter address!"
                        if (binding.addr3.text.toString().isEmpty())
                            binding.addr3.error = "Enter address!"
                        if (binding.addr4.text.toString().isEmpty())
                            binding.addr4.error = "Enter address!"
                        if (binding.addr5.text.toString().isEmpty())
                            binding.addr5.error = "Enter address!"
                    }
                }
            }
        }





        return binding.root
    }

    private fun resetViews() {
        when(count){
            1 ->{
                binding.addr1.setText("")
                count = 1
                updateViews()
            }
            2 ->{
                binding.addr1.setText("")
                binding.addr2.setText("")
                count = 1
                updateViews()
            }
            3 ->{
                binding.addr1.setText("")
                binding.addr2.setText("")
                binding.addr3.setText("")
                count = 1
                updateViews()
            }
            4 ->{
                binding.addr1.setText("")
                binding.addr2.setText("")
                binding.addr3.setText("")
                binding.addr4.setText("")
                count = 1
                updateViews()
            }
            5 ->{
                binding.addr1.setText("")
                binding.addr2.setText("")
                binding.addr3.setText("")
                binding.addr4.setText("")
                binding.addr5.setText("")
                count = 1
                updateViews()
            }
        }
    }

    private fun listeners() {

        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selected1 = mList!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selected2 = mList!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selected3 = mList!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selected4 = mList!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.spinner5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selected5 = mList!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun setUpSpinners(data: ArrayList<IncentiveResponse.Data>) {
        adapter1 = SpinnerAdapter(requireContext(), data)
        binding.spinner1.adapter = adapter1

        adapter2 = SpinnerAdapter(requireContext(), data)
        binding.spinner2.adapter = adapter2

        adapter3 = SpinnerAdapter(requireContext(), data)
        binding.spinner3.adapter = adapter3

        adapter4 = SpinnerAdapter(requireContext(), data)
        binding.spinner4.adapter = adapter4

        adapter5 = SpinnerAdapter(requireContext(), data)
        binding.spinner5.adapter = adapter5

    }

    private fun updateViews() {
        when (count) {
            1 -> {
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.GONE
                binding.layout3.visibility = View.GONE
                binding.layout4.visibility = View.GONE
                binding.layout5.visibility = View.GONE
            }
            2 -> {
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.VISIBLE
                binding.layout3.visibility = View.GONE
                binding.layout4.visibility = View.GONE
                binding.layout5.visibility = View.GONE
            }
            3 -> {
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.VISIBLE
                binding.layout3.visibility = View.VISIBLE
                binding.layout4.visibility = View.GONE
                binding.layout5.visibility = View.GONE
            }
            4 -> {
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.VISIBLE
                binding.layout3.visibility = View.VISIBLE
                binding.layout4.visibility = View.VISIBLE
                binding.layout5.visibility = View.GONE
            }
            5 -> {
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.VISIBLE
                binding.layout3.visibility = View.VISIBLE
                binding.layout4.visibility = View.VISIBLE
                binding.layout5.visibility = View.VISIBLE
            }
        }
    }


//    private fun setUpRecyclerView() {
//        mList = ArrayList()
//        mList!!.add(WalletResponse())
//        mList!!.add(WalletResponse())
//        mList!!.add(WalletResponse())
//        mList!!.add(WalletResponse())
//        mList!!.add(WalletResponse())
//        mList!!.add(WalletResponse())
//        mList!!.add(WalletResponse())
//
//        mAdapter = WalletAdapter(mList!!, requireContext())
//        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        binding.rvWallet.layoutManager = linearLayoutManager
//        binding.rvWallet.adapter = mAdapter
//    }

    override fun onPause() {
        count = 1
        updateViews()
        super.onPause()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

}