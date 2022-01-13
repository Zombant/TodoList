package com.piccinone.todolist

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class SharedPrefsUpdate {
    companion object {

        fun updateTask(context: Context?, position: Int, task: TodoListEntry) {
            val gson: Gson = Gson()
            var updatedData: ArrayList<TodoListEntry> = ArrayList()
            updatedData = loadTasks(context).clone() as ArrayList<TodoListEntry>;
            updatedData[position] = task
            storeTasks(context, updatedData)
        }

        fun storeNewTask(context: Context?, task: TodoListEntry) {
            var data = loadTasks(context)
            data.add(task)
            storeTasks(context, data)
        }

        fun deleteTask(context: Context?, position: Int) {
            val gson: Gson = Gson()
            var updatedData: ArrayList<TodoListEntry> = ArrayList()
            updatedData = loadTasks(context).clone() as ArrayList<TodoListEntry>
            updatedData.removeAt(position)
            storeTasks(context, updatedData)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun deleteCompletedTasks(context: Context?) {
            val gson: Gson = Gson()
            var updatedData: ArrayList<TodoListEntry> = ArrayList()
            updatedData = loadTasks(context).clone() as ArrayList<TodoListEntry>

            val condition: Predicate<TodoListEntry> =
                Predicate<TodoListEntry> { task: TodoListEntry ->
                    task.completed
                }
            updatedData.removeIf(condition)

            storeTasks(context, updatedData)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun sortTasks(context: Context?, sortType: SortType) {
            var data: ArrayList<TodoListEntry> = loadTasks(context).clone() as ArrayList<TodoListEntry>
            var outputData: ArrayList<TodoListEntry> = ArrayList()

            val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

            val hasNoDate: Predicate<TodoListEntry> =
                Predicate<TodoListEntry> { task: TodoListEntry ->
                    task.date == ""
                }
            val hasDate: Predicate<TodoListEntry> =
                Predicate<TodoListEntry> { task: TodoListEntry ->
                    task.date != ""
                }

            var dataWithDates = data.clone() as ArrayList<TodoListEntry>
            var dataWithoutDates = data.clone() as ArrayList<TodoListEntry>

            dataWithDates.removeIf(hasNoDate)
            dataWithoutDates.removeIf(hasDate)

            if (sortType == SortType.DateInc) {
                dataWithDates.sortBy { formatter.parse(it.date).time }
                dataWithDates.addAll(dataWithoutDates)
                outputData = dataWithDates
            } else if (sortType == SortType.DateDec) {
                dataWithDates.sortBy { formatter.parse(it.date).time }
                dataWithDates.reverse()
                dataWithDates.addAll(dataWithoutDates)
                outputData = dataWithDates
            } else if (sortType == SortType.AtoZ) {
                dataWithDates.sortBy { it.taskName.lowercase() }
                dataWithoutDates.sortBy { it.taskName.lowercase() }
                dataWithDates.addAll(dataWithoutDates)
                outputData = dataWithDates
            } else if (sortType == SortType.ZtoA) {
                dataWithDates.sortBy { it.taskName.lowercase() }
                dataWithoutDates.sortBy { it.taskName.lowercase() }
                dataWithoutDates.reverse()
                dataWithDates.reverse()
                dataWithDates.addAll(dataWithoutDates)
                outputData = dataWithDates
            }

            storeTasks(context, outputData)
        }

        fun loadTasks(context: Context?) : ArrayList<TodoListEntry> {
            val gson: Gson = Gson()
            val json = context?.getSharedPreferences("todolist", 0)?.getString("todolistitems",null)
            var data: ArrayList<TodoListEntry>
            if(json != null) {
                data = gson.fromJson(json, object : TypeToken<List<TodoListEntry?>?>() {}.type)
            } else {
                data = ArrayList<TodoListEntry>()
            }
            return data
        }

        private fun storeTasks(context: Context?, data: ArrayList<TodoListEntry>) {
            val gson: Gson = Gson()
            val editor = context?.getSharedPreferences("todolist", 0)?.edit()
            editor?.putString("todolistitems", gson.toJson(data))
            editor?.apply()
        }
    }

}