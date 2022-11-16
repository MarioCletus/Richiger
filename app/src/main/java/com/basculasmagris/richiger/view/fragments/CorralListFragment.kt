package com.basculasmagris.richiger.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
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
import com.basculasmagris.richiger.databinding.FragmentCorralListBinding
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.CorralRemote
import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.model.entities.EstablishmentRemote
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.activities.CorralData
import com.basculasmagris.richiger.view.activities.EstablishmentData
import com.basculasmagris.richiger.view.activities.MergedLocalData
import com.basculasmagris.richiger.view.adapter.CorralAdapter
import com.basculasmagris.richiger.view.adapter.EstablishmentAdapter
import com.basculasmagris.richiger.viewmodel.*

class CorralListFragment : Fragment() {

    private lateinit var mBinding: FragmentCorralListBinding

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as EnsiladoraApplication).corralRepository)
    }
    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((requireActivity().application as EnsiladoraApplication).establishmentRepository)
    }

    private var mLocalEstablishments: MutableList<Establishment>? = null
    private var mLocalCorrals: MutableList<Corral>? = null

    private var mCorralViewModelRemote: CorralRemoteViewModel? = null
    private var mEstablishmentViewModelRemote: EstablishmentRemoteViewModel? = null

    private var mProgressDialog: Dialog? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mEstablishmentViewModel.allEstablishmentList) {
            if (it != null) {
                liveDataMerger.value = EstablishmentData(it)
            }
        }
        liveDataMerger.addSource(mCorralViewModel.allCorralList) {
            if (it != null) {
                liveDataMerger.value = CorralData(it)
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
                    is EstablishmentData -> mLocalEstablishments = it.establishments.toMutableList()
                    is CorralData -> mLocalCorrals = it.corrals.toMutableList()
                    else -> ""
                }

                if (mLocalEstablishments != null && mLocalCorrals != null) {
                    Log.i("Sync", "CORR Cantidad de corrales: ${mLocalCorrals?.size}")
                    Log.i("Sync", "CORR Cantidad de estableci: ${mLocalEstablishments?.size}")

                    liveData.removeObserver(this)
                    liveData.value = null
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
        mBinding = FragmentCorralListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocalData()
        getLocalCorral()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_corral_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_corral)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvCorralsList.adapter as CorralAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvCorralsList.adapter as CorralAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_corral -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateCorralActivity::class.java))
                        goToAddUpdateCorral()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun getLocalCorral(){
        mBinding.rvCorralsList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val corralAdapter =  CorralAdapter(this@CorralListFragment)
        mBinding.rvCorralsList.adapter = corralAdapter
        mCorralViewModel.allCorralList.observe(viewLifecycleOwner) { corrals ->
            corrals?.let{ _corral ->
                if (_corral.isNotEmpty()) {
                    mLocalCorrals = _corral.toMutableList()
                    mBinding.rvCorralsList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE
                    Log.i("Sync", "Se actualiza el adapter con la cantidad: ${_corral.size}")
                    (mBinding.rvCorralsList.adapter as CorralAdapter).corralList(_corral.filter { corral ->
                        corral.archiveDate.isNullOrEmpty()
                    })
                } else {
                    mBinding.rvCorralsList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }

        mEstablishmentViewModel.allEstablishmentList.observe(viewLifecycleOwner) { establishments ->
            establishments.let{ _establishments ->
                if (_establishments?.isNotEmpty() == true){
                    mBinding.tvNoData.visibility = View.GONE
                    mLocalEstablishments = _establishments.toMutableList()
                    _establishments?.filter { establishment ->
                        establishment.archiveDate.isNullOrEmpty()
                    }?.let {
                        Log.i("SYNC", "Se actualiza UI establecimientos")
                    }
                } else {
                }
            }
        }


    }

    fun goToCorralDetails(corral: Corral){
        findNavController().navigate(CorralListFragmentDirections.actionCorralListFragmentToCorralDetailFragment(
            corral
        ))
    }
    fun goToAddUpdateCorral(){
        mLocalEstablishments?.let { _localEstablishments ->
            if (_localEstablishments.isEmpty()){
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.no_establishment),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(CorralListFragmentDirections.actionCorralListFragmentToAddUpdateCorralActivity())
            }
        }
    }
    fun deleteCorral(corral: Corral){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_corral))
        builder.setMessage(resources.getString(R.string.msg_delete_corral_dialog, corral.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mCorralViewModel.delete(corral)
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
        mEstablishmentViewModelRemote?.establishmentsResponse?.value = null
        mEstablishmentViewModelRemote?.establishmentsLoadingError?.value = null
        mEstablishmentViewModelRemote?.loadEstablishment?.value = null
        mEstablishmentViewModelRemote?.addEstablishmentsResponse?.value = null
        mEstablishmentViewModelRemote?.addEstablishmentErrorResponse?.value = null
        mEstablishmentViewModelRemote?.addEstablishmentsLoad?.value = null
        mEstablishmentViewModelRemote?.updateEstablishmentsResponse?.value = null
        mEstablishmentViewModelRemote?.updateEstablishmentsErrorResponse?.value = null
        mEstablishmentViewModelRemote?.updateEstablishmentsLoad?.value = null

        mCorralViewModelRemote?.corralsResponse?.value = null
        mCorralViewModelRemote?.corralsLoadingError?.value = null
        mCorralViewModelRemote?.loadCorral?.value = null
        mCorralViewModelRemote?.addCorralsResponse?.value = null
        mCorralViewModelRemote?.addCorralErrorResponse?.value = null
        mCorralViewModelRemote?.addCorralsLoad?.value = null
        mCorralViewModelRemote?.updateCorralsResponse?.value = null
        mCorralViewModelRemote?.updateCorralsErrorResponse?.value = null
        mCorralViewModelRemote?.updateCorralsLoad?.value = null

        //mEstablishmentViewModelRemote = null
        //mCorralViewModelRemote = null
        mLocalEstablishments = null
        mLocalCorrals = null
    }

}