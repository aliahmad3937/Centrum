package com.cc.cenntrum.ui.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog
import com.cc.cenntrum.R
import com.cc.cenntrum.adapters.FriendsAdapter
import com.cc.cenntrum.callbacks.FriendCallback
import com.cc.cenntrum.databinding.FragmentFriendsBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.FriendRequest
import com.cc.cenntrum.models.FriendResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.GlobalData
import com.cc.cenntrum.utils.LoadingUtils
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Friends : Fragment() , FriendCallback{
    private lateinit var binding: FragmentFriendsBinding
    var friendList: MutableList<FriendResponse.Data> = arrayListOf()
    var friendList2: MutableList<FriendResponse.Data> = arrayListOf()
    var otherPeopleList: MutableList<FriendResponse.Data> = arrayListOf()
    var otherPeopleList2: MutableList<FriendResponse.Data> = arrayListOf()
    lateinit var friendsAdapter: FriendsAdapter
    lateinit var otherPeopleAdapter: FriendsAdapter
  //  private val viewModel: MainViewModel by viewModels()

    private lateinit var viewModel: MainViewModel
    private lateinit var mContext:  MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        viewModel = ViewModelProvider(mContext).get(MainViewModel::class.java)





        binding.friendSeemore.setOnClickListener {
            if(friendList.isNotEmpty()) {
                val action = FriendsDirections.actionFriendsFragmentToMoreFriends(friend = true)
                findNavController().navigate(action)
            }
        }

        binding.otherSeemore.setOnClickListener {
            if(otherPeopleList.isNotEmpty()) {
                val action = FriendsDirections.actionFriendsFragmentToMoreFriends(false)
                findNavController().navigate(action)
            }
        }


//        if(viewModel.friendResponse.value is APIResponse.Starting){
//            GlobalData.user?.id?.let { viewModel.getFriendList(it)
//                Log.v("TAG9","friend list")
//            }
//        }
//        if(viewModel.otherPeopleResponse.value is APIResponse.Starting){
//            GlobalData.user?.id?.let { viewModel.getOtherPeopleList(it) }
//        }

        setFriendsAdapter()
        setOtherPeopleAdapter()
        viewModel.friendResponse.observe(viewLifecycleOwner, Observer {
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

                    val response = it.data as FriendResponse
                    Log.d("TAG8", "onCreate   viewModel.movieList.observe: ${response.data}")
                    if (response.status == true) {
//                          friendList.clear()
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        friendList.addAll(response.data)
//                        if(friendList.size > 8){
//                            friendList.add(7,FriendResponse.Data(firstname = "See", lastname = "More"))
//                        }
//                        Log.v("TAG9","size 1: ${friendList.size}")
//                        friendsAdapter.updateAdapter(friendList)

                        if(response.data.size > 0){
                            binding.recFriends.visibility = View.VISIBLE
                            binding.noFriends.visibility = View.GONE
//                        if(response.data.size > 8){
//                            friendList.clear()
//                            friendList.addAll(response.data)
//                            friendList.add(7,FriendResponse.Data(firstname = "See", lastname = "More"))
//                            friendsAdapter.updateAdapter(friendList)
//                        }else{
                            friendList.clear()
                            friendList.addAll(response.data)
                            friendsAdapter.notifyDataSetChanged()

                      //  }
                        }else{
                            binding.recFriends.visibility = View.INVISIBLE
                            binding.noFriends.visibility = View.VISIBLE
                        }
                        Log.v("TAG9","size 1: ${response.data.size}")


                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {}
            }
        })
        viewModel.otherPeopleResponse.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is APIResponse.Loading -> {
                    //  LoadingUtils.showLoading(requireActivity())
                }
                is APIResponse.Error -> {
                    //   LoadingUtils.pauseLoading()
                    ToastUtils.showToast(requireContext(), "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    //   LoadingUtils.pauseLoading()
                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                    val response: FriendResponse = it.data as FriendResponse

                    if (response.status == true) {
//                        otherPeopleList.clear()
//                        otherPeopleList.addAll(response.data)
//                        otherPeopleList.addAll(response.data)
//                        otherPeopleList.addAll(response.data)
//                        otherPeopleList.addAll(response.data)
//                        otherPeopleList.addAll(response.data)
//                        otherPeopleList.addAll(response.data)
//                        if(otherPeopleList.size > 12){
//                            otherPeopleList.add(11,FriendResponse.Data(firstname = "See ", lastname = "More"))
//                        }
//
//                        Log.v("TAG9","size 2 : ${otherPeopleList.size}")
//
//                        otherPeopleAdapter.updateAdapter(otherPeopleList)

//                        if(response.data.size > 12){
//                            otherPeopleList.clear()
//                            otherPeopleList.addAll(response.data)
//                            otherPeopleList.add(11,FriendResponse.Data(firstname = "See", lastname = "More"))
//                            otherPeopleAdapter.updateAdapter(otherPeopleList)
//                        }else{
                        otherPeopleList.clear()
                        otherPeopleList.addAll(response.data)
                        otherPeopleAdapter.notifyDataSetChanged()

                    //    }

                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {}
            }
        })


        binding.back.setOnClickListener {
            mContext.toggleLeftDrawer(binding.drawerLayout, binding.leftDrawerMenu.root)
        }

        binding.search.setOnClickListener {
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



//

        return binding.root
    }

    private fun setFriendsAdapter() {
        friendsAdapter = FriendsAdapter(activity!!, friendList , isFriend = true ,this)
        binding.recFriends.layoutManager = GridLayoutManager(activity, 4)
        binding.recFriends.adapter = friendsAdapter
    }

    private fun setOtherPeopleAdapter() {
        otherPeopleAdapter = FriendsAdapter(activity!!, otherPeopleList , isFriend = false,this)
        binding.recOtherPeople.layoutManager = GridLayoutManager(activity, 4)
        binding.recOtherPeople.adapter = otherPeopleAdapter
    }


    override fun onResume() {
        super.onResume()
        GlobalData.user?.id?.let { viewModel.getFriendList(it)}
        GlobalData.user?.id?.let { viewModel.getOtherPeopleList(it) }

    }

    override fun onFriendClick(pos: Int, friend: FriendResponse.Data, isFriends: Boolean) {
        if(isFriends){
            if(pos == 7){
                val action = FriendsDirections.actionFriendsFragmentToMoreFriends(isFriends)
                findNavController().navigate(action)
                ToastUtils.showToast(requireContext(),"Friend : see more")
            }else{
              //  showInfoDialog(friendID)

                val action = FriendsDirections.actionFriendsFragmentToMyFocus(id = friend.id!!, name =  friend.firstname +" "+ friend.lastname)
                findNavController().navigate(action)
             //   ToastUtils.showToast(requireContext(),"Friend :$pos")
            }
        }else{
            if(pos == 11){
                val action = FriendsDirections.actionFriendsFragmentToMoreFriends(isFriends)
                findNavController().navigate(action)
               // ToastUtils.showToast(requireContext(),"otherPeople :see more")
            }else{
                showInfoDialog(friend , pos)
              //  ToastUtils.showToast(requireContext(),"OtherPeople :$pos")
            }
        }

    }


    override fun onAttach(contex: Context) {
        super.onAttach(contex)
        mContext = contex as MainActivity

    }


    private fun showInfoDialog(friend: FriendResponse.Data, pos: Int) {
        AwesomeInfoDialog(requireContext())
            .setTitle(R.string.friend_confirmation)
            .setMessage("Do you want to Invite ${friend.firstname} ${friend.lastname} to become your friend?")
            .setColoredCircle(R.color.purple_200)
            .setDialogIconAndColor( com.awesomedialog.blennersilva.awesomedialoglibrary.R.drawable.ic_notice, R.color.white)
            .setCancelable(true)
            .setPositiveButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_yes_button))
            .setPositiveButtonbackgroundColor(R.color.purple_200)
            .setPositiveButtonTextColor(R.color.white)
            .setNeutralButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_no_button))
            .setNeutralButtonbackgroundColor(R.color.red)
            .setNeutralButtonTextColor(R.color.white)
            .setPositiveButtonClick{
                // send Friend request
               viewModel.sendFriendRequest(request = FriendRequest(userID = GlobalData.user!!.id, friendID = friend.id), friend = friend )
             showInfoMessageDialog()
                otherPeopleList.removeAt(pos)
                otherPeopleAdapter.notifyItemRemoved(pos)
            }
            .setNeutralButtonClick{
              //  ToastUtils.showToast(requireContext(), "no click")
            }
            .show();
    }

    private fun showInfoMessageDialog(){
        AwesomeInfoDialog(requireContext())
            .setTitle(R.string.invitation_sent)
            .setMessage(getString(R.string.friend_invitation_send))
            .setColoredCircle(R.color.purple_200)
            .setDialogIconAndColor( com.awesomedialog.blennersilva.awesomedialoglibrary.R.drawable.ic_notice, R.color.white)
            .setCancelable(true)
            .setPositiveButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_ok_button))
            .setPositiveButtonbackgroundColor(R.color.purple_200)
            .setPositiveButtonTextColor(R.color.white)
            .setPositiveButtonClick{
                // send Friend request

            }

            .show();
    }


}