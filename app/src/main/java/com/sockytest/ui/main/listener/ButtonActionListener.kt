package com.sockytest.ui.main.listener

interface ButtonActionListener {
    fun onEditClicked(position: Int)
    fun onUpdateClicked(position: Int, newSavingsGoal: Long, newSavingsPercentage : Long)
    fun onCancelClicked(position: Int)
}