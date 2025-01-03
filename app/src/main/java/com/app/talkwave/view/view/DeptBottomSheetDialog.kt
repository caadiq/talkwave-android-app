package com.app.talkwave.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.talkwave.databinding.BottomsheetdialogDeptBinding
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.view.adapter.DeptListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeptBottomSheetDialog(
    private val list: List<DeptListDto> = emptyList(),
    private val onItemClick: (DeptListDto) -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomsheetdialogDeptBinding? = null
    private val binding get() = _binding!!

    private val deptListAdapter = DeptListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomsheetdialogDeptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = deptListAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        deptListAdapter.apply {
            setItemList(list)
            setOnItemClickListener { item, _ ->
                onItemClick(item)
                dismiss()
            }
        }
    }
}