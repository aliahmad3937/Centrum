package com.cc.cenntrum.utils


object TimerUtils {


    fun timeFormater(time:Float):String {
        var secs:Int = (time / 1000).toInt()
        var mins:Int = ((time / 1000) / 60).toInt()
        var hrs:Int = (((time / 1000) / 60) / 60).toInt() /* Convert the seconds to String * and format to ensure it has * a leading zero when required */
        secs = secs % 60
        var seconds:String = secs.toString()

        if (secs == 0) {
            seconds = "00"
        }

        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds
        } /* Convert the minutes to String and format the String */
        mins = mins % 60
        var minutes:String = mins.toString()
        if (mins == 0) {
            minutes = "00"
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes
        } /* Convert the hours to String and format the String */
        var hours:String = hrs.toString()
        if (hrs == 0) {
            hours = "00"
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours
        }

        return hours + ":" + minutes + ":" + seconds
//        String milliseconds = String.valueOf((long) time);
//        if (milliseconds.length() == 2) {
//            milliseconds = "0" + milliseconds;
//        }
//        if (milliseconds.length() <= 1) {
//            milliseconds = "00";
//        }
//        milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2); /* Setting the timer text to the elapsed time */
    }

}