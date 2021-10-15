@file:Suppress("DEPRECATION")

package com.anujan.sphassignment.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anujan.sphassignment.R
import com.anujan.sphassignment.adapter.MobileDataAdapter
import com.anujan.sphassignment.app.SPHApplication
import com.anujan.sphassignment.databinding.FragmentHomeBinding
import com.anujan.sphassignment.entity.MainRecords
import com.anujan.sphassignment.response.Records
import com.anujan.sphassignment.util.Status
import kotlinx.android.synthetic.main.dialog_box_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var adapter: MobileDataAdapter
    private lateinit var progress: ProgressBar
    private var records: List<Records> = ArrayList()
    protected val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_home, container,
            false
        )

        val appComponent = (activity?.applicationContext as SPHApplication).appComponent
        appComponent.inject(this)

        val binding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding = binding
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.viewmodel = homeViewModel

        recyclerView = view.findViewById(R.id.ListOfUser)
        progress = view.findViewById(R.id.progressBar)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val cm =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting){
            setupObservers()
        }
        else{
            getListDataRoom()
        }

        return view
    }


    private fun setupObservers() {
        activity?.let {
            homeViewModel.getMobileData().observe(it, Observer {
                it?.let { resource ->

                    when (it.status) {
                        Status.SUCCESS -> {
                            recyclerView.visibility = View.VISIBLE
                            progress.visibility = View.GONE
                            records = resource.data?.result!!.records
                            retrieveList(records)
                            deleteData()
                            for (record in records){
                                setDataBase(MainRecords(record._id,record.volume_of_mobile_data,record.quarter))
                            }
                        }
                        Status.ERROR -> {
                            recyclerView.visibility = View.VISIBLE
                            progress.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            progress.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }
    private fun deleteData(){
        scope.launch {
           val intM = homeViewModel.deleteData()
        }
    }

    private fun getListDataRoom(){

        scope.launch {

            var list: ArrayList<Records> = ArrayList()
            val listOfMainData = homeViewModel.getListData()
            recyclerView.visibility = View.VISIBLE
            progress.visibility = View.GONE
            for (listOfMain in listOfMainData!!){
                list.add(Records(listOfMain.volume_of_mobile_data!!,listOfMain.quarter!!,listOfMain._id!!))
            }
            if (listOfMainData.isEmpty()){
                dialogBoxFunction(
                    "No Data",
                    "Sorry you didn't visit for this app. " +
                            "So we don't have data on local please check your internet connection"
                )
            }
            else{
                records = list
                retrieveList(records)
            }
        }
    }

    private fun setDataBase(records:MainRecords){
        scope.launch {
            val intM =  homeViewModel.saveData(records)
        }
    }


    private fun retrieveList(records: List<Records>) {
        MobileDataAdapter(records, onUserItemSelected).apply {
            recyclerView.adapter = this
        }
    }

    private val onUserItemSelected = object : MobileDataAdapter.Callback {
        override fun onItemClicked(records: Records) {

        }
    }
    private fun dialogBoxFunction(title:String,message:String){
        activity?.runOnUiThread {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_box_layout, null)
            mDialogView.dialogTitle.text = title
            mDialogView.dialogMessage.text = message
            mDialogView.imageView6.setImageResource(R.drawable.checked)
            val mBuilder = AlertDialog.Builder(requireActivity())
                .setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.dialogOkButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}



