package com.cc.cenntrum.utils

import com.cc.cenntrum.R
import com.ycuwq.datepicker.date.DatePickerDialogFragment

class MyDatePickerDialogFragment : DatePickerDialogFragment() {
    override fun initChild() {
        super.initChild()
       // mDatePicker.setTextColor(application!!.getColor(R.color.btn_color))
        mDatePicker.setSelectedItemTextColor(application!!.getColor(R.color.btn_color))
        mDecideButton.text = "OK"
        mDecideButton.setTextColor(application!!.getColor(R.color.white))
        mCancelButton.setTextColor(application!!.getColor(R.color.white))
        mCancelButton.textSize = 16F
        mDecideButton.textSize = 16F
        mDatePicker.setShowCurtain(false)
    }
}