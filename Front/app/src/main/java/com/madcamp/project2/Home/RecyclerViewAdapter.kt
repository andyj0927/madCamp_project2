package com.madcamp.project2.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.madcamp.project2.Data.User
import com.madcamp.project2.R

class RecyclerViewAdapter(val dataset: MutableList<User>, val listener: RecyclerViewClickListener): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int){
        holder.bind(dataset[position], position)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val displayname : TextView = itemView.findViewById(R.id.displayname)
        private val currentlyActive: TextView = itemView.findViewById(R.id.currentlyactive)
        private val c_view = view
        fun bind(item: User, position: Int) {
            displayname.text = item.displayName
            currentlyActive.text = item.currentlyActive.toString()
            c_view.setOnClickListener{
                listener.onClick(c_view, position)
            }

//            val pos = adapterPosition
//            if(pos!= RecyclerView.NO_POSITION)
//            {
//                itemView.setOnClickListener {
//                    listener?.onItemClick(itemView,item,pos)
//                }
//            }
        }
    }



    override fun getItemCount(): Int = dataset.size
}