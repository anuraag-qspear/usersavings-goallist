package com.sockytest.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sockytest.R
import com.sockytest.network.model.Job


class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmployeeAdapter

    private lateinit var viewModel: MainViewModel

    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        adapter = EmployeeAdapter(viewModel)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()

        updateData()
    }

    private fun updateData() {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage("Loading....")
        progressDialog?.show()

//        viewModel.userDataLiveData.observe(this, { users ->
//            progressDialog?.dismiss()
//
//            adapter.updateData(users)
//        })


        viewModel.employeeDataLiveData.observe(this, Observer {
            progressDialog?.dismiss()

            adapter.updateData(it)
             })

        viewModel.networkJobsLiveData.observe(this, Observer {
            adapter.updateJobs(it as ArrayList<Job>) })
//

        viewModel.networkApyLiveData.observe(this, Observer {
            adapter.updateApy(it as String) })
    }
}