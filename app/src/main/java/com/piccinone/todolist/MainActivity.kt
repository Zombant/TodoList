package com.piccinone.todolist

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AddItemDialogFragment.AddItemDialogListener, EditItemDialogFragment.EditItemDialogListener {

    private var fragment: TodoListFragment = TodoListFragment()

    val DEFAULT_TASK_NAME = "Unnamed task"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }
    }

    fun newFragment() {
        fragment = TodoListFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }
    }

    fun sort() {
        TODO("Not yet implemented")
        //Will load data, sort it, store it back in sharedprefs, and create new fragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.addTodoItem) {
            showAddItemDialog()
            return true
        } else if (id == R.id.deleteCompleted) {
            SharedPrefsUpdate.deleteCompletedTasks(applicationContext)
            newFragment()
            return true
        } else if (id == R.id.incDate) {
            SharedPrefsUpdate.sortTasks(applicationContext, SortType.DateInc)
            newFragment()
            return true
        } else if (id == R.id.decDate) {
            SharedPrefsUpdate.sortTasks(applicationContext, SortType.DateDec)
            newFragment()
            return true
        } else if (id == R.id.AtoZ) {
            SharedPrefsUpdate.sortTasks(applicationContext, SortType.AtoZ)
            newFragment()
            return true
        } else if (id == R.id.ZtoA) {
            SharedPrefsUpdate.sortTasks(applicationContext, SortType.ZtoA)
            newFragment()
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
    //TODO: Clean up copy/pasted code
    override fun onAddDialogPositiveClick(dialog: DialogFragment) {
        // Get views from dialog
        val taskNameEditText: EditText? = dialog.dialog?.findViewById(R.id.taskNameEditText) as EditText?
        val datePicker = dialog.dialog?.findViewById(R.id.calendar) as CalendarView?
        val dateSwitch = dialog.dialog?.findViewById(R.id.hasDateSwitch) as Switch?

        // Get taskname and date from dialog
        var taskName: String = taskNameEditText?.text.toString()
        val date = if (dateSwitch!!.isChecked){
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(datePicker?.date!!))
        } else {
            ""
        }

        // If task name is blank, set it to the default
        if (taskName.isBlank()) {
            taskName = DEFAULT_TASK_NAME
        }

        // Store in shared preferences
        SharedPrefsUpdate.storeNewTask(applicationContext, TodoListEntry(false, taskName, date))

        // New fragment to reflect new data
        newFragment()
    }

    // When the "Cancel" button is pressed on the AddItemDialogFragment
    override fun onAddDialogNegativeClick(dialog: DialogFragment) {
        // Do nothing
    }

    override fun onEditDialogPositiveClick(dialog: DialogFragment) {
        // Get views from dialog
        val taskNameEditText: EditText? = dialog.dialog?.findViewById(R.id.taskNameEditText) as EditText?
        val datePicker = dialog.dialog?.findViewById(R.id.calendar) as CalendarView?
        val dateSwitch = dialog.dialog?.findViewById(R.id.hasDateSwitch) as Switch?

        // Get taskname and date from dialog
        var taskName: String = taskNameEditText?.text.toString()
        val date = if (dateSwitch!!.isChecked){
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(datePicker?.date!!))
        } else {
            ""
        }

        // If task name is blank, set it to the default
        if (taskName.isBlank()) {
            taskName = DEFAULT_TASK_NAME
        }

        // Store in shared preferences
        SharedPrefsUpdate.updateTask(applicationContext, (dialog as EditItemDialogFragment).taskPosition, TodoListEntry(dialog.completed, taskName, date))

        // New fragment to reflect new data
        newFragment()
    }

    override fun onEditDialogNegativeClick(dialog: DialogFragment) {
        //Do nothing
    }
}