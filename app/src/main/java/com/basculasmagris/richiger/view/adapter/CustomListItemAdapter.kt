package com.basculasmagris.richiger.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.databinding.ItemCustomListBinding
import com.basculasmagris.richiger.view.activities.AddUpdateCorralActivity
import com.basculasmagris.richiger.view.activities.AddUpdateDietActivity
import com.basculasmagris.richiger.view.activities.AddUpdateProductActivity
import com.basculasmagris.richiger.view.activities.AddUpdateRoundActivity

class CustomListItemAdapter (
    private val activity: Activity,
    private val listItems: List<CustomListItem>,
    private val selection: String) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root){
        val tvTxt = view.tvText
        val tvDescription = view.tvDescription
        val iconItem = view.iconItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvTxt.text = item.name
        if (item.resourceIcon > 0){
            holder.iconItem.setImageDrawable(activity.getDrawable(item.resourceIcon))
        } else {
            holder.iconItem.setImageDrawable(activity.getDrawable(R.drawable.ic_adjust))
        }

        holder.tvDescription.text = if (item.description.isEmpty()) activity.getString(R.string.no_description) else item.description

        holder.itemView.setOnClickListener{
            if (activity is AddUpdateCorralActivity){
                activity.selectedListItem(item, selection)
            }

            if (activity is AddUpdateDietActivity){
                activity.selectedListItem(item, selection)
            }

            if (activity is AddUpdateRoundActivity){
                activity.selectedListItem(item, selection)
            }

        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}

class CustomListItem {
    var id: Long = 0
    var remoteId: Long = 0
    var name: String = ""
    var description = ""
    var resourceIcon: Int = 0


    constructor(id: Long, remoteId: Long, name: String){
        this.id = id
        this.remoteId = remoteId
        this.name = name
    }

    constructor(id: Long, remoteId: Long, name: String, description: String, resourceIcon: Int){
        this.id = id
        this.remoteId = remoteId
        this.name = name
        this.description = description
        this.resourceIcon = resourceIcon
    }
}