package com.example.localdatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeworkAdapter2 (private val listHomework: ArrayList<Homework>):
    RecyclerView.Adapter<HomeworkAdapter2.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_homework, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvHomeworkDate.text = listHomework[position].date.toString()
        holder.tvHomeworkDescription.text = listHomework[position].description.toString()
        holder.tvHomeworkTitle.text = listHomework[position].title.toString()

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listHomework[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listHomework.size

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvHomeworkDate: TextView = itemView.findViewById(R.id.tv_item_date)
        var tvHomeworkDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        var tvHomeworkTitle: TextView = itemView.findViewById(R.id.tv_item_title)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Homework)
    }
}