package com.cc.cenntrum.ui.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentLoginBinding
import com.cc.cenntrum.models.APIResponse
import com.cc.cenntrum.models.User
import com.cc.cenntrum.models.UserResponse
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.Constants
import com.cc.cenntrum.utils.GlobalData.user
import com.cc.cenntrum.utils.LoadingUtils
import com.cc.cenntrum.utils.SavedPreference
import com.cc.cenntrum.utils.SavedPreference.getToken
import com.cc.cenntrum.utils.ToastUtils
import com.cc.cenntrum.utils.ToastUtils.showToast
import com.cc.cenntrum.viewmodels.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class Login : Fragment() {
    // Initialize variable
    private lateinit var binding: FragmentLoginBinding
    private val TAG = "Login"
    private var email: String = ""
    private var password: String = ""
    private lateinit var mContext: Context
   private val viewModel: MainViewModel by viewModels()

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Assign variable
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
// getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient= GoogleSignIn.getClient(requireContext(),gso)
// initialize the firebaseAuth variable
        firebaseAuth= FirebaseAuth.getInstance()







        Log.v(Constants.TAG, "${Constants.BASE_URL}")

        binding.textView5.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgetPassword)
        }

        binding.tvDont.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signUp)
        }
        binding.imageView2.setOnClickListener {
           signInGoogle()
        }

        binding.signinButton.setOnClickListener {
            email =binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString()
           if(isValid()){
                  val user = User(email = email, password = password , token = getToken(requireContext()))
               viewModel.loginUser(user)
           }
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

                    if (response.status==true)
                    {
                        showToast(requireContext(), getString(R.string.login_successfuly))
                        user = response.data!!
                        SavedPreference.setUserData(requireContext(), data = response.data!!)
                          findNavController().navigate(R.id.action_login_to_homeFragment)
                        /// action perform

                    }else{
                        showToast(requireContext(),response.message.toString())
                    }

                }
                is APIResponse.Starting -> {}
            }
        })

        // setListeners()


        return binding.root
    }
//
//    private fun setListeners() {
//        binding.signinButton.setOnClickListener {
//
//            email=binding.editTextTextEmailAddress.text.toString()
//            password=binding.editTextTextPassword.text.toString()
//
//            if (isValid())
//            {
////                loginUser()
//                viewModel.loginUser(email,password)
//            }
//        }
//    }


    private fun isValid(): Boolean {

        var result = true
        if (TextUtils.isEmpty(email)) {
            binding.editTextTextEmailAddress.error = getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(password)) {
            binding.editTextTextPassword.error = getString(R.string.required)
            result = false
        } else if (!isEmailValid(email)) {
            binding.editTextTextEmailAddress.error = getString(R.string.invalid_email)
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


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)

            }
        }

    // signInGoogle() function
    private  fun signInGoogle(){


       // startActivityForResult(signInIntent,Req_Code)
        resultLauncher.launch(mGoogleSignInClient.signInIntent)
    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){
         ToastUtils.showToast(requireContext(),e.message.toString())
        }
    }
    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                ToastUtils.showToast(requireContext(),"Signin successfull")
//                val bundle = Bundle()
//                bundle.putString(ARG_PARAM1,account.displayName.toString())
//                bundle.putString(ARG_PARAM2,account.pho)
//                bundle.putString(ARG_PARAM3,account.displayName.toString())
//                bundle.putString(ARG_PARAM4,account.displayName.toString())
//                bundle.putString(ARG_PARAM5,account.displayName.toString())
//                findNavController().navigate(R.id.action_login_to_signUp)
                findNavController().navigate(R.id.action_login_to_homeFragment)

                Log.v("TAG","${account.displayName} 1 ${account.givenName} 2 ${account.familyName} 3 ${account.email} ${account.email} ")
            }
        }
    }
    override fun onStart() {
        super.onStart()
//        if(GoogleSignIn.getLastSignedInAccount(requireContext())!=null){
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }


    override fun onResume() {
        super.onResume()
        SavedPreference.getUserID(mContext)?.let {
            if(it != 0)
                findNavController().navigate(R.id.action_login_to_homeFragment)
        }
    }

    fun getDeviceToken(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//            .addOnSuccessListener(this) { instanceIdResult ->
//                val newToken: String = instanceIdResult.getToken()
//                Log.e("newToken", newToken)
//            }
    }

}