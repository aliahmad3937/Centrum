package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog
import com.cc.cenntrum.R
import com.cc.cenntrum.adapters.MoreFriendsAdapter
import com.cc.cenntrum.callbacks.FriendCallback
import com.cc.cenntrum.databinding.FragmentMoreFriendsBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.FriendRequest
import com.cc.cenntrum.models.FriendResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.GlobalData
import com.cc.cenntrum.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreFriends : Fragment(), FriendCallback {
    private lateinit var binding: FragmentMoreFriendsBinding

    //  lateinit var list: MutableList<FriendResponse.Data>
    lateinit var adapter: MoreFriendsAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var mContext: MainActivity
    private val args: MoreFriendsArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_more_friends, container, false)
        viewModel = ViewModelProvider(mContext).get(MainViewModel::class.java)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.icSearch.setOnClickListener {
            if (binding.searchView.visibility == View.VISIBLE) {
                binding.searchView.visibility = View.GONE
            } else {
                binding.searchView.visibility = View.VISIBLE
            }
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.getFilter(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })



        setAdapter()


        return binding.root
    }


    private fun setAdapter() {
        if (args.friend) {
            binding.btnMore.text = getString(R.string.friends)
            adapter = MoreFriendsAdapter(
                mContext,
                ((viewModel.friendResponse.value as APIResponse.Success<FriendResponse>).data).data,
                isFriend = args.friend,
                this
            )
            binding.recyclerMore.layoutManager = GridLayoutManager(mContext, 4)
            binding.recyclerMore.adapter = adapter

        } else {
            binding.btnMore.text = getString(R.string.all_other_peoples)
            adapter = MoreFriendsAdapter(
                mContext,
                ((viewModel.otherPeopleResponse.value as APIResponse.Success<FriendResponse>).data).data,
                isFriend = args.friend,
                this
            )
            binding.recyclerMore.layoutManager = GridLayoutManager(mContext, 4)
            binding.recyclerMore.adapter = adapter
        }

    }


    private fun showInfoDialog(friend: FriendResponse.Data, pos: Int) {
        AwesomeInfoDialog(requireContext())
            .setTitle(R.string.friend_confirmation)
            .setMessage("Do you want to Invite ${friend.firstname} ${friend.lastname} to become your friend?")
            .setColoredCircle(R.color.purple_200)
            .setDialogIconAndColor(
                com.awesomedialog.blennersilva.awesomedialoglibrary.R.drawable.ic_notice,
                R.color.white
            )
            .setCancelable(true)
            .setPositiveButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_yes_button))
            .setPositiveButtonbackgroundColor(R.color.purple_200)
            .setPositiveButtonTextColor(R.color.white)
            .setNegativeButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_no_button))
            .setNegativeButtonbackgroundColor(com.awesomedialog.blennersilva.awesomedialoglibrary.R.color.dialogErrorBackgroundColor)
            .setNegativeButtonTextColor(R.color.white)
            .setPositiveButtonClick {
                viewModel.sendFriendRequest(
                    request = FriendRequest(
                        userID = GlobalData.user!!.id,
                        friendID = friend.id
                    ), friend = friend
                )
                adapter.list.removeAt(pos)
                adapter.templist.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                showInfoMessageDialog()
            }
            .setNegativeButtonClick {
                //     ToastUtils.showToast(requireContext(), "no click")
            }
            .show();
    }


    private fun showInfoMessageDialog() {
        AwesomeInfoDialog(requireContext())
            .setTitle(R.string.invitation_sent)
            .setMessage(getString(R.string.friend_invitation_send))
            .setColoredCircle(R.color.purple_200)
            .setDialogIconAndColor(
                com.awesomedialog.blennersilva.awesomedialoglibrary.R.drawable.ic_notice,
                R.color.white
            )
            .setCancelable(true)
            .setPositiveButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_ok_button))
            .setPositiveButtonbackgroundColor(R.color.purple_200)
            .setPositiveButtonTextColor(R.color.white)
            .setPositiveButtonClick {
                // send Friend request

            }

            .show();
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onFriendClick(pos: Int, friend: FriendResponse.Data, isFriends: Boolean) {
        if (isFriends) {
            //  ToastUtils.showToast(requireContext(),"Friend :$pos")
            val action = MoreFriendsDirections.actionMoreFriendsToMyFocus(
                id = friend.id!!,
                name = friend.firstname + " " + friend.lastname
            )
            findNavController().navigate(action)

        } else {
            showInfoDialog(friend, pos)
            //   ToastUtils.showToast(requireContext(),"OtherPeople :$pos")

        }
    }

}


