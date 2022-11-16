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
import com.basculasmagris.richiger.databinding.FragmentEstablishmentListBinding
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.CorralRemote
import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.model.entities.EstablishmentRemote
import com.basculasmagris.richiger.view.activities.*
import com.basculasmagris.richiger.view.adapter.CorralAdapter
import com.basculasmagris.richiger.view.adapter.EstablishmentAdapter
import com.basculasmagris.richiger.viewmodel.*

class EstablishmentListFragment : Fragment() {

    private lateinit var mBinding: FragmentEstablishmentListBinding
    private var isSynchronizing = false

    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((requireActivity().application as EnsiladoraApplication).establishmentRepository)
    }

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((requireActivity().application as EnsiladoraApplication).corralRepository)
    }

    private var mProgressDialog: Dialog? = null

    private var mLocalEstablishments: List<Establishment>? = null
    private var mLocalCorrals: List<Corral>? = null

    private var mEstablishmentViewModelRemote: EstablishmentRemoteViewModel? = null
    private var mCorralViewModelRemote: CorralRemoteViewModel? = null

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
                    is EstablishmentData -> mLocalEstablishments = it.establishments
                    is CorralData -> mLocalCorrals = it.corrals
                    else -> ""
                }

                if (mLocalEstablishments != null && mLocalCorrals != null) {
                    Log.i("Sync", "EST Cantidad de corrales: ${mLocalCorrals?.size}")
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
        mBinding = FragmentEstablishmentListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocalData()
        getLocalEstablishment()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_establishment_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_establishment)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvEstablishmentsList.adapter as EstablishmentAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvEstablishmentsList.adapter as EstablishmentAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_establishment -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateEstablishmentActivity::class.java))
                        goToAddUpdateEstablishment()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Content
        //getLocalEstablishment()
    }

    private fun getLocalEstablishment(){
        mBinding.rvEstablishmentsList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val establishmentAdapter =  EstablishmentAdapter(this@EstablishmentListFragment)
        mBinding.rvEstablishmentsList.adapter = establishmentAdapter
        mEstablishmentViewModel.allEstablishmentList.observe(viewLifecycleOwner) { establishments ->
            establishments.let{ _establishments ->
                if (_establishments?.isNotEmpty() == true){
                    mLocalEstablishments = _establishments
                    _establishments?.filter { establishment ->
                        establishment.archiveDate.isNullOrEmpty()
                    }?.let {

                        if (it.isEmpty()){
                            mBinding.rvEstablishmentsList.visibility = View.GONE
                            mBinding.tvNoData.visibility = View.VISIBLE
                        } else {
                            Log.i("SYNC", "Se actualiza UI establecimientos: ${it.size} ")
                            mBinding.rvEstablishmentsList.visibility = View.VISIBLE
                            mBinding.tvNoData.visibility = View.GONE
                            establishmentAdapter.establishmentList(it)
                        }

                    }
                } else {
                    mBinding.rvEstablishmentsList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }

        mCorralViewModel.allCorralList.observe(viewLifecycleOwner) { corrals ->
            corrals.let{ _corrals ->
                if (_corrals?.isNotEmpty() == true){
                    mLocalCorrals = _corrals
                    _corrals?.filter { corral ->
                        corral.archiveDate.isNullOrEmpty()
                    }?.let {
                        Log.i("SYNC", "Se actualiza lista ade Corral: ${mLocalCorrals?.size}")

                        mLocalEstablishments?.let { _establishments ->
                            (mBinding.rvEstablishmentsList.adapter as EstablishmentAdapter).establishmentList(_establishments?.filter { establishment ->
                                establishment.archiveDate.isNullOrEmpty()
                            })
                        }
                    }
                }
            }
        }
    }

    fun goToEstablishmentDetails(establishment: Establishment){
        findNavController().navigate(EstablishmentListFragmentDirections.actionEstablishmentListFragmentToEstablishmentDetailFragment(
            establishment
        ))
    }

    fun goToAddUpdateEstablishment(){
        findNavController().navigate(EstablishmentListFragmentDirections.actionEstablishmentListFragmentToAddUpdateEstablishmentActivity())
    }

    fun deleteEstablishment(establishment: Establishment){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_establishment))
        builder.setMessage(resources.getString(R.string.msg_delete_establishment_dialog, establishment.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mEstablishmentViewModel.delete(establishment)
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

        mEstablishmentViewModelRemote = null
        mCorralViewModelRemote = null
        mLocalEstablishments = null
        mLocalCorrals = null
    }

}