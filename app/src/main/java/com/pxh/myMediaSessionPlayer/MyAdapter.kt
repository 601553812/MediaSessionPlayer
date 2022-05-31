package com.pxh.myMediaSessionPlayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.pxh.myMediaSessionPlayer.model.SongBean
import java.util.ArrayList



class MyAdapter(private val songList: ArrayList<SongBean>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),R.layout.player_recycler_item,parent,false)
           return    ViewHolder(view)
        }

    fun setClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
        override fun getItemCount(): Int = songList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener{onItemClickListener.onItemClick(songList[position])}
            val binding : ViewDataBinding = holder.dataBinding
            binding.setVariable(BR.song,songList[position])
        }

    open class ViewHolder(var dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root)
    interface OnItemClickListener{
        fun onItemClick(song: SongBean)
    }

    }
