package com.example.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.shoppinglist.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)

        anim.setAnimationListener(
            object: Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    var intent = Intent()
                    intent.setClass(this@SplashActivity, ListActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAnimationStart(animation: Animation?) {}
            }
        )

        binding.imgSplash.startAnimation(anim)

    }
}