package com.basculasmagris.richiger.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.FragmentEstablishmentDetailBinding
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.adapter.CorralAdapter
import com.basculasmagris.richiger.viewmodel.CorralViewModel
import com.basculasmagris.richiger.viewmodel.CorralViewModelFactory
import com.bumptech.glide.Glide


class EstablishmentDetailFragment : Fragment() {

    private var mBinding: FragmentEstablishmentDetailBinding? = null

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as EnsiladoraApplication).corralRepository)
    }

    private fun getLocalCorralByEstablishment(id: Long){

        mBinding?.let { view ->
            view.rvCorralsList.layoutManager = GridLayoutManager(requireActivity(), 3)
            val corralAdapter =  CorralAdapter(this@EstablishmentDetailFragment)
            view.rvCorralsList.adapter = corralAdapter
            mCorralViewModel.allCorralList.observe(viewLifecycleOwner) { corrals ->
                corrals.let{

                    if (corrals.isNotEmpty()){


                        val establishmentCorrals = it.filter { corral ->
                            corral.establishmentId == id
                        }

                        if (establishmentCorrals.isEmpty()){
                            view.tvCorralCount.text = getString(R.string.no_corrals)
                            view.tvNoAdditionalData.visibility = View.VISIBLE
                            view.rvCorralsList.visibility = View.GONE
                        } else {
                            view.tvCorralCount.text = getString(R.string.corral_establishment_count, establishmentCorrals.size.toString())
                            view.rvCorralsList.visibility = View.VISIBLE
                            view.tvNoAdditionalData.visibility = View.GONE
                        }
                        corralAdapter.corralList(establishmentCorrals)
                    } else {
                        view.rvCorralsList.visibility = View.GONE
                        view.tvNoAdditionalData.visibility = View.VISIBLE
                        view.tvCorralCount.text = getString(R.string.no_corrals)
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentEstablishmentDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: EstablishmentDetailFragmentArgs by navArgs()
        Log.i("Establishment name", args.establishmentDetail.name)

        args.establishmentDetail?.let { establishment ->
            mBinding?.let {

                it.tvEstablishmentTitle.text = establishment.name
                it.tvEstablishmentDescription.text = establishment.description
                getLocalCorralByEstablishment(establishment.id)

                if (establishment.remoteId > 0) {
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