package com.basculasmagris.richiger.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.FragmentCorralDetailBinding
import com.basculasmagris.richiger.view.adapter.CustomListItem
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModel
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModelFactory

class CorralDetailFragment : Fragment() {

    private var mBinding: FragmentCorralDetailBinding? = null
    private var establishmentId = 0L

    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((requireActivity().application as EnsiladoraApplication).establishmentRepository)
    }

    private fun getLocalEstablishment(){
        mEstablishmentViewModel.allEstablishmentList.observe(requireActivity()) {
            establishments ->
            val establishment =  establishments.first {
                it.id == establishmentId
            }

            establishment?.let {
                mBinding?.tvEstablishment?.text = it.name
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentCorralDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CorralDetailFragmentArgs by navArgs()
        Log.i("Corral name", args.corralDetail.name)

        args.corralDetail?.let { corral ->
            mBinding?.let {

                it.tvCorralTitle.text = corral.name
                it.tvCorralDescription.text = if (corral.description.isEmpty()) resources.getString(R.string.lbl_no_description) else corral.description
                it.tvNoAdditionalData.text = resources.getString(R.string.tv_no_additional_data)
                it.tvAnimalQuantity.text = corral.animalQuantity.toString()
                it.tvCorralRfid.text = corral.rfid.toString()
                establishmentId = corral.establishmentId

                if (corral.remoteId > 0) {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.sync)
                } else {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.pending_sync)
                }
            }

            getLocalEstablishment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}