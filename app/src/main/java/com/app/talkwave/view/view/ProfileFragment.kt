package com.app.talkwave.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.talkwave.databinding.FragmentProfileBinding
import com.app.talkwave.model.data.UserData
import com.app.talkwave.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                UserData.clearUserId()
                dataStoreViewModel.clearUserId()
                dataStoreViewModel.clearPassword()
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}