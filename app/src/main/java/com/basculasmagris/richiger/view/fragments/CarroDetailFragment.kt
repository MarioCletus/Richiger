package com.basculasmagris.richiger.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.databinding.FragmentCarroDetailBinding
import com.basculasmagris.richiger.databinding.FragmentCarroListBinding
import com.basculasmagris.richiger.utils.Helper
import com.bumptech.glide.Glide

class CarroDetailFragment : Fragment() {

    private var mBinding: FragmentCarroDetailBinding? = null
    private var mImagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentCarroDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CarroDetailFragmentArgs by navArgs()
        Log.i("Carro name", args.carroDetail.name)

        args.carroDetail?.let { carro ->
            mBinding?.let {
//                mImagePath = carro.image
                Glide.with(this@CarroDetailFragment)
                    .load(mImagePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(it.ivCarroAvatar)

                it.tvCarroTitle.text = carro.name
//                it.tvCarroDescription.text = if (carro.description.isEmpty()) resources.getString(R.string.lbl_no_description) else carro.description
//                it.tvCarroSpecificWeight.text = Helper.getFormattedWeight(carro.specificWeight, requireActivity())
//                it.tvCarroRefid.text = if (carro.rfid > 0) carro.rfid.toString() else resources.getString(R.string.lbl_empty)
                it.tvNoAdditionalData.text = resources.getString(R.string.tv_no_additional_data)

                if (carro.remoteId > 0) {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.sync)
                } else {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.pending_sync)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}