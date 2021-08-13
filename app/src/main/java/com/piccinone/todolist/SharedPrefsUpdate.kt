package com.piccinone.todolist

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.function.Predicate

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