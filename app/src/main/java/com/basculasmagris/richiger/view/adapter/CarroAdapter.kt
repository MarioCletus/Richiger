package com.basculasmagris.richiger.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.databinding.ItemCarroLayoutBinding
import com.basculasmagris.richiger.model.entities.Carro
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.activities.AddUpdateCarroActivity
import com.basculasmagris.richiger.view.fragments.CarroListFragment
import com.bumptech.glide.Glide

class CarroAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<CarroAdapter.ViewHolder>(),
    Filterable {

    private var carros: List<Carro> = listOf()
    private var filteredCarros: List<Carro> = listOf()

    class ViewHolder (view: ItemCarroLayoutBinding) : RecyclerView.ViewHolder(view.root) {
//        val ivCarroAvatar = view.ivCarroAvatar
        val tvCarroTitle = view.tvCarroTitle
        val ibMore = view.ibMore
        val tvCarroDescription = view.tvCarroDescription
//        val tvCarroSpecificWeight = view.tvCarroSpecificWeight

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCarroLayoutBinding = ItemCarroLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carro = filteredCarros[position]
        holder.tvCarroTitle.text = carro.name
//        holder.tvCarroDescription.text = if (carro.description.isEmpty()) fragment.getString(R.string.lbl_no_description_short) else carro.description
        fragment.context?.let {
//            holder.tvCarroSpecificWeight.text = Helper.getFormattedWeight(carro.specificWeight, it)
        }

//        if (carro.image.isNotEmpty()){
//            Glide.with(fragment)
//                .load(carro.image)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.ivCarroAvatar)
//        } else {
//            Glide.with(fragment)
//                .load(R.mipmap.ic_launcher)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.ivCarroAvatar)
//        }


        holder.itemView.setOnClickListener {
            if (fragment is CarroListFragment){
                fragment.goToCarroDetails(carro)
            }
        }

        holder.ibMore.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            // Si el carroo está sincronizado no se permite el borrado.
            if (carro.remoteId != 0L) {
                popup.menu.getItem(1).isVisible = false
            }

            popup.setOnMenuItemClickListener {
//                if (it.itemId  == R.id.action_edit_carro){
//                    val intent = Intent(fragment.requireActivity(), AddUpdateCarroActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_DETAILS, carro)
//                    fragment.requireActivity().startActivity(intent)
//                } else if (it.itemId == R.id.action_delete_carro){
//                    if (fragment is CarroListFragment) {
//                        fragment.deleteCarro(carro)
//                    }
//                }
                true
            }

            popup.show()
        }

        if (fragment is CarroListFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredCarros.size
    }

    fun carroList(list: List<Carro>){
        carros = list
        filteredCarros = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredCarros = carros else {
                    val filteredList = ArrayList<Carro>()
                    carros
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase()))
//                            or
//                                    (it.description.lowercase().contains(charString.lowercase()))
//
                        }
                        .forEach { filteredList.add(it) }
                    filteredCarros = filteredList

                }
                return FilterResults().apply { values = filteredCarros }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredCarros = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Carro>
                notifyDataSetChanged()
            }
        }
    }
}