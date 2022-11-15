package com.basculasmagris.richiger.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ItemEstablishmentLayoutBinding
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.activities.AddUpdateEstablishmentActivity
import com.basculasmagris.richiger.view.activities.CorralData
import com.basculasmagris.richiger.view.activities.EstablishmentData
import com.basculasmagris.richiger.view.activities.MergedLocalData
import com.basculasmagris.richiger.view.fragments.EstablishmentListFragment
import com.basculasmagris.richiger.viewmodel.CorralViewModel
import com.basculasmagris.richiger.viewmodel.CorralViewModelFactory
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModel
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModelFactory
import com.bumptech.glide.Glide

class EstablishmentAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<EstablishmentAdapter.ViewHolder>(),
    Filterable {

    private var establishments: List<Establishment> = listOf()
    private var filteredEstablishments: List<Establishment> = listOf()
    private var mLocalCorrals: List<Corral>? = null

    private val mCorralViewModel: CorralViewModel by fragment.viewModels {
        CorralViewModelFactory((fragment.requireActivity().application as EnsiladoraApplication).corralRepository)
    }

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mCorralViewModel.allCorralList) {
            if (it != null) {
                liveDataMerger.value = CorralData(it)
            }
        }
        return liveDataMerger
    }
    private fun getLocalData(holder: ViewHolder, establishment: Establishment){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(fragment.requireActivity(), object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is CorralData -> mLocalCorrals = it.corrals
                    else -> ""
                }

                if (mLocalCorrals != null) {
                    Log.i("SYNC", "ADP Cantidad de corrales ${mLocalCorrals?.size}")
                    getLocalCorral(holder, establishment)
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    private fun getLocalCorral(holder: ViewHolder, establishment: Establishment){
        val corralCount = mLocalCorrals?.filter { corral ->
            corral.establishmentId == establishment.id
        }
        holder.tvCorralQuantity.text = corralCount?.size.toString()
    }

    class ViewHolder (view: ItemEstablishmentLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvEstablishmentTitle = view.tvEstablishmentTitle
        val ibMore = view.ibMore
        val tvEstablishmentDescription = view.tvEstablishmentDescription
        val tvCorralQuantity = view.tvCorralQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEstablishmentLayoutBinding = ItemEstablishmentLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val establishment = filteredEstablishments[position]
        getLocalData(holder, establishment)
        holder.tvEstablishmentTitle.text = establishment.name
        holder.tvEstablishmentDescription.text = if (establishment.description.isEmpty()) fragment.getString(R.string.lbl_no_description_short) else establishment.description

        holder.itemView.setOnClickListener {
            if (fragment is EstablishmentListFragment){
                fragment.goToEstablishmentDetails(establishment)
            }
        }

        holder.ibMore.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter_establishment, popup.menu)

            // Si el establishmento está sincronizado no se permite el borrado.
            if (establishment.remoteId != 0L) {
                popup.menu.getItem(1).isVisible = false
            }

            popup.setOnMenuItemClickListener {
                if (it.itemId  == R.id.action_edit_establishment){
                    val intent = Intent(fragment.requireActivity(), AddUpdateEstablishmentActivity::class.java)
                    intent.putExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS, establishment)
                    fragment.requireActivity().startActivity(intent)
                } else if (it.itemId == R.id.action_delete_establishment){
                    if (fragment is EstablishmentListFragment) {
                        fragment.deleteEstablishment(establishment)
                    }
                }
                true
            }

            popup.show()
        }



        if (fragment is EstablishmentListFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredEstablishments.size
    }

    fun establishmentList(list: List<Establishment>){
        establishments = list
        filteredEstablishments = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredEstablishments = establishments else {
                    val filteredList = ArrayList<Establishment>()
                    establishments
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredEstablishments = filteredList

                }
                return FilterResults().apply { values = filteredEstablishments }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredEstablishments = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Establishment>
                notifyDataSetChanged()
            }
        }
    }
}