package com.sockytest.ui.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jakewharton.picasso.OkHttp3Downloader
import com.sockytest.R
import com.sockytest.network.model.Job
import com.sockytest.ui.main.EmployeeAdapter.CustomViewHolder
import com.sockytest.ui.main.listener.ButtonActionListener
import com.sockytest.ui.model.EmployeeData
import com.squareup.picasso.Picasso

class EmployeeAdapter(private val buttonActionListener: ButtonActionListener) :
    RecyclerView.Adapter<CustomViewHolder>() {
    private var dataList = ArrayList<EmployeeData>()
    private var jobsList = ArrayList<Job>()
    private lateinit var apy : String

    fun updateData(dataList: List<EmployeeData>?) {
        this.dataList.clear()
        dataList?.let {
            this.dataList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun updateJobs(jobsList: ArrayList<Job>) {
       this.jobsList.clear()
        jobsList?.let {
            this.jobsList.addAll(it)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.user_layout, parent, false)
        return CustomViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.name.text = dataList[position].displayName
        holder.savingsGoal.text = ""+dataList[position].goal
        holder.comment.text = dataList[position].getDaysToAchieveGoal(dataList[position].getSavingsPerWeek(jobsList,dataList[position]),dataList[position],apy) + " to achieve goal of "+dataList[position].goal

        holder.company.text = dataList[position].getJobUsed()
        holder.yearlyWage.text = dataList[position].yearlyWage.toString()

        val builder = Picasso.Builder(holder.itemView.context)
        builder.downloader(OkHttp3Downloader(holder.itemView.context))
        builder.build().load(dataList[position].picture)
            .placeholder((R.drawable.ic_launcher_foreground))
            .error(android.R.drawable.stat_notify_error)
            .into(holder.userImage)

        if (dataList[position].editMode) {
            holder.editButton.visibility = View.GONE
            holder.editLayout.visibility = View.VISIBLE

           // holder.newWageField.setText("" + dataList[position].dailyWage)

            holder.cancelButton.setOnClickListener { buttonActionListener.onCancelClicked(position) }
            holder.updateButton.setOnClickListener {
                val newSavingsGoal: Long =
                    if (holder.newSavingsGoal.text.isEmpty()) 0 else holder.newSavingsGoal.text.toString()
                        .toLong()

                val newSavingsPercentage: Long =
                    if (holder.newSavingsPercentage.text.isEmpty()) 0 else holder.newSavingsPercentage.text.toString()
                        .toLong()

                buttonActionListener.onUpdateClicked(position, newSavingsGoal, newSavingsPercentage)
            }
        } else {
            holder.editButton.visibility = View.VISIBLE
            holder.editLayout.visibility = View.GONE
            holder.editButton.setOnClickListener { buttonActionListener.onEditClicked(position) }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateApy(s: String) {
        this.apy = s
    }

    inner class CustomViewHolder(view: View) : ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val company : TextView = view.findViewById(R.id.company)
        val yearlyWage : TextView = view.findViewById(R.id.yearly_wage)
        val savingsGoal : TextView = view.findViewById(R.id.savings_goal)
        val comment: TextView = view.findViewById(R.id.comment)
        val userImage: ImageView = view.findViewById(R.id.user_image)
        val editButton: Button = view.findViewById(R.id.editButton)
        val editLayout: View = view.findViewById(R.id.editLayout)
        val newSavingsGoal: EditText = view.findViewById(R.id.newSavingsGoal)
        val newSavingsPercentage : EditText = view.findViewById(R.id.newSavingsPercentage)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        val updateButton: Button = view.findViewById(R.id.updateButton)
    }
}