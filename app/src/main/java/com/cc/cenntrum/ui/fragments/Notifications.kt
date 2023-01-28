package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure
import com.cc.cenntrum.R
import com.cc.cenntrum.adapters.NotificationAdapter
import com.cc.cenntrum.callbacks.NotificationCallback
import com.cc.cenntrum.databinding.FragmentNotificationsBinding
import com.cc.cenntrum.models.*
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.GlobalData
import com.cc.cenntrum.utils.LoadingUtils
import com.cc.cenntrum.utils.SavedPreference
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.viewmodels.MainViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notifications : Fragment(), NotificationCallback {
    lateinit var binding: FragmentNotificationsBinding
    var notificationList: MutableList<FriendResponse.Data> = arrayListOf()
    lateinit var adapter: NotificationAdapter
    lateinit var mContext: MainActivity

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(mContext).get(MainViewModel::class.java)
        viewModel.getFriendRequestList(SavedPreference.getUserID(mContext))

        setAdapter()

        viewModel.friendRequestList.observe(viewLifecycleOwner, Observer {
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

                        if (response.data.isNotEmpty()) {
                            adapter.updateAdapter(response.data)
                            mContext.showNotificationBadge(response.data.size)
                            binding.recyler.visibility = View.VISIBLE
                            binding.noNotifi.visibility = View.GONE
                        } else {
                            binding.recyler.visibility = View.GONE
                            binding.noNotifi.visibility = View.VISIBLE
                            mContext.hideNotificationBadge()
                        }

                    } else {
                        ToastUtils.showToast(requireContext(), response.message.toString())
                    }

                }
                is APIResponse.Starting -> {}
            }
        })


        return binding.root
    }

    private fun setAdapter() {
        adapter = NotificationAdapter(activity!!, notificationList, this)
        binding.recyler.layoutManager = LinearLayoutManager(activity)
        binding.recyler.adapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }


    override fun onNotificationClick(pos: Int, notif: FriendResponse.Data, isAccept: Boolean) {
        var mail = SavedPreference.getUserEmail(mContext)
        if (isAccept) {
            // accept friend request
            viewModel.acceptFriendRequest(
                friend = FriendRequest(
                    userID = SavedPreference.getUserID(
                        mContext
                    ), friendID = notif.id
                ), token = notif.deviceToken.toString(), email = mail.substringBefore("@")
            )
            successDialogue("Accepted Successfully!")
            // this will on subscription on receiver side with sender email
            subscribeTopic(notif.email!!.substringBefore("@"))
            if (adapter.list.isEmpty()) {
                mContext.showNotificationBadge(adapter.list.size)
            } else {
                mContext.hideNotificationBadge()
            }
            adapter.list.removeAt(pos)
            adapter.notifyDataSetChanged()
        } else {
            // reject friend request
            successDialogue("Rejected Successfully!")
            if (adapter.list.isEmpty()) {
                mContext.showNotificationBadge(adapter.list.size)
            } else {
                mContext.hideNotificationBadge()
            }
            adapter.list.removeAt(pos)
            adapter.notifyDataSetChanged()
            viewModel.rejectFriendRequest(
                friend = FriendRequest(SavedPreference.getUserID(mContext)),
                token = notif.deviceToken.toString(),
                email = mail.substringBefore("@")
            )
        }
    }

    private fun successDialogue(msg: String) {
        AwesomeSuccessDialog(mContext)
            .setTitle("Friend Request")
            .setMessage(msg)
            .setColoredCircle(R.color.purple_200)
            .setDialogIconAndColor(
                com.awesomedialog.blennersilva.awesomedialoglibrary.R.drawable.ic_success,
                R.color.white
            )
            .setCancelable(true)
            .setPositiveButtonText(getString(com.awesomedialog.blennersilva.awesomedialoglibrary.R.string.dialog_ok_button))
            .setPositiveButtonbackgroundColor(R.color.purple_200)
            .setPositiveButtonTextColor(R.color.white)
            .setPositiveButtonClick(object : Closure {
                override fun exec() {
                    //click
                }
            }).show()


    }

    // TODO: Step 3.3 subscribe to breakfast topic
    private fun subscribeTopic(topic: String) {
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Subscribes"
                if (!task.isSuccessful) {
                    msg = "Not Subscribed! :${task.exception.toString()}"
                }
                Log.v("TAG9", "firebase Messaging :$msg")
                //  ToastUtils.showToast(mContext, msg)
            }
        // [END subscribe_topics]
    }


}