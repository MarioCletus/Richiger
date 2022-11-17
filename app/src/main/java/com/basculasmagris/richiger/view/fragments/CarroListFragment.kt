package com.basculasmagris.richiger.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.FragmentCarroListBinding
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.Carro
import com.basculasmagris.richiger.model.entities.CarroRemote
import com.basculasmagris.richiger.view.activities.MergedLocalData
import com.basculasmagris.richiger.view.activities.MixerData
import com.basculasmagris.richiger.view.activities.CarroData
import com.basculasmagris.richiger.view.adapter.CarroAdapter
import com.basculasmagris.richiger.viewmodel.MixerRemoteViewModel
import com.basculasmagris.richiger.viewmodel.CarroRemoteViewModel
import com.basculasmagris.richiger.viewmodel.CarroViewModel
import com.basculasmagris.richiger.viewmodel.CarroViewModelFactory

class CarroListFragment : Fragment() {

    private lateinit var mBinding: FragmentCarroListBinding

    private val mCarroViewModel: CarroViewModel by viewModels {
        CarroViewModelFactory((requireActivity().application as EnsiladoraApplication).carroRepository)
    }
    private var mLocalCarros: List<Carro>? = null

    private lateinit var mCarroViewModelRemote: CarroRemoteViewModel
    private var mProgressDialog: Dialog? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mCarroViewModel.allCarroList) {
            if (it != null) {
                liveDataMerger.value = CarroData(it)
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
                    is CarroData -> mLocalCarros = it.carros
                    else -> ""
                }

                if (mLocalCarros != null) {
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
        mBinding = FragmentCarroListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start Sync
        getLocalData()
        getLocalCarro()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_carro_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_carro)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvCarrosList.adapter as CarroAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvCarrosList.adapter as CarroAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_carro -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateCarroActivity::class.java))
                        goToAddUpdateCarro()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun getLocalCarro(){
        mBinding.rvCarrosList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val carroAdapter =  CarroAdapter(this@CarroListFragment)
        mBinding.rvCarrosList.adapter = carroAdapter
        mCarroViewModel.allCarroList.observe(viewLifecycleOwner) {
                carros ->
            carros.let{
                if (it.isNotEmpty()){
                    mBinding.rvCarrosList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE
                    carroAdapter.carroList(it.filter { carro ->
                        carro.archiveDate.isNullOrEmpty()
                    })
                } else {
                    mBinding.rvCarrosList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToCarroDetails(carro: Carro){
        findNavController().navigate(CarroListFragmentDirections.actionCarroFragmentToCarroDetailFragment(
            carro
        ))
    }

    fun goToAddUpdateCarro(){
        findNavController().navigate(CarroListFragmentDirections.actionNavCarroToAddUpdateCarroActivity())
    }

    fun deleteCarro(carro: Carro){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_carro))
        builder.setMessage(resources.getString(R.string.msg_delete_carro_dialog, carro.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mCarroViewModel.delete(carro)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}