package com.anujan.sphassignment.singlerecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anujan.sphassignment.R
import com.anujan.sphassignment.databinding.CartLayoutBinding
import com.anujan.sphassignment.databinding.FragmentHomeBinding
import com.anujan.sphassignment.databinding.FragmentSingleDataBinding
import com.anujan.sphassignment.response.singledata.Records

class SingleDataFragment : Fragment() {

  private lateinit var binding: FragmentSingleDataBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
  Bundle?): View? {


    val view = inflater.inflate(R.layout.fragment_single_data, container, false)

    binding = FragmentSingleDataBinding.bind(view)

    val args = arguments
    val quarterText = args?.getString("quarter")
    val volume_of_mobile_dataText = args?.getString("volume_of_mobile_data")
    val rankText = args?.getDouble("rank")
    val id = args?.getInt("id")
    val full_countText = args?.getString("full_count")


    binding.records = Records(
      volume_of_mobile_dataText!!,
      quarterText!!,
      id!!,
      full_countText!!,
      rankText!!
    )

    return view
  }

  companion object {

    fun newInstance(record: Records): SingleDataFragment {

      val args = Bundle()
      args.putString("quarter", record.quarter)
      args.putDouble("rank", record.rank)
      args.putInt("id", record._id)
      args.putString("full_count", record._full_count)
      args.putString("volume_of_mobile_data", record.volume_of_mobile_data)

      val fragment = SingleDataFragment()
      fragment.arguments = args
      return fragment
    }
  }

}
