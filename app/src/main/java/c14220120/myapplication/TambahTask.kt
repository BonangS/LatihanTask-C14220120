package c14220120.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TambahTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_task)

        // Apply padding to system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var judul = findViewById<EditText>(R.id.edtTitle)
        var deskripsi = findViewById<EditText>(R.id.edtDescription)
        var btnSave = findViewById<Button>(R.id.btnSave)


        val title = intent.getStringExtra("title")
        if (title != null) {
            judul.setText(title)
        }
        val description = intent.getStringExtra("description")
        if (description != null) {
            deskripsi.setText(description)
        }
        btnSave.setOnClickListener {
            val intent = Intent().apply {
                putExtra("title", judul.text.toString())
                putExtra("description", deskripsi.text.toString())
                putExtra("position", intent.getIntExtra("position", -1))
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
