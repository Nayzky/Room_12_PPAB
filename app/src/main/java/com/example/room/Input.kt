package com.example.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.room.database.Note
import com.example.room.database.NoteDao
import com.example.room.database.NoteRoomDatabase
import com.example.room.databinding.ActivityInputBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Input : AppCompatActivity() {
    private lateinit var binding : ActivityInputBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!
        var command = intent.getStringExtra("COMMAND")


        with(binding){

            if(command=="UPDATE"){
                binding.btnUpdate.isVisible = true
                binding.btnAdd.isVisible = false
                updateId = intent.getIntExtra("ID", 0)
                var item_title = intent.getStringExtra("TITTLE")
                var item_desc = intent.getStringExtra("DESC")
                var item_date = intent.getStringExtra("DATE")

                binding.txtTitle.setText(item_title.toString())
                binding.txtDate.setText(item_date.toString())
                binding.txtDesc.setText(item_desc.toString())
            }else{
                binding.btnUpdate.isVisible = false
                binding.btnAdd.isVisible = true
            }

            btnAdd.setOnClickListener(View.OnClickListener {
                if (validateInput()){
                    insert(
                        Note(
                            title = txtTitle.text.toString(),
                            description = txtDesc.text.toString(),
                            date = txtDate.text.toString()
                        )
                    )
                    setEmptyField()
                    val IntentToHome = Intent(this@Input, MainActivity::class.java)
                    Toast.makeText(this@Input, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    startActivity(IntentToHome)
                }else{
                    Toast.makeText(this@Input, "Kolom Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show()
                }
            })

            btnUpdate.setOnClickListener {
                if(validateInput()){
                    update(
                        Note(
                            id = updateId,
                            title = txtTitle.text.toString(),
                            description = txtDesc.text.toString(),
                            date = txtDate.text.toString()
                        )
                    )
                    updateId = 0
                    setEmptyField()
                    val IntentToHome = Intent(this@Input, MainActivity::class.java)
                    Toast.makeText(this@Input, "Data berhasil di update", Toast.LENGTH_SHORT).show()
                    startActivity(IntentToHome)
                }else{
                    Toast.makeText(this@Input, "Kolom Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }

    override fun onResume() {
        super.onResume()
//        getAllNotes()
    }

    private fun setEmptyField() {
        with(binding) {
            txtTitle.setText("")
            txtDate.setText("")
            txtDesc.setText("")
        }
    }

    private fun validateInput(): Boolean {
        with(binding) {
            if(txtDate.text.toString()!="" && txtTitle.text.toString()!="" && txtDesc.text.toString()!=""){
                return true
            }else{
                return false
            }
        }

    }
}