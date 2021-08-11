package com.piccinone.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AddItemDialogFragment.AddItemDialogListener {

    private val fragment: TodoListFragment = TodoListFragment()

    val DEFAULT_TASK_NAME = "Unnamed task"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.addTodoItem) {
            showAddItemDialog()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // Show the add item dialog
    private fun showAddItemDialog() {
        // Create an instance of the AddItemDialogFragment
        val dialogFragment: AddItemDialogFragment = AddItemDialogFragment()

        // Show the dialog fragment
        dialogFragment.show(supportFragmentManager, "AddItemDialogFragment")
    }

    // When the "Add" button is pressed on the AddItemDialogFragment
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        // Get views from dialog
        val taskNameEditText: EditText? = dialog.dialog?.findViewById(R.id.taskNameEditText) as EditText?
        val datePicker = dialog.dialog?.findViewById(R.id.calendar) as CalendarView?

        // Get taskname and date from dialog
        var taskName: String = taskNameEditText?.text.toString()
        val date: String = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(datePicker?.date!!))

        // If task name is blank, set it to the default
        if (taskName.isBlank()) {
            taskName = DEFAULT_TASK_NAME
        }

        // Store in shared preferences
        storeNewTask(TodoListEntry(false, taskName, date))
    }

    // When the "Cancel" button is pressed on the AddItemDialogFragment
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        //Do nothing
    }

    private fun getTodoSharedPrefs() : ArrayList<TodoListEntry>{
        val gson: Gson = Gson()
        val json = getSharedPreferences("todolist", 0)?.getString("todolistitems",null)
        var data: ArrayList<TodoListEntry>
        if(json != null) {
            data = gson.fromJson(json, object : TypeToken<List<TodoListEntry?>?>() {}.type)
        } else {
            data = ArrayList<TodoListEntry>()
        }
        return data
    }

    private fun storeTodoSharedPrefs(data: ArrayList<TodoListEntry>) {
        val gson: Gson = Gson()
        val editor = getSharedPreferences("todolist", 0)?.edit()
        editor?.putString("todolistitems", gson.toJson(data))
        editor?.apply()
    }

    private fun storeNewTask(task: TodoListEntry) {
        var data = getTodoSharedPrefs()
        data.add(task)
        storeTodoSharedPrefs(data)
    }
}