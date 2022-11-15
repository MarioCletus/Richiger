package com.basculasmagris.richiger.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.FragmentRoundDetailBinding
import com.basculasmagris.richiger.model.entities.Diet
import com.basculasmagris.richiger.model.entities.Round
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.activities.DietData
import com.basculasmagris.richiger.view.activities.MergedLocalData
import com.basculasmagris.richiger.view.activities.RoundData
import com.basculasmagris.richiger.view.adapter.RoundCorralAdapter
import com.basculasmagris.richiger.view.adapter.CorralAdapter
import com.basculasmagris.richiger.viewmodel.*
import com.bumptech.glide.Glide


class RoundDetailFragment : Fragment() {

    private var mBinding: FragmentRoundDetailBinding? = null
    private var mLocalDiets: List<Diet>? = null
    private var dietRound: Diet? = null
    private var currentRound: Round? = null

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as EnsiladoraApplication).corralRepository)
    }

    private val mRoundViewModel: RoundViewModel by viewModels {
        RoundViewModelFactory((requireActivity().application as EnsiladoraApplication).roundRepository)
    }

    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((requireActivity().application as EnsiladoraApplication).dietRepository)
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mDietViewModel.allDietList) {
            if (it != null) {

                liveDataMerger.value = DietData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(viewLifecycleOwner, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is DietData -> mLocalDiets = it.diets
                    else -> ""
                }

                if (mLocalDiets != null) {
                    val localDiets = mLocalDiets
                    dietRound = localDiets?.first {
                        it.id == currentRound?.dietId
                    }
                    setRoundValues()
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    private fun getLocalCorralByRound(roundId: Long){

        mBinding?.let { view ->
            view.rvCorralsList.layoutManager = GridLayoutManager(requireActivity(), 1)
            val corralAdapter =  RoundCorralAdapter(requireActivity(),
                usePercentage = false,
                totalWeight = 0.0,
                null,
                null,
                null,
                null,
                readOnly = true
            )
            view.rvCorralsList.adapter = corralAdapter
            mRoundViewModel.getCorralsBy(roundId).observe(viewLifecycleOwner) { corrals ->
                corrals.let{

                    if (corrals.isNotEmpty()){


                        if (corrals.isEmpty()){
                            view.tvCorralCount.text = getString(R.string.no_corrals)
                            view.tvNoAdditionalData.visibility = View.VISIBLE
                            view.rvCorralsList.visibility = View.GONE
                        } else {
                            view.tvCorralCount.text = getString(R.string.corral_round_count, corrals.size.toString())
                            view.rvCorralsList.visibility = View.VISIBLE
                            view.tvNoAdditionalData.visibility = View.GONE
                        }
                        corralAdapter.roundList(corrals)
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
        mBinding = FragmentRoundDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    fun setRoundValues(){
        mBinding?.let {
            currentRound?.let { round ->
                it.tvRoundTitle.text = round.name
                it.tvRoundDescription.text = round.description
                it.tvAdjustPercentage.text = "${round.customPercentage}%"
                it.tvRoundWeight.text = Helper.getFormattedWeightKg(round.weight, requireContext())
                it.tvDietName.text = dietRound?.name

                getLocalCorralByRound(round.id)

                if (round.remoteId > 0) {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.sync)
                } else {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.pending_sync)
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RoundDetailFragmentArgs by navArgs()
        args.roundDetail?.let { round ->
            currentRound = round
            getLocalData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}