package com.app.randomchat.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*


fun View.hide(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.disable(){
    isEnabled = false
}

fun View.enabled(){
    isEnabled = true
}

fun Fragment.toast(msg: String?){
    Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
}

fun Activity.toast(msg: String?){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

fun Long.toPrettyDate(): String {
    val nowTime = Calendar.getInstance()
    val neededTime = Calendar.getInstance()
    neededTime.timeInMillis = this

    return if (neededTime[Calendar.YEAR] == nowTime[Calendar.YEAR]) {
        if (neededTime[Calendar.MONTH] == nowTime[Calendar.MONTH]) {
            when {
                neededTime[Calendar.DATE] - nowTime[Calendar.DATE] == 1 -> {
                    //here return like "Tomorrow at 12:00"
                    "Amanhã às " +  SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }
                nowTime[Calendar.DATE] == neededTime[Calendar.DATE] -> {
                    //here return like "Today at 12:00"
                    " Hoje às " +  SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }
                nowTime[Calendar.DATE] - neededTime[Calendar.DATE] == 1 -> {
                    //here return like "Yesterday at 12:00"
                    "Ontem às " +  SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }
                else -> {
                    //here return like "May 31, 12:00"
                    SimpleDateFormat("MMMM d, HH:mm", Locale.getDefault()).format(Date(this))
                }
            }
        } else {
            //here return like "May 31, 12:00"
            SimpleDateFormat("MMMM d, HH:mm", Locale.getDefault()).format(Date(this))
        }
    } else {
        //here return like "May 31 2022, 12:00" - it's a different year we need to show it
        SimpleDateFormat("MMMM dd yyyy, HH:mm", Locale.getDefault()).format(Date(this))
    }
}


fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    return dialog
}

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
