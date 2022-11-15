package com.basculasmagris.richiger.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.databinding.ItemMixerLayoutBinding
import com.basculasmagris.richiger.model.entities.Mixer
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.activities.AddUpdateMixerActivity
import com.basculasmagris.richiger.view.fragments.MixerListFragment
import com.bumptech.glide.Glide

class MixerAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<MixerAdapter.ViewHolder>(),
    Filterable {

    private var mixers: List<Mixer> = listOf()
    private var filteredMixers: List<Mixer> = listOf()

    class ViewHolder (view: ItemMixerLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvMixerTitle = view.tvMixerTitle
        val ibMore = view.ibMore
        val tvMixerDescription = view.tvMixerDescription
        val ivTara = view.ivTara
        val tvTara = view.tvTara
        val ivCalibration = view.ivCalibration
        val tvCalibration = view.tvCalibration
        val tvMac = view.tvMac
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMixerLayoutBinding = ItemMixerLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mixer = filteredMixers[position]
        holder.tvMixerTitle.text = mixer.name
        holder.tvMixerDescription.text = if (mixer.description.isEmpty()) fragment.getString(R.string.lbl_no_description_short) else mixer.description
        holder.tvMac.text = mixer.mac

        /*Glide.with(fragment)
            .load(R.drawable.ic_calibration)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivCalibration)*/
        holder.tvCalibration.text = mixer.tara.toString()

       /* Glide.with(fragment)
            .load(R.drawable.ic_tara)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivTara)*/
        holder.tvCalibration.text = mixer.calibration.toString()
        holder.tvTara.text = mixer.tara.toString()

        holder.itemView.setOnClickListener {
            if (fragment is MixerListFragment){
                fragment.goToMixerDetails(mixer)
            }
        }

        holder.ibMore.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter_mixer, popup.menu)

            // Si el mixero está sincronizado no se permite el borrado.
            if (mixer.remoteId != 0L) {
                popup.menu.getItem(1).isVisible = false
            }

            popup.setOnMenuItemClickListener {
                if (it.itemId  == R.id.action_edit_mixer){
                    val intent = Intent(fragment.requireActivity(), AddUpdateMixerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_MIXER_DETAILS, mixer)
                    fragment.requireActivity().startActivity(intent)
                } else if (it.itemId == R.id.action_delete_mixer){
                    if (fragment is MixerListFragment) {
                        fragment.deleteMixer(mixer)
                    }
                }
                true
            }

            popup.show()
        }

        if (fragment is MixerListFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredMixers.size
    }

    fun mixerList(list: List<Mixer>){
        mixers = list
        filteredMixers = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredMixers = mixers else {
                    val filteredList = ArrayList<Mixer>()
                    mixers
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredMixers = filteredList

                }
                return FilterResults().apply { values = filteredMixers }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredMixers = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Mixer>
                notifyDataSetChanged()
            }
        }
    }
}