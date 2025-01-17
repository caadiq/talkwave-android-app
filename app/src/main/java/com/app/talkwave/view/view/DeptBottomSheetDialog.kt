package com.app.talkwave.view.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.talkwave.databinding.BottomsheetdialogDeptBinding
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.view.adapter.DeptListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeptBottomSheetDialog(
    private val list: List<DeptListDto> = emptyList(),
    private val onItemClick: (DeptListDto) -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomsheetdialogDeptBinding? = null
    private val binding get() = _binding!!

    private val deptListAdapter = DeptListAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet = (dialogInterface as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.45).toInt()
                sheet.layoutParams.height = resources.displayMetrics.heightPixels
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                behavior.isDraggable = true
            }
        }

        return dialog
    }

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