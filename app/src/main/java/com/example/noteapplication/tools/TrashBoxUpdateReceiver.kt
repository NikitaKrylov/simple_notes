package com.example.noteapplication.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.noteapplication.MainActivity

class TrashBoxUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(MainActivity.LOG_TAG, "onReceive")
    }

}