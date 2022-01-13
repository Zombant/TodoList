package com.piccinone.todolist

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TodoListFragment : Fragment() {

    //Returns the view that will be shown
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Use the fragment_todo_list layout
        var rootView: View =  inflater.inflate(R.layout.fragment_todo_list, container, false)

        //Data to be put on the recyclerview
        val gson: Gson = Gson()

        //TESTING
        /*val dataTest = arrayListOf(TodoListEntry(false, "Test1", "May 7, 2001"), TodoListEntry(true, "Test2", "August 2, 2433"))
        val editor = context?.getSharedPreferences("todolist", 0)?.edit()
        editor?.putString("todolistitems", gson.toJson(dataTest))
        editor?.apply()*/
        //TESTING

        var data: ArrayList<TodoListEntry> = SharedPrefsUpdate.loadTasks(activity?.applicationContext)


        // Set the Adapter of the RecyclerView that is in the root view (R.layout.fragment_todo_list)
        // to the custom Adapter and pass in the data, a copy of the data, and application context
        rootView.findViewById<RecyclerView>(R.id.recyclerView).adapter = TodoListAdapter(data, ArrayList<TodoListEntry>(data), activity?.applicationContext)

        //Set the layout manager of the RecyclerView to a LinearLayoutManager
        rootView.findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(activity?.applicationContext)

        //Allow the recyclerview to change size
        rootView.findViewById<RecyclerView>(R.id.recyclerView).setHasFixedSize(false)

        return rootView
    }


}