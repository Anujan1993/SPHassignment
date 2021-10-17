package com.anujan.sphassignment.home

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.anujan.sphassignment.entity.MainRecords
import com.anujan.sphassignment.entity.RecordsRoom
import com.anujan.sphassignment.repository.MobileDataRepository
import com.anujan.sphassignment.repository.UserRepository
import com.anujan.sphassignment.response.MainResponse
import com.anujan.sphassignment.response.Records
import com.anujan.sphassignment.response.Result
import com.anujan.sphassignment.response.singledata.MainSingleData
import com.anujan.sphassignment.util.Resource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeViewModelTest{

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mobileDataRepository: MobileDataRepository
    @Mock
    lateinit var sharedPreferences: SharedPreferences
    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var homeViewModel:HomeViewModel

    @Mock
    lateinit var data:MainRecords
    @Mock
    lateinit var rec:RecordsRoom

    private lateinit var mainResponseObserver: Observer<Resource<MainResponse>>
    private lateinit var mainResponseSingleObserver: Observer<Resource<MainSingleData>>
    private val successResource = Resource.success(MainResponse(Result(
        listOf(Records("0.000384","004-Q3",1), Records("0.000543","004-Q4",2))
    )))

    private val validateYear = "2014"
    private val successSingleResource = Resource.success(
        MainSingleData(
            com.anujan.sphassignment.response.singledata.Result(
                listOf(
                    com.anujan.sphassignment.response.singledata.Records(
                    "0.000384",
                    "004-Q3",
                    1,
                    "4",
                    0.12122
                    ),
                    com.anujan.sphassignment.response.singledata.Records(
                    "0.000543",
                    "004-Q4",
                    2,
                    "4",
                    0.00022
                    )
                ),
            4
            )
        )
    )


    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        runBlocking {
            whenever(mobileDataRepository.getMobileData()).thenReturn(successResource)
            whenever(mobileDataRepository.getMobileSingleData(validateYear)).thenReturn(successSingleResource)
        }
        homeViewModel = HomeViewModel(mobileDataRepository,sharedPreferences,userRepository)

        mainResponseObserver = mock()
        mainResponseSingleObserver = mock()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun getMobileData()  = runBlocking {
        homeViewModel.getMobileData().observeForever(mainResponseObserver)
        homeViewModel.getMobileData()
        delay(100)
        verify(mobileDataRepository).getMobileData()
        verify(mainResponseObserver, timeout(50)).onChanged(Resource.loading(null))
        verify(mainResponseObserver, timeout(50)).onChanged(successResource)
    }

    @Test
    fun getSingleData()= runBlocking {
        homeViewModel.getSingleData(validateYear).observeForever(mainResponseSingleObserver)
        homeViewModel.getSingleData(validateYear)
        delay(10)
        verify(mobileDataRepository).getMobileSingleData(validateYear)
        verify(mainResponseSingleObserver, timeout(50)).onChanged(Resource.loading(null))
        verify(mainResponseSingleObserver, timeout(50)).onChanged(successSingleResource)
    }

    @Test
    fun loggedInOrNot() {
        assert(homeViewModel.loggedInOrNot() is Boolean)
    }

    @Test
    fun loadThemeMode() {
        assert(homeViewModel.loadThemeMode() is Boolean)
    }

    @Test
    fun getListData() {
        assert(homeViewModel.getListData() is List<MainRecords> )
    }

    @Test
    fun deleteData() {
        assert(homeViewModel.deleteData() is Int)
    }

    @Test
    fun saveData() = runBlocking {
        assert(homeViewModel.saveData(data) is Long)
    }

    @Test
    fun getRecordData() {
        assert(homeViewModel.getRecordData("2005") is List<RecordsRoom> )
    }

    @Test
    fun deleteRecordData()  {
        assert(homeViewModel.deleteRecordData("2005") is Int )
    }

    @Test
    fun saveRecordData() {
        assert(homeViewModel.saveRecordData(rec) is Long )
    }
}