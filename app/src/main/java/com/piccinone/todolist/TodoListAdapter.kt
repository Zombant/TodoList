package com.piccinone.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TodoListAdapter(private val data: ArrayList<TodoListEntry>, private val dataFiltered: ArrayList<TodoListEntry>, private val context: Context?) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    // Called by recycle view when its time to create a new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.TodoListViewHolder {
        //Create a row instance but do add to RecyclerView yet (the RecyclerView does that)
        val rowInstance = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_row, parent, false)
        //Pass new rowInstance to the View Holder
        return TodoListViewHolder(rowInstance)
    }

    // Populates view holders with data
    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        //Get the corresponding item from data
        val currentItem = dataFiltered[position]

        //Populate respective view holder with the data
        holder.nameTextView.text = currentItem.taskName
        holder.checkBox.isChecked = currentItem.completed
        holder.dateTextView.text = currentItem.date

        holder.checkBox.setOnClickListener {
            val gson: Gson = Gson()

            var updatedData: ArrayList<TodoListEntry> = ArrayList()

            updatedData = dataFiltered.clone() as ArrayList<TodoListEntry>;
            updatedData[position] = TodoListEntry(holder.checkBox.isChecked, currentItem.taskName, currentItem.date)

            val editor = context?.getSharedPreferences("todolist", 0)?.edit()
            editor?.putString("todolistitems", gson.toJson(updatedData))
            editor?.apply()
        }

    }

    override fun getItemCount(): Int {
        return dataFiltered.size
    }

    class TodoListViewHolder(rowInstance: View) : RecyclerView.ViewHolder(rowInstance) {
        val nameTextView: TextView = rowInstance.findViewById(R.id.taskTextView)
        val checkBox: CheckBox = rowInstance.findViewById(R.id.completedCheckbox)
        val dateTextView: TextView = rowInstance.findViewById(R.id.dateTextView)
    }
}