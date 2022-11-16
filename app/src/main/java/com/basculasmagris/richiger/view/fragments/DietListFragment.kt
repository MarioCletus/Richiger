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
import com.basculasmagris.richiger.databinding.FragmentDietListBinding
import com.basculasmagris.richiger.model.entities.*
import com.basculasmagris.richiger.view.activities.*
import com.basculasmagris.richiger.view.adapter.ProductAdapter
import com.basculasmagris.richiger.view.adapter.DietAdapter
import com.basculasmagris.richiger.viewmodel.*

class DietListFragment : Fragment() {

    private lateinit var mBinding: FragmentDietListBinding

    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((requireActivity().application as EnsiladoraApplication).dietRepository)
    }

    private var mProgressDialog: Dialog? = null

    private var mLocalDiets: List<Diet>? = null
    private var mDietViewModelRemote: DietRemoteViewModel? = null
    private var mProductViewModelRemote: ProductRemoteViewModel? = null

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
        mBinding = FragmentDietListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocalData()
        getLocalDiet()

        // Navigation Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_diet_list, menu)

                // Associate searchable configuration with the SearchView
                val search = menu.findItem(R.id.search_diet)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        (mBinding.rvDietsList.adapter as DietAdapter).filter.filter(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (mBinding.rvDietsList.adapter as DietAdapter).filter.filter(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_add_diet -> {
                        // clearCompletedTasks()
                        //startActivity(Intent(requireActivity(), AddUpdateDietActivity::class.java))
                        goToAddUpdateDiet()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun getLocalDiet(){

        mBinding.rvDietsList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val dietAdapter =  DietAdapter(this@DietListFragment)
        mBinding.rvDietsList.adapter = dietAdapter

        mDietViewModel.allDietList.observe(viewLifecycleOwner) { diets ->
            diets.let{ _diets ->
                if (_diets?.isNotEmpty() == true){
                    mLocalDiets = _diets
                    _diets?.filter { diet ->
                        diet.archiveDate.isNullOrEmpty()
                    }?.let {

                        if (it.isEmpty()){
                            mBinding.rvDietsList.visibility = View.GONE
                            mBinding.tvNoData.visibility = View.VISIBLE
                        } else {
                            Log.i("SYNC", "Se actualiza UI Dietas: ${it.size} ")
                            mBinding.rvDietsList.visibility = View.VISIBLE
                            mBinding.tvNoData.visibility = View.GONE
                            dietAdapter.dietList(it)
                        }
                    }
                } else {
                    mBinding.rvDietsList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToDietDetails(diet: Diet){
        findNavController().navigate(DietListFragmentDirections.actionDietListFragmentToDietDetailFragment(
            diet
        ))
    }

    fun goToAddUpdateDiet(){
        findNavController().navigate(DietListFragmentDirections.actionDietListFragmentToAddUpdateDietActivity())
    }

    fun deleteDiet(diet: Diet){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_diet))
        builder.setMessage(resources.getString(R.string.msg_delete_diet_dialog, diet.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mDietViewModel.delete(diet)
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
        mDietViewModelRemote?.dietsResponse?.value = null
        mDietViewModelRemote?.dietsLoadingError?.value = null
        mDietViewModelRemote?.loadDiet?.value = null
        mDietViewModelRemote?.addDietsResponse?.value = null
        mDietViewModelRemote?.addDietErrorResponse?.value = null
        mDietViewModelRemote?.addDietsLoad?.value = null
        mDietViewModelRemote?.updateDietsResponse?.value = null
        mDietViewModelRemote?.updateDietsErrorResponse?.value = null
        mDietViewModelRemote?.updateDietsLoad?.value = null

        mProductViewModelRemote?.productsResponse?.value = null
        mProductViewModelRemote?.productsLoadingError?.value = null
        mProductViewModelRemote?.loadProduct?.value = null
        mProductViewModelRemote?.addProductsResponse?.value = null
        mProductViewModelRemote?.addProductErrorResponse?.value = null
        mProductViewModelRemote?.addProductsLoad?.value = null
        mProductViewModelRemote?.updateProductsResponse?.value = null
        mProductViewModelRemote?.updateProductsErrorResponse?.value = null
        mProductViewModelRemote?.updateProductsLoad?.value = null

        mDietViewModelRemote = null
        mProductViewModelRemote = null
        mLocalDiets = null
    }

}