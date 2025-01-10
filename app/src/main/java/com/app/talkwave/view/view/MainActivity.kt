package com.app.talkwave.view.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.talkwave.R
import com.app.talkwave.databinding.ActivityMainBinding
import com.app.talkwave.viewmodel.MainFragmentType
import com.app.talkwave.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupFragment()
        }
        setupBottomNavigation()
        setupViewModel()
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(binding.containerView.id, HomeFragment(), MainFragmentType.HOME.tag)
            add(binding.containerView.id, ChatFragment(), MainFragmentType.CHAT.tag)
            add(binding.containerView.id, ProfileFragment(), MainFragmentType.PROFILE.tag)
            add(binding.containerView.id, SettingsFragment(), MainFragmentType.SETTINGS.tag)
            commit()
        }

        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.beginTransaction().apply {
            hide(supportFragmentManager.findFragmentByTag(MainFragmentType.CHAT.tag)!!)
            hide(supportFragmentManager.findFragmentByTag(MainFragmentType.PROFILE.tag)!!)
            hide(supportFragmentManager.findFragmentByTag(MainFragmentType.SETTINGS.tag)!!)
            commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> mainViewModel.setCurrentFragment(0)
                R.id.chat -> mainViewModel.setCurrentFragment(1)
                R.id.profile -> mainViewModel.setCurrentFragment(2)
                R.id.settings -> mainViewModel.setCurrentFragment(3)
            }
            true
        }
    }

    private fun setupViewModel() {
        mainViewModel.currentFragmentType.observe(this) { fragmentType ->
            val currentFragment = supportFragmentManager.findFragmentByTag(fragmentType.tag)
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                supportFragmentManager.fragments.forEach { fragment ->
                    if (fragment == currentFragment)
                        show(fragment)
                    else
                        hide(fragment)
                }
            }.commit()

            binding.bottomNav.selectedItemId = when (fragmentType) {
                MainFragmentType.HOME -> R.id.home
                MainFragmentType.CHAT -> R.id.chat
                MainFragmentType.PROFILE -> R.id.profile
                MainFragmentType.SETTINGS -> R.id.settings
                else -> R.id.home
            }
        }
    }
}