package com.anujan.sphassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anujan.sphassignment.databinding.CartLayoutBinding
import com.anujan.sphassignment.response.Records

class MobileDataAdapter(
private val nameList: List<Records>, private val userItemSelectedCallback: Callback?
) :
RecyclerView.Adapter<MobileDataAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return with(CartLayoutBinding.inflate(layoutInflater, parent, false)) {
            UserViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        nameList[position].also {
            holder.bindData(it)
        }
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    open inner class UserViewHolder(private val binding: CartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(records: Records) {
            binding.records = records
            binding.clickCard.setOnClickListener {
                userItemSelectedCallback?.onItemClicked(records)
            }
        }
    }

    interface Callback {
        fun onItemClicked(records: Records)
    }
}