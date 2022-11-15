package com.basculasmagris.richiger.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.databinding.FragmentHomeBinding
import com.basculasmagris.richiger.databinding.FragmentProductListBinding
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    //private lateinit var homeViewModel: HomeViewModel
    //private var _binding: FragmentHomeBinding? = null
    private lateinit var mBinding: FragmentHomeBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this@HomeFragment)
            .load(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(mBinding.ivProductAvatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }

}