package com.example.progerapp

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class TaskAdapter(
    context: Context,
    private val tasks: MutableList<Task>,
    private val onTaskChecked: (Int, Boolean) -> Unit
) : ArrayAdapter<Task>(context, R.layout.item_task, tasks) {

    private class ViewHolder(view: View) {
        val taskText: TextView = view.findViewById(R.id.taskText)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
            viewHolder = ViewHolder(view).apply {
                checkBox.apply {
                    isFocusable = false
                    isFocusableInTouchMode = false
                }
            }
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val task = tasks[position]
        viewHolder.taskText.text = task.text

        // Обновляем зачеркивание текста
        viewHolder.taskText.paintFlags = if (task.isCompleted) {
            viewHolder.taskText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            viewHolder.taskText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        viewHolder.checkBox.isChecked = task.isCompleted
        viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskChecked(position, isChecked)
        }

        return view
    }
}