package com.app.talkwave.view.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.talkwave.databinding.BottomsheetdialogEmoticonBinding
import com.app.talkwave.model.dto.EmoticonListDto
import com.app.talkwave.view.adapter.EmoticonListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmoticonBottomSheetDialog(
    private val list: List<EmoticonListDto> = emptyList(),
    private val onItemClick: (EmoticonListDto) -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomsheetdialogEmoticonBinding? = null
    private val binding get() = _binding!!

    private val emoticonListAdapter = EmoticonListAdapter()

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
        _binding = BottomsheetdialogEmoticonBinding.inflate(inflater, container, false)
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
        binding.recyclerView.adapter = emoticonListAdapter

        emoticonListAdapter.apply {
            setItemList(list)
            setOnItemClickListener { item, _ ->
                onItemClick(item)
                dismiss()
            }
        }
    }
}