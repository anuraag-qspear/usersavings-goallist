package com.sockytest.network.model

data class Job(
    val id: Int,
    val maxHoursPerWeek: Int,
    val minHoursPerWeek: Int,
    val name: String,
    val weekDayHourlyWage: Int,
    val weekEndDayHourlyWage: Int
)