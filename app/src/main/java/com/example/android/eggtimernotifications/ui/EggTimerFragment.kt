/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.databinding.FragmentEggTimerBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class EggTimerFragment : Fragment() {

    private val TOPIC = "breakfast"

    private lateinit var binding: FragmentEggTimerBinding
    private val viewModel: EggTimerViewModel by viewModels()

    private val notificationManager by lazy {
        requireActivity().getSystemService(NotificationManager::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEggTimerBinding.inflate(inflater)

        binding.run {
            eggTimerViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        // TODO: Step 1.7 call create channel
        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )

        // TODO: Step 3.1 create a new channel for FCM
        createChannel(
            getString(R.string.breakfast_notification_channel_id),
            getString(R.string.breakfast_notification_channel_name)
        )

        // TODO: Step 3.4 call subscribe topics on start

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "firebase token: $token")
            Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        // Create channel to show notifications.
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            // TODO: Step 2.4 change importance
            NotificationManager.IMPORTANCE_HIGH
        )
            // TODO: Step 2.6 disable badges for this channel
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description =
            getString(R.string.breakfast_notification_channel_description)

        notificationManager.createNotificationChannel(notificationChannel)

        // TODO: Step 1.6 END create channel
    }

    // TODO: Step 3.3 subscribe to breakfast topic

    companion object {
        private const val TAG = "EggTimberFragment"

        fun newInstance() = EggTimerFragment()
    }
}

