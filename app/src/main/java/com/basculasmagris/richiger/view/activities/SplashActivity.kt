package com.basculasmagris.richiger.view.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ActivitySplashBinding
import com.basculasmagris.richiger.model.entities.*
import com.basculasmagris.richiger.viewmodel.*

sealed class MergedLocalData
data class EstablishmentData(val establishments: MutableList<Establishment>): MergedLocalData()
data class CorralData(val corrals: MutableList<Corral>): MergedLocalData()
data class ProductData(val products: MutableList<Product>): MergedLocalData()
data class MixerData(val mixers: MutableList<Mixer>): MergedLocalData()
data class DietData(val diets: MutableList<Diet>): MergedLocalData()
data class DietProductDetailData(val dietProductsDetail: MutableList<DietProductDetail>): MergedLocalData()
data class DietProductData(val dietProducts: MutableList<DietProduct>): MergedLocalData()
data class RoundData(val rounds: MutableList<Round>): MergedLocalData()
data class RoundCorralDetailData(val roundCorralDetail: MutableList<RoundCorralDetail>): MergedLocalData()
data class RoundCorralData(val roundCorrals: MutableList<RoundCorral>): MergedLocalData()

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sSplashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(sSplashBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }

        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        sSplashBinding.tvAppName.animation = splashAnimation

        splashAnimation.setAnimationListener(object :
            Animation.AnimationListener {

            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }, 1000)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }
}