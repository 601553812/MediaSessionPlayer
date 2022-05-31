package com.pxh.myMediaSessionPlayer.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pxh.myMediaSessionPlayer.ClickListener
import com.pxh.myMediaSessionPlayer.viewModel.MyViewModel
import com.pxh.myMediaSessionPlayer.model.SongBean
import com.pxh.myMediaSessionPlayer.databinding.PlayerFragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var myViewModel: MyViewModel
    private lateinit var binding: PlayerFragmentMainBinding
    private lateinit var clickListener: ClickListener

    override fun onAttach(activity: Context) {
        clickListener = activity as ClickListener
        super.onAttach(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel = activity?.let { ViewModelProvider(it) }?.get(MyViewModel::class.java)!!
        val songObserver = Observer<SongBean>{
            binding.songBean = it
        }
        myViewModel.song.observe(this,songObserver)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerFragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.songBean = myViewModel.getSong()
        binding.btPlay.setOnClickListener {
            clickListener.playListener()
        }
        binding.btPrev.setOnClickListener {
            clickListener.prevListener()
        }
        binding.btNext.setOnClickListener {
            clickListener.nextListener()
        }
        binding.btPlaylist.setOnClickListener {
            clickListener.fragmentChange()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}