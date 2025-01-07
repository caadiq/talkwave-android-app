package com.app.talkwave.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.talkwave.databinding.FragmentHomeBinding
import com.app.talkwave.view.adapter.HomeDeptListAdapter
import com.app.talkwave.viewmodel.DeptViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val deptViewModel by activityViewModels<DeptViewModel>()

    private val homeDeptListAdapter = HomeDeptListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            deptViewModel.getDeptList()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView. adapter = homeDeptListAdapter
    }

    private fun setupViewModel() {
        deptViewModel.apply {
            deptList.observe(viewLifecycleOwner) { list ->
                homeDeptListAdapter.setItemList(list)
            }
        }
    }
}