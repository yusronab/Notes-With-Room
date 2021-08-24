package com.example.appwithroomdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appwithroomdb.databinding.AdapterMainBinding
import com.example.appwithroomdb.room.Note

class MainAdapter (var note: ArrayList<Note>, private var listener: OnAdapterListener) : RecyclerView.Adapter<MainAdapter.UserViewHolder>() {

    inner class UserViewHolder(var binding: AdapterMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(AdapterMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.apply {
            textTitle.text = note[position].title
            textTitle.setOnClickListener {
                listener.onCLick(note[position])
            }

            iconEdit.setOnClickListener {
                listener.onUpdate(note[position])
            }

            iconDelete.setOnClickListener {
                listener.onDelete(note[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return note.size
    }

    fun setData(data: List<Note>){
        note.clear()
        note.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onCLick(note: Note)
        fun onUpdate(note: Note)
        fun onDelete(note: Note)
    }
}