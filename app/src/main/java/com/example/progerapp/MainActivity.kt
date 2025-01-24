package com.example.progerapp

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

data class Task(
    val text: String,
    var isCompleted: Boolean = false
)

class MainActivity : AppCompatActivity() {

    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.listView)
        val userData = findViewById<EditText>(R.id.user_data)
        val button = findViewById<Button>(R.id.button)

        adapter = TaskAdapter(this, tasks) { position, isChecked ->
            tasks[position].isCompleted = isChecked
            adapter.notifyDataSetChanged()
        }
        listView.adapter = adapter

        registerForContextMenu(listView)

        button.setOnClickListener {
            val text = userData.text.toString().trim()
            if (text.isNotEmpty()) {
                tasks.add(Task(text))
                adapter.notifyDataSetChanged()
                userData.text.clear()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.listView) {
            (menuInfo as? AdapterView.AdapterContextMenuInfo)?.let {
                menu.setHeaderTitle(tasks[it.position].text)
                menuInflater.inflate(R.menu.context_menu, menu)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.delete -> {
                tasks.removeAt(info?.position ?: return false)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Задача удалена", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}