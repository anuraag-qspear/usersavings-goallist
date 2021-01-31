package com.sockytest.network

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.sockytest.App
import com.sockytest.network.model.Employee
import com.sockytest.network.model.EmployeeJobResponse
import com.sockytest.network.model.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EmployeeDataRepository {
    private val userDataService = RetrofitClient.instance?.create(EmployeeDataService::class.java)

    fun getUserList(
        liveData: MutableLiveData<List<Employee>?>,
        networkJobsLiveData: MutableLiveData<List<Job>?>,
        networkApyLiveData: MutableLiveData<String>
    ) {
        val call = userDataService?.employeeJobs
        call?.enqueue(object : Callback<EmployeeJobResponse> {
            override fun onResponse(call: Call<EmployeeJobResponse>, response: Response<EmployeeJobResponse>) {
                liveData.value = response.body()?.employeeList
                networkJobsLiveData.value = response.body()?.jobs
                networkApyLiveData.value = response.body()?.bankInfo?.apy

            }

            override fun onFailure(call: Call<EmployeeJobResponse>, t: Throwable) {
                Log.e("UserDataRepository", "getUserList::onFailure", t)

                Toast.makeText(
                    App.getInstance(),
                    "Something went wrong...Please try later! " + t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}