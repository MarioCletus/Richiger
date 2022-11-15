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
import com.basculasmagris.richiger.databinding.ItemProductLayoutBinding
import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.activities.AddUpdateProductActivity
import com.basculasmagris.richiger.view.fragments.ProductListFragment
import com.bumptech.glide.Glide

class ProductAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(),
    Filterable {

    private var products: List<Product> = listOf()
    private var filteredProducts: List<Product> = listOf()

    class ViewHolder (view: ItemProductLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivProductAvatar = view.ivProductAvatar
        val tvProductTitle = view.tvProductTitle
        val ibMore = view.ibMore
        val tvProductDescription = view.tvProductDescription
        val tvProductSpecificWeight = view.tvProductSpecificWeight

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemProductLayoutBinding = ItemProductLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = filteredProducts[position]
        holder.tvProductTitle.text = product.name
        holder.tvProductDescription.text = if (product.description.isEmpty()) fragment.getString(R.string.lbl_no_description_short) else product.description
        fragment.context?.let {
            holder.tvProductSpecificWeight.text = Helper.getFormattedWeight(product.specificWeight, it)
        }

        if (product.image.isNotEmpty()){
            Glide.with(fragment)
                .load(product.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivProductAvatar)
        } else {
            Glide.with(fragment)
                .load(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivProductAvatar)
        }


        holder.itemView.setOnClickListener {
            if (fragment is ProductListFragment){
                fragment.goToProductDetails(product)
            }
        }

        holder.ibMore.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            // Si el producto está sincronizado no se permite el borrado.
            if (product.remoteId != 0L) {
                popup.menu.getItem(1).isVisible = false
            }

            popup.setOnMenuItemClickListener {
                if (it.itemId  == R.id.action_edit_product){
                    val intent = Intent(fragment.requireActivity(), AddUpdateProductActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_DETAILS, product)
                    fragment.requireActivity().startActivity(intent)
                } else if (it.itemId == R.id.action_delete_product){
                    if (fragment is ProductListFragment) {
                        fragment.deleteProduct(product)
                    }
                }
                true
            }

            popup.show()
        }

        if (fragment is ProductListFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredProducts.size
    }

    fun productList(list: List<Product>){
        products = list
        filteredProducts = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredProducts = products else {
                    val filteredList = ArrayList<Product>()
                    products
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredProducts = filteredList

                }
                return FilterResults().apply { values = filteredProducts }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredProducts = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Product>
                notifyDataSetChanged()
            }
        }
    }
}