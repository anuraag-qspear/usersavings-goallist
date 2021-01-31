package com.sockytest.ui.main

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sockytest.App
import com.sockytest.db.LocalCache
import com.sockytest.db.SharePreferenceCache
import com.sockytest.network.EmployeeDataRepository.getUserList
import com.sockytest.network.model.Employee
import com.sockytest.network.model.Job
import com.sockytest.ui.main.listener.ButtonActionListener
import com.sockytest.ui.model.EmployeeData
import java.util.ArrayList

class MainViewModel : ViewModel(), ButtonActionListener {
    private val savingsGoalMap = HashMap<String, Int>()
    private val savingsPercentageMap = HashMap<String, Int>()

    private var userDataList: List<EmployeeData> = ArrayList()

    private val networkEmployeeLiveData = MutableLiveData<List<Employee>?>()

     val networkJobsLiveData = MutableLiveData<List<Job>?>()
     val networkApyLiveData = MutableLiveData<String>()
    val employeeDataLiveData = MutableLiveData<List<EmployeeData>>()

    val cache: LocalCache = SharePreferenceCache()

    init {
        networkEmployeeLiveData.observeForever {
            updateUserData(it)
        }
    }

    fun onStart() {
        getUserData()
    }

    private fun getUserData() {
        if (networkEmployeeLiveData.value == null) {
            getUserList(networkEmployeeLiveData,networkJobsLiveData,networkApyLiveData)
        }
    }

    private fun updateUserData(userList: List<Employee>?) {
        val userDataList = ArrayList<EmployeeData>()
        userList?.forEach {
            val employeeData = EmployeeData(it)

            savingsGoalMap[it.id+"goal"] = cache.getValue(it.id+"goal")
            savingsPercentageMap[it.id+"percent"] = cache.getValue(it.id+"percent")
            employeeData.goal = savingsGoalMap[it.id+"goal"]?.takeIf { wage -> wage > 0 } ?: employeeData.goal
            if(savingsPercentageMap[it.id+"percent"]!=null && savingsPercentageMap[it.id+"percent"]!!.toInt()>0){
                employeeData.savingPerWeek = savingsPercentageMap[it.id+"percent"].toString()+"%";
            }else{
                employeeData.savingPerWeek = employeeData.savingPerWeek
            }
            userDataList.add(employeeData)
        }

        sortAndUpdate(userDataList)
    }

    private fun sortAndUpdate(userDataList: List<EmployeeData>) {
        this.userDataList = userDataList.sortedBy { employee -> employee.goal }
        employeeDataLiveData.postValue(this.userDataList)
    }

    override fun onEditClicked(position: Int) {
        userDataList[position].editMode = true

        employeeDataLiveData.postValue(this.userDataList)
    }

    override fun onUpdateClicked(position: Int, newSavingsGoal: Long, newSavingsPercentage : Long) {
        if (newSavingsGoal.toInt() == 0) {
            Toast.makeText(App.getInstance(), "Enter new savings goal more than zero", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (newSavingsPercentage.toInt() == 0) {
            Toast.makeText(App.getInstance(), "Enter new savings percentage more than zero", Toast.LENGTH_SHORT)
                .show()
            return
        }

        userDataList[position].editMode = false

        //userDataList[position].dailyWage = newWage
        userDataList[position].goal = newSavingsGoal.toInt();
        savingsGoalMap[userDataList[position].id+"goal"] = newSavingsGoal.toInt()
        cache.storeValue(userDataList[position].id+"goal", newSavingsGoal.toInt())


        userDataList[position].savingPerWeek = newSavingsPercentage.toString()+"%";
        savingsPercentageMap[userDataList[position].id+"percent"]= newSavingsPercentage.toInt()
        cache.storeValue(userDataList[position].id+"percent",newSavingsPercentage.toInt())


        sortAndUpdate(this.userDataList)
    }

    override fun onCancelClicked(position: Int) {
        userDataList[position].editMode = false

        employeeDataLiveData.postValue(this.userDataList)
    }
}