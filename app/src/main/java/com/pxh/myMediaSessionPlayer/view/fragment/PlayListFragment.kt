package com.pxh.myMediaSessionPlayer.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pxh.myMediaSessionPlayer.*
import com.pxh.myMediaSessionPlayer.model.SongBean
import com.pxh.myMediaSessionPlayer.viewModel.MyViewModel


class PlayListFragment : Fragment() {

    private lateinit var myViewModel: MyViewModel
    private lateinit var clickListener: ClickListener


    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        clickListener = activity as ClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel = activity?.let { ViewModelProvider(it) }?.get(MyViewModel::class.java)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.player_fragment_playlist, container, false)
        val myAdapter = MyAdapter(myViewModel.getSongList())
        myAdapter.setClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(song: SongBean) {
                clickListener.playUriListener(song.id)
            }
        })
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
        view.findViewById<Button>(R.id.bt_back)?.setOnClickListener {
            clickListener.fragmentBack()
        }
        view.findViewById<Switch>(R.id.sw_reverse)?.setOnClickListener {
            clickListener.reverse()
            myAdapter.notifyDataSetChanged()
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}