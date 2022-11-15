package com.basculasmagris.richiger.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.FragmentMixerListBinding
import com.basculasmagris.richiger.model.entities.Mixer
import com.basculasmagris.richiger.model.entities.MixerRemote
import com.basculasmagris.richiger.view.activities.MergedLocalData
import com.basculasmagris.richiger.view.activities.MixerData
import com.basculasmagris.richiger.view.adapter.MixerAdapter
import com.basculasmagris.richiger.viewmodel.*

class MixerListFragment : Fragment() {

    private lateinit var mBinding: FragmentMixerListBinding

    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((requireActivity().application as EnsiladoraApplication).mixerRepository)
    }

    private var mMixerViewModelRemote: MixerRemoteViewModel? = null
    private var mLocalMixers: List<Mixer>? = null

    private var mProgressDialog: Dialog? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mMixerViewModel.allMixerList) {
            if (it != null) {
                liveDataMerger.value = MixerData(it)
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
                    is MixerData -> mLocalMixers = it.mixers
                    else -> ""
                }

                if (mLocalMixers != null) {
                    liveData.removeObserver(this)
                }
            }
        })
    }

    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }
    private fun hideCustomProgressDialog(){
        mProgressDialog?.let {
            it.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMixerListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start Sync
        getLocalData()
        getLocalMixer()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_mixer_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_mixer)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvMixersList.adapter as MixerAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvMixersList.adapter as MixerAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_mixer -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateMixerActivity::class.java))
                        goToAddUpdateMixer()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun getLocalMixer(){
        mBinding.rvMixersList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val mixerAdapter =  MixerAdapter(this@MixerListFragment)
        mBinding.rvMixersList.adapter = mixerAdapter
        mMixerViewModel.allMixerList.observe(viewLifecycleOwner) { mixers ->
            mixers.let{ _mixers ->
                if (_mixers.isNotEmpty()){
                    mBinding.rvMixersList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE
                    mixerAdapter.mixerList(_mixers.filter { mixer ->
                        mixer.archiveDate.isNullOrEmpty()
                    })
                } else {
                    mBinding.rvMixersList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }
    fun goToMixerDetails(mixer: Mixer){
        findNavController().navigate(MixerListFragmentDirections.actionMixerListFragmentToMixerDetailFragment(
            mixer
        ))
    }
    fun goToAddUpdateMixer(){
        findNavController().navigate(MixerListFragmentDirections.actionMixerListFragmentToAddUpdateMixerActivity())
    }
    fun deleteMixer(mixer: Mixer){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_mixer))
        builder.setMessage(resources.getString(R.string.msg_delete_mixer_dialog, mixer.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mMixerViewModel.delete(mixer)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanObservers()
    }

    private fun cleanObservers(){
        mMixerViewModelRemote?.mixersResponse?.value = null
        mMixerViewModelRemote?.mixersLoadingError?.value = null
        mMixerViewModelRemote?.loadMixer?.value = null
        mMixerViewModelRemote?.addMixersResponse?.value = null
        mMixerViewModelRemote?.addMixerErrorResponse?.value = null
        mMixerViewModelRemote?.addMixersLoad?.value = null
        mMixerViewModelRemote?.updateMixersResponse?.value = null
        mMixerViewModelRemote?.updateMixersErrorResponse?.value = null
        mMixerViewModelRemote?.updateMixersLoad?.value = null

        mMixerViewModelRemote = null
        mLocalMixers = null
    }
}