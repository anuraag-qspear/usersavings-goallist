package com.sockytest.ui.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sockytest.network.model.Employee
import com.sockytest.network.model.Job
import kotlin.math.log

class EmployeeData(private val employee: Employee) {
    var editMode: Boolean = false
    val usedJobList: MutableList<Job> = mutableListOf<Job>()
    var goal = employee.goal
    var savingPerWeek = employee.savingPerWeek

    val id = employee.id

    val displayName = employee.name[0].toUpperCase() + ". " + employee.lastName

    val picture = employee.picture

    var yearlyWage = employee.weekDayHours + employee.weekEndHours

    @RequiresApi(Build.VERSION_CODES.N)
    fun getSavingsPerWeek(jobsList: ArrayList<Job>,employeeData: EmployeeData): Double {

        var mapJobHours = mutableMapOf<String, Int?>()

        val weekdayCharge: List<Job> =
            jobsList.sortedByDescending { job: Job -> job.weekDayHourlyWage }
        val weekendCharge: List<Job> =
            jobsList.sortedByDescending { job: Job -> job.weekEndDayHourlyWage }
        var hoursLeftWeekday: Int = employee.weekDayHours
        var hoursLeftWeekend: Int = employee.weekEndHours
        var finalJobList: List<Job>

        var isWeekDayListPicked: Boolean
        var wageEarned: Int = 0

        usedJobList.clear()


        if (employee.weekDayHours >= employee.weekEndHours) {
            finalJobList = weekdayCharge
            isWeekDayListPicked = true
        } else {
            finalJobList = weekendCharge
            isWeekDayListPicked = false
        }

        for (job in finalJobList) {
            var previousHoursBurned = mapJobHours.getOrDefault(job.name, 0)
            if (hoursLeftWeekday + hoursLeftWeekend + previousHoursBurned!! >= job.minHoursPerWeek && previousHoursBurned < job.maxHoursPerWeek) {
                usedJobList.add(job)
                if (isWeekDayListPicked) {
                    val minValue = minOf(hoursLeftWeekday, job.maxHoursPerWeek)
                    hoursLeftWeekday = hoursLeftWeekday - minValue

                    addValueToMap(job.name, mapJobHours, minValue)
                    wageEarned = wageEarned + minValue * job.weekDayHourlyWage
                    if (hoursLeftWeekday <= 0) {
                        break
                    }
                } else {
                    val minValue = minOf(hoursLeftWeekend, job.maxHoursPerWeek)
                    hoursLeftWeekend = hoursLeftWeekend - minValue

                    addValueToMap(job.name, mapJobHours, minValue)
                    wageEarned = wageEarned + minValue * job.weekEndDayHourlyWage
                    if (hoursLeftWeekend <= 0) {
                        break
                    }
                }

            }
        }

        if (isWeekDayListPicked) {
            finalJobList = weekendCharge
        } else {
            finalJobList = weekdayCharge

        }

        isWeekDayListPicked = !isWeekDayListPicked
        for (job in finalJobList) {

            var previousHoursBurned = mapJobHours.getOrDefault(job.name, 0)
            if (hoursLeftWeekday + hoursLeftWeekend + previousHoursBurned!! >= job.minHoursPerWeek && previousHoursBurned < job.maxHoursPerWeek) {
                usedJobList.add(job)
                if (isWeekDayListPicked) {
                    val minValue = minOf(hoursLeftWeekday, job.maxHoursPerWeek)
                    hoursLeftWeekday = hoursLeftWeekday - minValue

                    addValueToMap(job.name, mapJobHours, minValue)
                    wageEarned = wageEarned + minValue * job.weekDayHourlyWage
                    if (hoursLeftWeekday <= 0) {
                        break
                    }
                } else {
                    val minValue = minOf(hoursLeftWeekend, job.maxHoursPerWeek)
                    hoursLeftWeekend = hoursLeftWeekend - minValue

                    addValueToMap(job.name, mapJobHours, minValue)
                    wageEarned = wageEarned + minValue * job.weekEndDayHourlyWage
                    if (hoursLeftWeekend <= 0) {
                        break
                    }
                }


            }
        }

        Log.i("earnedWage::", "" + wageEarned)
        this.yearlyWage = wageEarned*52
        return (wageEarned.times(employeeData.savingPerWeek.replace("%", "", true).toDouble() / 100))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addValueToMap(
        mapJobHours: String,
        maxValue: MutableMap<String, Int?>,
        maxValue1: Int
    ) {

        var lastValue = maxValue.get(mapJobHours)
        if (lastValue != null) {
            lastValue = lastValue + maxValue1
            maxValue.replace(mapJobHours, lastValue)
        } else {
            maxValue.put(mapJobHours, maxValue1)
        }
    }

    fun getDaysToAchieveGoal(
        savingsPerWeek: Double,
        employeeData: EmployeeData,
        apyPercent: String
    ): String {

        val apy = apyPercent.replace("%", "", true).toDouble() / 100
        val goal = employeeData.goal
        val savingFourWeek = savingsPerWeek.times(4)

        val numerator =
            ((goal.times(apy).div(savingFourWeek.times(13).times((apy.div(13)).plus(1))))).plus(1)
        val denominator = (apy.div(13)).plus(1)
        val numberOfFourWeeks = log(numerator, 10.0).div(log(denominator, 10.0))
        val numberOfWeeks = numberOfFourWeeks * 4
        val numberOfYears = numberOfWeeks / 52
        if (numberOfYears < 1) {
            return  "%.1f".format(numberOfYears.times(12)) + " Month(s) are required "
        } else {
            return "%.1f".format(numberOfYears) + " Year(s) are required "
        }

        return "Oops.. Some issue"
    }

    fun getJobUsed(): String {
        val commaSeperatedString = usedJobList.distinct().joinToString { it -> "\'${it.name}\'" }
        return "Jobs : " + commaSeperatedString

    }

}
