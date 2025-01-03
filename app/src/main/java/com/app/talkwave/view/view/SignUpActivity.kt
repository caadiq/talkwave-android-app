package com.app.talkwave.view.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.app.talkwave.databinding.ActivitySignupBinding
import com.app.talkwave.model.data.UserData
import com.app.talkwave.model.dto.SignUpDto
import com.app.talkwave.viewmodel.AuthViewModel
import com.app.talkwave.viewmodel.DataStoreViewModel
import com.app.talkwave.viewmodel.DeptViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    private val dataStoreViewModel by viewModels<DataStoreViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private val deptViewModel by viewModels<DeptViewModel>()

    private var name: String = ""
    private var userId: String = ""
    private var password: String = ""
    private var passwordConfirm: String = ""
    private var deptId: Int? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java)).also { finish() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.editName.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.inputName.error = null
                }
            }

            addTextChangedListener {
                binding.inputName.error = null
            }
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

        binding.editPasswordConfirm.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.inputPasswordConfirm.error = null
                }
            }

            addTextChangedListener {
                binding.inputPasswordConfirm.error = null
            }
        }

        binding.editDept.setOnClickListener {
            binding.inputDept.error = null
            deptViewModel.getDeptList()
        }

        binding.btnSignUp.setOnClickListener {
            name = binding.editName.text?.trim().toString()
            userId = binding.editId.text?.trim().toString()
            password = binding.editPassword.text?.trim().toString()
            passwordConfirm = binding.editPasswordConfirm.text?.trim().toString()

            if (name.isEmpty()) {
                binding.inputName.error = "이름을 입력해주세요."
            }

            if (userId.isEmpty()) {
                binding.inputId.error = "아이디를 입력해주세요."
            }

            if (password.isEmpty()) {
                binding.inputPassword.error = "비밀번호를 입력해주세요."
            }

            if (password != passwordConfirm) {
                binding.inputPasswordConfirm.error = "비밀번호가 일치하지 않습니다."
            }

            if (deptId == null) {
                binding.inputDept.error = "부서를 선택해주세요."
            }

            if (name.isNotEmpty() && userId.isNotEmpty() && password.isNotEmpty() && password == passwordConfirm && deptId != null) {
                authViewModel.signUp(SignUpDto(userId, password, name, deptId!!))
            }
        }
    }

    private fun setupViewModel() {
        dataStoreViewModel.apply {
            combine(userId, password).observe(this@SignUpActivity) { (userId, password) ->
                if (userId != null && password != null) {
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java)).also { finish() }
                }
            }
        }

        authViewModel.apply {
            signUp.observe(this@SignUpActivity) {
                signIn(userId, password)
            }

            signIn.observe(this@SignUpActivity) {
                UserData.setUserId(binding.editId.text.toString())
                dataStoreViewModel.saveUserId(binding.editId.text.toString())
                dataStoreViewModel.savePassword(binding.editPassword.text.toString())
            }

            errorMessage.observe(this@SignUpActivity) { message ->
                if (message != null)
                    Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        deptViewModel.deptList.observe(this) { list ->
            DeptBottomSheetDialog(
                list = list,
                onItemClick = { item ->
                    deptId = item.deptId
                    binding.editDept.setText(item.name)
                }
            ).show(supportFragmentManager, "DeptBottomSheetDialog")
        }
    }
}