package com.piccinone.todolist

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TodoListAdapter(private val data: ArrayList<TodoListEntry>, private var dataFiltered: ArrayList<TodoListEntry>, private val context: Context?) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    // Called by recycle view when its time to create a new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.TodoListViewHolder {
        //Create a row instance but do add to RecyclerView yet (the RecyclerView does that)
        val rowInstance = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_row, parent, false)
        //Pass new rowInstance to the View Holder
        return TodoListViewHolder(rowInstance, this)
    }

    // Populates view holders with data
    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        //Get the corresponding item from data
        val currentItem = dataFiltered[position]

        //Populate respective view holder with the data
        holder.nameTextView.text = currentItem.taskName
        holder.checkBox.isChecked = currentItem.completed
        holder.dateTextView.text = currentItem.date

        // Update the strikethrough text
        holder.updateTextStrikethrough()

        // When the task is checked or unchecked
        holder.checkBox.setOnClickListener {

            // Update the checked status
            SharedPrefsUpdate.updateTask(context, position, TodoListEntry(holder.checkBox.isChecked, currentItem.taskName, currentItem.date))

            // Update the strikethrough text
            holder.updateTextStrikethrough()
        }

    }

    override fun getItemCount(): Int {
        return dataFiltered.size
    }

    class TodoListViewHolder(private val rowInstance: View, private val adapter: TodoListAdapter) : RecyclerView.ViewHolder(rowInstance), View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        val nameTextView: TextView = rowInstance.findViewById(R.id.taskTextView)
        val checkBox: CheckBox = rowInstance.findViewById(R.id.completedCheckbox)
        val dateTextView: TextView = rowInstance.findViewById(R.id.dateTextView)

        init {
            rowInstance.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.setHeaderTitle("Actions")
            menu?.add(Menu.NONE, 1, 1, "Edit")?.setOnMenuItemClickListener(this)
            menu?.add(Menu.NONE, 2, 2, "Delete")?.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            val info = item?.menuInfo
            when (item?.itemId) {
                1 -> {
                    editTask()
                    newFragment()
                }
                2 -> {
                    SharedPrefsUpdate.deleteTask(rowInstance.context, this.adapterPosition)
                    newFragment()
                }
            }

            return true

        }

        private fun editTask() {
            val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            val editDialog: EditItemDialogFragment = EditItemDialogFragment(this.adapterPosition, this.nameTextView.text as String, this.checkBox.isChecked, formatter.parse(this.dateTextView.text as String).time)
            var activity: AppCompatActivity = rowInstance.context as AppCompatActivity
            editDialog.show(activity.supportFragmentManager, "EditItemDialogFragment")
        }

        // Update the strikethrough status of the checkbox
        fun updateTextStrikethrough() {
            if(checkBox.isChecked) {
                nameTextView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                nameTextView.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

        private fun newFragment() {
            var activity: AppCompatActivity = rowInstance.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentHolder, TodoListFragment()).commit()
        }

    }

}