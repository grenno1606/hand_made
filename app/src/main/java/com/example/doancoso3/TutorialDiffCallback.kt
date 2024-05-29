package com.example.doancoso3

import androidx.recyclerview.widget.DiffUtil
import com.example.dacs3_ns_22ns082.Tutorial


class TutorialDiffCallback(
    private val oldList: List<Tutorial>,
    private val newList: List<Tutorial>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].tutorialId == newList[newItemPosition].tutorialId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}