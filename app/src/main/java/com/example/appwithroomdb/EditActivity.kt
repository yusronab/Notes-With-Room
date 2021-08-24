package com.example.appwithroomdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.appwithroomdb.databinding.ActivityEditBinding
import com.example.appwithroomdb.room.Constant
import com.example.appwithroomdb.room.Note
import com.example.appwithroomdb.room.NoteDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    private lateinit var binding: ActivityEditBinding
    private var getId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnSave()
        setupView()
    }

    private fun btnSave() {
        binding.buttonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNotes(
                    Note(0, binding.editTitle.text.toString(), binding.editNote.text.toString())
                )
                finish()
            }
        }
        binding.buttonUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNotes(
                        Note(getId, binding.editTitle.text.toString(), binding.editNote.text.toString())
                )
                finish()
            }
        }
    }

    private fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            Constant.TYPE_CREATE -> {
                binding.buttonUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                binding.buttonSave.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                getData()
            }
            Constant.TYPE_UPDATE -> {
                binding.buttonSave.visibility = View.GONE
                getData()
            }
        }
    }

    fun getData(){
        getId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val getAll = db.noteDao().getNote(getId)[0]
            binding.editTitle.setText(getAll.title)
            binding.editNote.setText(getAll.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}