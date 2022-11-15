package com.basculasmagris.richiger.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ItemCorralLayoutBinding
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.view.activities.AddUpdateCorralActivity
import com.basculasmagris.richiger.view.fragments.CorralListFragment
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModel
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModelFactory

class CorralAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<CorralAdapter.ViewHolder>(),
    Filterable {

    private var corrals: List<Corral> = listOf()
    private var filteredCorrals: List<Corral> = listOf()
    private val mEstablishmentViewModel: EstablishmentViewModel by fragment.viewModels {
        EstablishmentViewModelFactory((fragment.requireActivity().application as EnsiladoraApplication).establishmentRepository)
    }

    private fun getLocalEstablishment(holder: ViewHolder, corral: Corral){
        mEstablishmentViewModel.allEstablishmentList.observe(fragment.requireActivity()) { establishments ->

            val establishment =  establishments.first {
                it.id == corral.establishmentId
            }

            establishment?.let { _establishment ->
                holder.tvEstablishmentName.text = _establishment.name
            }
        }
    }

    class ViewHolder (view: ItemCorralLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvCorralTitle = view.tvCorralTitle
        val ibMore = view.ibMore
        val tvCorralDescription = view.tvCorralDescription
        val tvEstablishmentName = view.tvEstablishmentName
        val tvAnimalQuantity = view.tvAnimalQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCorralLayoutBinding = ItemCorralLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corral = filteredCorrals[position]
        getLocalEstablishment(holder, corral)
        holder.tvCorralTitle.text = corral.name
        holder.tvCorralDescription.text = if (corral.description.isEmpty()) fragment.getString(R.string.lbl_no_description_short) else corral.description
        holder.tvAnimalQuantity.text = corral.animalQuantity.toString()
        holder.itemView.setOnClickListener {
            if (fragment is CorralListFragment){
                fragment.goToCorralDetails(corral)
            }
        }

        holder.ibMore.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter_corral, popup.menu)

            // Si el corralo está sincronizado no se permite el borrado.
            if (corral.remoteId != 0L) {
                popup.menu.getItem(1).isVisible = false
            }

            popup.setOnMenuItemClickListener {
                if (it.itemId  == R.id.action_edit_corral){
                    val intent = Intent(fragment.requireActivity(), AddUpdateCorralActivity::class.java)
                    intent.putExtra(Constants.EXTRA_CORRAL_DETAILS, corral)
                    fragment.requireActivity().startActivity(intent)
                } else if (it.itemId == R.id.action_delete_corral){
                    if (fragment is CorralListFragment) {
                        fragment.deleteCorral(corral)
                    }
                }
                true
            }

            popup.show()
        }

        if (fragment is CorralListFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredCorrals.size
    }

    fun corralList(list: List<Corral>){
        corrals = list
        filteredCorrals = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredCorrals = corrals else {
                    val filteredList = ArrayList<Corral>()
                    corrals
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredCorrals = filteredList

                }
                return FilterResults().apply { values = filteredCorrals }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredCorrals = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Corral>
                notifyDataSetChanged()
            }
        }
    }
}