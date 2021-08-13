package com.piccinone.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException
import java.util.*

class EditItemDialogFragment(val taskPosition: Int, val taskName: String, val completed: Boolean, val date: Long) : DialogFragment() {

    // Create a listener to deliver info to the activity
    internal lateinit var listener: EditItemDialogListener
    interface EditItemDialogListener {
        fun onEditDialogPositiveClick(dialog: DialogFragment)
        fun onEditDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            // Get the layout inflater
            val inflater: LayoutInflater = requireActivity().layoutInflater

            builder.setMessage("Edit Task")

            //Inflate dialog_add_item layout
            val dialogView: View = inflater.inflate(R.layout.dialog_add_item, null)
            val calendarView: CalendarView = dialogView.findViewById<CalendarView>(R.id.calendar)

            dialogView.findViewById<EditText>(R.id.taskNameEditText).setText(taskName)
            calendarView.date = date

            //Set the date when a new date is selected
            calendarView.setOnDateChangeListener { view: CalendarView, year: Int, month: Int, day: Int ->
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                view.date = calendar.timeInMillis
            }



            builder.setView(dialogView)
            builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, id ->
                // Send the positive button event back to the host activity
                listener.onEditDialogPositiveClick(this)
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                // Send the negative button event back to the host activity
                listener.onEditDialogNegativeClick(this)
            })

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as EditItemDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() + " must implement NoticeDialogListener"))
        }
    }
}