package com.sockytest.network.model

data class EmployeeJobResponse(
    val bankInfo: BankInfo,
    val employeeList: List<Employee>,
    val jobs: List<Job>
)