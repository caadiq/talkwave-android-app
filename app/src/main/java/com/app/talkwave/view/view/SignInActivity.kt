package com.app.talkwave.view.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.app.talkwave.databinding.ActivitySigninBinding
import com.app.talkwave.model.data.UserData
import com.app.talkwave.viewmodel.AuthViewModel
import com.app.talkwave.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }

    private val dataStoreViewModel by viewModels<DataStoreViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        splashScreen()
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java)).also { finish() }
        }

        binding.editId.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.inputId.error = null
                }
            }

            addTextChangedListener {
                binding.inputId.error = null
            }
        }

        binding.editPassword.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.inputPassword.error = null
                }
            }

            addTextChangedListener {
                binding.inputPassword.error = null
            }
        }
    }

    private fun setupViewModel() {
        authViewModel.apply {
            signIn.observe(this@SignInActivity) {
                UserData.setUserId(binding.editId.text.toString())
                dataStoreViewModel.saveUserId(binding.editId.text.toString())
                dataStoreViewModel.savePassword(binding.editPassword.text.toString())
                startActivity(Intent(this@SignInActivity, MainActivity::class.java)).also { finish() }
            }

            errorCode.observe(this@SignInActivity) { code ->
                if (code == null)
                    return@observe

                when (code) {
                    400 -> Toast.makeText(this@SignInActivity, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this@SignInActivity, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signIn() {
        val userId = binding.editId.text.toString()
        val password = binding.editPassword.text.toString()

        if (userId.isEmpty()) {
            binding.inputId.error = "아이디를 입력해주세요."
        }

        if (password.isEmpty()) {
            binding.inputPassword.error = "비밀번호를 입력해주세요."
            return
        }

        authViewModel.signIn(userId, password)
    }

    private fun splashScreen() {
        splashScreen.setKeepOnScreenCondition { true }

        lifecycleScope.launch {
            val userId = dataStoreViewModel.userIdFlow.first()
            val password = dataStoreViewModel.passwordFlow.first()

            if (!userId.isNullOrEmpty() && !password.isNullOrEmpty()) {
                authViewModel.autoSignIn(userId, password)
                UserData.setUserId(userId)
            } else {
                splashScreen.setKeepOnScreenCondition { false }
            }
        }

        authViewModel.autoSignIn.observe(this) {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java)).also { finish() }
            splashScreen.setKeepOnScreenCondition { false }
        }
    }
}