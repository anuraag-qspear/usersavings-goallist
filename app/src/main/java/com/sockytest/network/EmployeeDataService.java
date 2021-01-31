package com.sockytest.network;

import com.sockytest.network.model.EmployeeJobResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EmployeeDataService {

    @GET("/v3/21473259-9162-4797-97ed-0e1d364f98fa")
    Call<EmployeeJobResponse> getEmployeeJobs();
}