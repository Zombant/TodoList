package com.piccinone.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TodoListFragment : Fragment() {

    //Returns the view that will be shown
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Use the fragment_todo_list layout
        var rootView: View =  inflater.inflate(R.layout.fragment_todo_list, container, false)

        //Data to be put on the recyclerview
        val data = arrayListOf(TodoListEntry(false, "Test1"), TodoListEntry(true, "Test2"))

        // Set the Adapter of the RecyclerView that is in the root view (R.layout.fragment_todo_list)
        // to the custom Adapter and pass in the data, a copy of the data, and application context
        rootView.findViewById<RecyclerView>(R.id.recyclerView).adapter = TodoListAdapter(data, ArrayList(data), activity?.applicationContext)

        //Set the layout manager of the RecyclerView to a LinearLayoutManager
        rootView.findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(activity?.applicationContext)

        //Allow the recyclerview to change size
        rootView.findViewById<RecyclerView>(R.id.recyclerView).setHasFixedSize(false)

        return rootView
    }


}