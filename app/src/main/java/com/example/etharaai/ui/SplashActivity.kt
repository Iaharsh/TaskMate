package com.example.etharaai.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.etharaai.databinding.ActivitySplashBinding
import com.example.etharaai.ui.auth.LoginActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Simulate progress using lifecycleScope
        lifecycleScope.launchWhenStarted {
            while (progressStatus < 100) {
                delay(30)
                progressStatus += 1
                binding.progressBar.progress = progressStatus
                binding.percentageText.text = "$progressStatus%"
                if (progressStatus == 100) {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}
