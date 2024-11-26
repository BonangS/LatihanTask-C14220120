package c14220120.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var tasks: ArrayList<Task> = arrayListOf()
    private var judul: MutableList<String> = mutableListOf()
    private var deskripsi: MutableList<String> = mutableListOf()

    companion object {
        const val ADD_TASK = 1
        const val EDIT_TASK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Apply padding to system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerView)

        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            val intent = Intent(this, TambahTask::class.java)
            intent.putExtra("Task", "task")
            startActivityForResult(intent, ADD_TASK)
        }
        fun tambahData() {
            tasks.clear()
            for (position in judul.indices) {
                val task = Task(
                    judul[position],
                    deskripsi[position],
                    false,
                    false
                )
                tasks.add(task)
            }
        }

        fun tampilkanData() {
            taskAdapter = TaskAdapter(tasks)
            recyclerView.adapter = taskAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            taskAdapter.setOnItemClickCallback(object : TaskAdapter.OnItemClickCallback {
                override fun deleteItem(pos: Int) {
                    if (pos >= 0 && pos < tasks.size) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("HAPUS DATA")
                            .setMessage("Apakah Benar Data akan dihapus ?")
                            .setPositiveButton("HAPUS") { dialog, which ->
                                tasks.removeAt(pos)
                                taskAdapter.notifyItemRemoved(pos) // Pastikan data terhapus dan adapter diperbarui
                                taskAdapter.notifyItemRangeChanged(pos, tasks.size) // Update sisa item
                            }
                            .setNegativeButton("BATAL") { dialog, which ->
                                Toast.makeText(this@MainActivity, "Data Batal Dihapus", Toast.LENGTH_LONG).show()
                            }
                            .show()
                    } else {
                        Toast.makeText(this@MainActivity, "Invalid task index", Toast.LENGTH_SHORT).show()
                    }
                }



                override fun editItem(pos: Int) {
                    if (pos >= 0 && pos < tasks.size) {
                        val intent = Intent(this@MainActivity, TambahTask::class.java)
                        intent.putExtra("position", pos)
                        intent.putExtra("title", tasks[pos].judul)
                        intent.putExtra("description", tasks[pos].deskripsi)
                        startActivityForResult(intent, EDIT_TASK)
                    }
                }

                override fun updateItem(pos: Int) {
                    if (pos >= 0 && pos < tasks.size && !tasks[pos].statusStart) {
                        tasks[pos].statusStart = true
                        taskAdapter.notifyItemChanged(pos) // Beri tahu adapter untuk memperbarui item
                    } else if (pos >= 0 && pos < tasks.size && tasks[pos].statusStart) {
                        tasks[pos].statusEnd = true
                        taskAdapter.notifyItemChanged(pos) // Beri tahu adapter untuk memperbarui item
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Invalid position",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
        }
        judul = mutableListOf()
        deskripsi = mutableListOf()
        tambahData()
        tampilkanData()
    }

    private fun addTask(judul: String, deskripsi: String) {
        // Tambahkan task baru
        val task = Task(judul, deskripsi, false, false)
        tasks.add(task)

        // Perbarui adapter
        taskAdapter.notifyItemInserted(tasks.size - 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("title")
            val description = data?.getStringExtra("description")
            if (title != null && description != null) {
                addTask(title, description)
            }
        } else if (requestCode == EDIT_TASK && resultCode == RESULT_OK) {
            val position = data?.getIntExtra("position", -1)
            val title = data?.getStringExtra("title")
            val description = data?.getStringExtra("description")

            if (position != null && position >= 0 && title != null && description != null) {
                tasks[position].judul = title
                tasks[position].deskripsi = description
                taskAdapter.notifyItemChanged(position)
            }
        }
    }
}
