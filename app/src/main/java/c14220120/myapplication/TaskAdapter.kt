package c14220120.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: ArrayList<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback


        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_recyler, parent, false)
        return TaskViewHolder(view)
    }
    interface OnItemClickCallback {
        fun deleteItem(pos: Int)
        fun editItem(pos: Int)
        fun updateItem(pos: Int)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val task = tasks[position]
        holder.title.text = task.judul
        holder.description.text = task.deskripsi


        if(task.statusStart){
            holder.btnStart.text = "Finish"
            holder.btnEdit.isEnabled = false
        }
        if (task.statusEnd){
            holder.btnStart.text = "Done"
            holder.btnEdit.isEnabled = false
            holder.btnStart.isEnabled = false
        }

        holder.btnDelete.setOnClickListener {
            onItemClickCallback.deleteItem(position)
        }
        holder.btnEdit.setOnClickListener {
            onItemClickCallback.editItem(position)
        }

        holder.btnStart.setOnClickListener {
            onItemClickCallback.updateItem(position)
        }

    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnStart: Button = itemView.findViewById(R.id.btnStartFinish)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    }
}