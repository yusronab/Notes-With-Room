package com.example.appwithroomdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appwithroomdb.adapter.MainAdapter
import com.example.appwithroomdb.databinding.ActivityMainBinding
import com.example.appwithroomdb.room.Constant
import com.example.appwithroomdb.room.Note
import com.example.appwithroomdb.room.NoteDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mainAdapter: MainAdapter
    val db by lazy { NoteDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnCreate()
        setRecycler()
    }

    override fun onStart() {
        super.onStart()
        loadRecycler()
    }

    private fun loadRecycler(){
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNotes()
            withContext(Dispatchers.Main){
                mainAdapter.setData(notes)
            }
        }
    }

    private fun btnCreate() {
        binding.buttonCreate.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    private fun intentEdit(noteId: Int, intentTYpe: Int){
        startActivity(Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentTYpe)
        )
    }

    private fun setRecycler() {
        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onCLick(note: Note) {
                intentEdit(note.id, Constant.TYPE_READ)
            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(note: Note) {
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNotes(note)
                    loadRecycler()
                }
            }

        })
        binding.listNote.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}