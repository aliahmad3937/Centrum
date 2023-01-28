package com.cc.cenntrum.ui.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.cc.cenntrum.R
import com.cc.cenntrum.databinding.FragmentIncentivesBinding
import com.cc.cenntrum.databinding.FragmentTermsConditionsBinding
import com.cc.cenntrum.ui.activities.MainActivity
import com.cc.cenntrum.utils.ToastUtils


class TermsConditions : Fragment() {
    private lateinit var binding: FragmentTermsConditionsBinding
    private lateinit var mContext: MainActivity

    val str ="<pre>These Terms and Conditions (\"Terms\", \"Terms and Conditions\") govern your relationship with Cenntrum mobile application (the \"Service\") operated by Cenntrum LLC. (\"us\", \"we\", or \"our\").\n" +
            "Please read these Terms and Conditions carefully before using our Cenntrum mobile application (the \"Service\").\n" +
            "Your access to and use of the Service is conditioned on your acceptance of and compliance with these Terms. These Terms apply to all visitors, users and others who access or use the Service.\n" +
            "By accessing or using the Service you agree to be bound by these Terms. If you disagree with any part of the terms, then you may not access the Service.\n" +
            "The Service is available on mobile devices. Do not use the Service in a way that distracts you and prevents you from obeying traffic or safety laws.\n" +
            "<br><br><h4>Purchases</h4><br>" +
            "If you wish to purchase any product or service made available through the Service (\"Purchase\"), you may be asked to supply certain information relevant to your Purchase including, without limitation, your credit card number, the expiration date of your credit card, your billing address, and your shipping information. \n" +
            "You represent and warrant that: (i) you have the legal right to use any credit card(s) or other payment method(s) in connection with any Purchase; and that (ii) the information you supply to us is true, correct and complete.\n" +
            "By submitting such information, you grant us the right to provide the information to third parties for purposes of facilitating the completion of Purchases.\n" +
            "We reserve the right to refuse or cancel your order at any time for certain reasons including but not limited to (i) product or service availability, (ii) errors in the description or price of the product or service, (iii) error in your order or (iv) other reasons.\n" +
            "We reserve the right to refuse or cancel your order if fraud or an unauthorized or illegal transaction is suspected.\n" +
            "<br><h4><br><h4>Software in our Service</h4><br></h4><br>"+
            "When a Service requires or includes downloadable software, Cenntrum gives you a personal, worldwide, royalty-free, non-assignable and non-exclusive license to use the software provided to you by Cenntrum as part of the Service. This license is for the sole purpose of enabling you to use and enjoy the benefit of the Service as provided by Cenntrum in the manner permitted by these terms. You may not copy, modify, distribute, sell or lease any part of our Service or included software, nor may you reverse engineer or attempt to extract the source code of that software, unless laws prohibit those restrictions or you have our written permission.\n" +
            "<br><br><h4>Availability, Errors and Inaccuracies</h4><br>" +
            "We are constantly updating our offerings of products and services on the Service. The products or services available on our Service may be mispriced, described inaccurately, or unavailable, and we may experience delays in updating information on the Service and in our advertising on other web sites.\n" +
            "We cannot and do not guarantee the accuracy or completeness of any information, including prices, product images, specifications, availability, and services. \n" +
            "We reserve the right to change or update information and to correct errors, inaccuracies, or omissions at any time without prior notice." +
            "<br><h4><br><h4>Contests, Sweepstakes and Promotions</h4><br></h4><br>" +
            "Any contests, sweepstakes or other promotions (collectively, \"Promotions\") made available through the Service may be governed by rules that are separate from these Terms. If you participate in any Promotions, please review the applicable rules as well as our Privacy Policy. If the rules for a Promotion conflict with these Terms, the Promotion rules will apply." +
            "<br><h4><br><h4>Content</h4><br></h4><br>"+
            "Our Service allows you to post, link, store, share and otherwise make available certain information, text, graphics, videos, or other material (\"Content\"). You are responsible for the Content that you post to the Service, including its legality, reliability, and appropriateness.\n" +
            "By posting Content to the Service, you grant us the right and license to use, modify, publicly perform, publicly display, reproduce, and distribute such Content on and through the Service. You retain any and all of your rights to any Content you submit, post or display on or through the Service and you are responsible for protecting those rights. \n" +
            "You agree that this license includes the right for us to make your Content available to other users of the Service, who may also use your Content subject to these Terms.\n" +
            "You represent and warrant that: (i) the Content is yours (you own it) or you have the right to use it and grant us the rights and license as provided in these Terms, and (ii) the posting of your Content on or through the Service does not violate the privacy rights, publicity rights, copyrights, contract rights or any other rights of any person.\n" +
            "If you submit feedback or suggestions about our Service, we may use your feedback or suggestions without obligation to you.\n" +
            "<br><h4><br><h4>Accounts</h4><br></h4><br>"+
            "You may need an account in order to use our Service.\n" +
            "When you create an account with us, you must provide us information that is accurate, complete, and current at all times. Failure to do so constitutes a breach of the Terms, which may result in immediate termination of your account on our Service.\n" +
            "You are responsible for safeguarding the password that you use to access the Service and for any activities or actions under your password, whether your password is with our Service or a third-party service.\n" +
            "You agree not to disclose your password to any third party. You must notify us immediately upon becoming aware of any breach of security or unauthorized use of your account.\n" +
            "You may not use as a username the name of another person or entity or that is not lawfully available for use, a name or trade mark that is subject to any rights of another person or entity other than you without appropriate authorization, or a name that is otherwise offensive, vulgar or obscene.\n" +
            "<br><h4><br><h4>Copyright Policy</h4><br></h4><br>"+
            "We respect the intellectual property rights of others. It is our policy to respond to any claim that Content posted on the Service infringes the copyright or other intellectual property infringement (\"Infringement\") of any person.\n" +
            "\n" +
            "If you are a copyright owner, or authorized on behalf of one, and you believe that the copyrighted work has been copied in a way that constitutes copyright infringement that is taking place through the Service, you must submit your notice in writing to the attention of \"Copyright Infringement\" of support@cenntrum.com and include in your notice a detailed description of the alleged Infringement.\n" +
            "You may be held accountable for damages (including costs and attorneys' fees) for misrepresenting that any Content is infringing your copyright.\n"+
            "<br><h4><br><h4>Intellectual Property</h4><br></h4><br>"+
            "The Service and its original content (excluding Content provided by users), features and functionality are and will remain the exclusive property of Cenntrum LLC and its licensors. The Service is protected by copyright, trademark, and other laws of both the United States of America Taiwan and foreign countries. Our trademarks and trade dress may not be used in connection with any product or service without the prior written consent of Cenntrum LLC."+
            "<br><h4><br><h4>Links To Other Web Sites</h4><br></h4><br>"+
            "Our Service may contain links to third-party web sites or services that are not owned or controlled by Cenntrum LLC.\n" +
            "Cenntrum LLC has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that Cenntrum LLC shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with use of or reliance on any such content, goods or services available on or through any such web sites or services.\n" +
            "We strongly advise you to read the terms and conditions and privacy policies of any third-party web sites or services that you visit.\n"+
            "<br><h4><br><h4>Termination</h4><br></h4><br>"+
            "We may terminate or suspend your account immediately, without prior notice or liability, for any reason whatsoever, including without limitation if you breach the Terms.\n" +
            "Upon termination, your right to use the Service will immediately cease. If you wish to terminate your account, you may simply discontinue using the Service.\n"+
            "<br><h4><br><h4>Limitation of Liability</h4><br></h4><br>"+
            "In no event shall Cenntrum LLC nor its directors, employees, partners, agents, suppliers, or affiliates, be liable for any indirect, incidental, special, consequential or punitive damages, including without limitation, loss of profits, data, use, goodwill, or other intangible losses, resulting from i) your access to or use of or inability to access or use the Service; (ii) any conduct or content of any third party on the Service; (iii) any content obtained from the Service; and (iv) unauthorized access, use or alteration of your transmissions or content, whether based on warranty, contract, tort (including negligence) or any other legal theory, whether or not we have been informed of the possibility of such damage, and even if a remedy set forth herein is found to have failed of its essential purpose.\n" +
            "To the extent permitted by law, the total liability of Cenntrum and its suppliers and distributors for any claims under these terms, including for any implied warranties, is limited to the amount that you paid us to use the Service (or, if we choose, to supplying you with the Service again).\n" +
            "We recognize that in some countries, you might have legal rights as a consumer. If you are using the Service for a personal purpose, then nothing in these terms or any additional terms limits any consumers’ legal rights which may not be waived by contract.\n" +
            "<br><h4><br><h4>Disclaimer</h4><br></h4><br>"+
            "Your use of the Service is at your sole risk. The Service is provided on an \"AS IS\" and \"AS AVAILABLE\" basis. The Service is provided without warranties of any kind, whether express or implied, including, but not limited to, implied warranties of merchantability, fitness for a particular purpose, non-infringement or course of performance.\n" +
            "Cenntrum LLC its subsidiaries, affiliates, and its licensors do not warrant that a) the Service will function uninterrupted, secure or available at any particular time or location; b) any errors or defects will be corrected; c) the Service is free of viruses or other harmful components; or d) the results of using the Service will meet your requirements.\n"+
            "<br><h4><br><h4>Governing Law</h4><br></h4><br>" +
            "These Terms shall be governed and construed in accordance with the laws of the United States of America, without regard to its conflict of law provisions.\n" +
            "Our failure to enforce any right or provision of these Terms will not be considered a waiver of those rights. If any provision of these Terms is held to be invalid or unenforceable by a court, the remaining provisions of these Terms will remain in effect. These Terms constitute the entire agreement between us regarding our Service, and supersede and replace any prior agreements we might have between us regarding the Service.\n" +
            "<br><h4><br><h4>Changes</h4><br></h4><br>" +
            "We reserve the right, at our sole discretion, to modify or replace these Terms at any time. If a revision is material we will try to provide at least 30 days notice prior to any new terms taking effect. However, changes addressing new functions for a Service or changes made for legal reasons will be effective immediately. What constitutes a material change will be determined at our sole discretion.\n" +
            "By continuing to access or use our Service after those revisions become effective, you agree to be bound by the revised terms. If you do not agree to the new terms, please stop using the Service.\n" +
            "If there is any inconsistency between these terms and the additional terms, the additional terms will prevail to the extent of the inconsistency.\n" +
            "<br><h4><br><h4>Contact Us</h4><br></h4><br>" +
            "If you have any questions about these Terms, please contact us." +
            "</pre>"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater , R.layout.fragment_terms_conditions, container, false)
        binding.tvWritten.movementMethod = ScrollingMovementMethod()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvWritten.text = Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvWritten.text = Html.fromHtml(str)
        }


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