package com.anujan.sphassignment.singlerecord

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anujan.sphassignment.R
import com.anujan.sphassignment.app.SPHApplication
import com.anujan.sphassignment.databinding.ActivitySingleRecordBinding
import com.anujan.sphassignment.entity.RecordsRoom
import com.anujan.sphassignment.home.HomeViewModel
import com.anujan.sphassignment.response.singledata.Records
import com.anujan.sphassignment.util.AppConstant
import com.anujan.sphassignment.util.Status
import kotlinx.android.synthetic.main.dialog_box_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Math.abs
import javax.inject.Inject

class SingleRecordActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var homeViewModel: HomeViewModel
    private var records: List<Records> = ArrayList()
    private var value: String = ""
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    private var recordIndex = 0
    private var  year:String =""
    protected val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {

        val appComponent = (applicationContext as SPHApplication).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_record)

        val binding = ActivitySingleRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        year = intent.getStringExtra(AppConstant.QUEAR)!!
        value = year.take(4)


        val actionbar = supportActionBar
        actionbar!!.title = "$value Records"
        actionbar.setDisplayHomeAsUpEnabled(true)


        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.viewmodel = homeViewModel

        setDark()
        setDarkAction()

        val cm =
               this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting){
            setupObservers()
        }
        else{
            getDataRoom()
        }

        gestureDetector = GestureDetector(this)
    }

    // Override this method to recognize touch event
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        }
        else {
            super.onTouchEvent(event)
        }
    }

    private fun setupObservers() {
        let {
            homeViewModel.getSingleData(value).observe(it, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {

                            val valQ = year?.takeLast(2).toString()
                            records = resource.data!!.result.records
                            when (resource.data.result.records[0].quarter.takeLast(2)) {
                                "Q1" -> {
                                    when (valQ) {
                                        "Q1" -> {
                                            recordIndex = 0
                                        }
                                        "Q2" -> {
                                            recordIndex = 1
                                        }
                                        "Q3" -> {
                                            recordIndex = 2
                                        }
                                        "Q4" -> {
                                            recordIndex = 3
                                        }
                                    }
                                }
                                "Q2" -> {
                                    when (valQ) {
                                        "Q2" -> {
                                            recordIndex = 0
                                        }
                                        "Q3" -> {
                                            recordIndex = 1
                                        }
                                        "Q4" -> {
                                            recordIndex = 2
                                        }
                                    }
                                }
                                "Q3" -> {
                                    recordIndex = when (valQ) {
                                        "Q3" -> {
                                            0
                                        }
                                        else -> 1
                                    }
                                }
                                else -> recordIndex = 0
                            }

                            scope.launch {
                                async {   deleteDataRoom(value)}.await()
                                for (rec in records){
                                    saveDataRoom(RecordsRoom(
                                        rec._id,
                                        rec.volume_of_mobile_data,
                                        rec.quarter,
                                        rec._full_count,
                                        rec.rank
                                    ))
                                }
                            }
                            loadDataToFragment(records)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            })
        }
    }

    private fun saveDataRoom(recordsRoom: RecordsRoom){
        val returnVal = homeViewModel.saveRecordData(recordsRoom)
    }
    private suspend fun deleteDataRoom(year:String){
            val returnVal = homeViewModel.deleteRecordData(year)
    }
    private fun getDataRoom(){
        scope.launch {
            val listOfMainData = homeViewModel.getRecordData(value)
            if(listOfMainData!!.isNotEmpty()){
                val valQ = year?.takeLast(2).toString()
                when (listOfMainData[0].quarter.takeLast(2)) {
                    "Q1" -> {
                        when (valQ) {
                            "Q1" -> {
                                recordIndex = 0
                            }
                            "Q2" -> {
                                recordIndex = 1
                            }
                            "Q3" -> {
                                recordIndex = 2
                            }
                            "Q4" -> {
                                recordIndex = 3
                            }
                        }
                    }
                    "Q2" -> {
                        when (valQ) {
                            "Q2" -> {
                                recordIndex = 0
                            }
                            "Q3" -> {
                                recordIndex = 1
                            }
                            "Q4" -> {
                                recordIndex = 2
                            }
                        }
                    }
                    "Q3" -> {
                        recordIndex = when (valQ) {
                            "Q3" -> {
                                0
                            }
                            else -> 1
                        }
                    }
                    else -> recordIndex = 0
                }
                val list: ArrayList<Records> = ArrayList()
                for (listOfMain in listOfMainData){
                    list.add(Records(
                        listOfMain.volume_of_mobile_data!!,
                        listOfMain.quarter!!,
                        listOfMain._id!!,
                        listOfMain._full_count!!,
                        listOfMain.rank!!
                    ))
                }
                records = list
                loadDataToFragment(records)
            }else{
                dialogBoxFunction(
                    "No Data",
                    "Sorry you didn't visit for this year to check data. " +
                            "So we don't have this year data on local please check your internet connection"
                )
            }
        }
    }

    private fun loadDataToFragment(records: List<Records>){
        replaceFragment(records, recordIndex)
    }

    private fun replaceFragment(records: List<Records>, recordIndex: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SingleDataFragment.newInstance(records[recordIndex]))
            .commit()
    }
    private fun setDark() {
        if (homeViewModel.loadThemeMode()){
            setTheme(R.style.AppThemeDark)
        }
        else{
            setTheme(R.style.AppThemeLite)
        }
    }
    private fun setDarkAction() {
        if (homeViewModel.loadThemeMode()){
            setTheme(R.style.AppThemeDarkActionBar)
        }
        else{
            setTheme(R.style.AppThemeLiteActionBar)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
        return
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        return
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        if (recordIndex > 0) {
                            recordIndex--
                            replaceFragment(records, recordIndex)
                        }
                    }
                    else {
                        if (recordIndex < records.size - 1) {
                            recordIndex++
                            replaceFragment(records, recordIndex)
                        }
                    }
                }
            }
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }
    private fun dialogBoxFunction(title:String,message:String){
        this.runOnUiThread {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_box_layout, null)
            //AlertDialogBuilder
            mDialogView.dialogTitle.text = title
            mDialogView.dialogMessage.text = message
            mDialogView.imageView6.setImageResource(R.drawable.checked)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.dialogOkButton.setOnClickListener {
                mAlertDialog.dismiss()
                finish()
            }
        }
    }
}