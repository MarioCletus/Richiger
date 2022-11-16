package com.basculasmagris.richiger.view.adapter

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.databinding.ItemLineDietProductBinding
import com.basculasmagris.richiger.model.entities.DietProductDetail
import android.text.Editable

import android.text.TextWatcher
import android.util.Log
import android.view.*

import com.basculasmagris.richiger.view.activities.MainActivity

import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.utils.Helper
import kotlin.math.round
import kotlin.math.roundToInt


class DietProductAdapter (
    private  val activity: Activity,
    private var usePercentage: Boolean,
    private var imgVerify: ImageView?,
    private var summaryWeight: EditText?,
    private var summaryPercentage: EditText?,
    private var readOnly: Boolean = false) : RecyclerView.Adapter<DietProductAdapter.ViewHolder>(),
    Filterable {

    private var originalProducts: MutableList<DietProductDetail> = ArrayList()
    private var dietProducts: MutableList<DietProductDetail> = ArrayList()
    private var filteredDietProducts: MutableList<DietProductDetail> = ArrayList()

    class ViewHolder (view: ItemLineDietProductBinding) : RecyclerView.ViewHolder(view.root){

        val tvProductName = view.tvProductName
        val tvProductDescription = view.tvProductDescription
        val etPercentage = view.etPercentage
        val etWeight = view.etWeight
        val tvOrder = view.tvProductOrder
        val tiWight = view.tiWeight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineDietProductBinding = ItemLineDietProductBinding.inflate(
            LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    fun verifyPercentage() {
        var currentTotalPercentage = 0.0
        var currentTotalWeight = 0.0
        dietProducts.forEach { dietProductDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual del orden ${dietProductDetail.order} ${dietProductDetail.weight} / ${dietProductDetail.percentage}")

            if (dietProductDetail.percentage >= 0){
                currentTotalPercentage += dietProductDetail.percentage
            }
            if (dietProductDetail.weight >= 0){
                currentTotalWeight += dietProductDetail.weight
            }
        }

        Log.i("FAT", "PORCENTAJE: Valor actual $currentTotalPercentage / 100")
        if (currentTotalPercentage.roundToInt() != 100 ){
            imgVerify?.setColorFilter(ContextCompat.getColor(activity, R.color.red_500_light), android.graphics.PorterDuff.Mode.SRC_IN);

        } else {
            imgVerify?.setColorFilter(ContextCompat.getColor(activity, R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    fun updatePercentageToItem(order: Int, percentage: Double){
        val itemToUpdate = dietProducts.first {
            it.order == order
        }

        if (itemToUpdate != null){
            itemToUpdate.percentage = percentage
        }
    }

    fun updateWeightToItem(order: Int, weight: Double){
        val itemToUpdate = dietProducts.first {
            it.order == order
        }

        if (itemToUpdate != null){
            Log.i("FAT", "Se actualiza el orden $order con valor $weight")
            itemToUpdate.weight = weight
        }
    }

    fun updateTotalPercentage(){
        var currentTotalPercentage = 0.0
        dietProducts.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.percentage >= 0){
                currentTotalPercentage += roundCorralDetail.percentage
            }

        }
        summaryPercentage?.setText(currentTotalPercentage.roundToInt().toString())
    }

    fun updateTotalWeight(){
        var currentTotalWeight = 0.0
        dietProducts.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
        }
        summaryWeight?.setText(currentTotalWeight.toString())
    }

    fun updatePercentageUse(with: Boolean){
        usePercentage = with
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dietProduct = dietProducts[position]
        holder.etPercentage.setText(dietProduct.percentage.toString())
        holder.etWeight.setText(dietProduct.weight.toString())
        holder.tvProductName.text = dietProduct.productName
        holder.tvProductDescription.text = if (dietProduct.productDescription.isEmpty()) "Sin descripción" else dietProduct.productDescription
        holder.tvOrder.text = dietProduct.order.toString()
        holder.etWeight.isEnabled = !usePercentage and !readOnly

        if (usePercentage) {
            holder.tiWight.visibility = View.INVISIBLE
            holder.etWeight.visibility  = View.INVISIBLE
        } else {
            holder.tiWight.visibility = View.VISIBLE
            holder.etWeight.visibility  = View.VISIBLE
        }


        holder.etWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().toDoubleOrNull() != null && !usePercentage){
                    dietProducts[holder.adapterPosition].weight = s.toString().toDouble()
                    updateTotalWeight()
                    updateTotalPercentage()
                } else {
                    if (!usePercentage){
                        dietProducts[holder.adapterPosition].weight = 0.0
                        updateTotalWeight()
                        updateTotalPercentage()
                    }

                }

            }
        })


        holder.etPercentage.isEnabled = usePercentage and !readOnly
        holder.etPercentage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.i("FAT", "EL valor del campo es ${s.toString().toDoubleOrNull()} / $usePercentage")
                if (s.toString().toDoubleOrNull() != null && usePercentage){
                    dietProducts[holder.adapterPosition].percentage = s.toString().toDouble()
                    var newValue = "0.0"
                    newValue = Helper.getNumberWithDecimals(1000 * s.toString().toDouble() / 100, 2)
                    //holder.etWeight.setText(newValue, TextView.BufferType.EDITABLE);
                    //Log.i("FAT", "Total 1000. El nuevo valor es de peso es $newValue")
                    //dietProducts[holder.adapterPosition].weight = newValue.toDouble()
                    updateTotalPercentage()
                    //updateTotalWeight()
                    verifyPercentage()
                } else if (s.toString().toDoubleOrNull() == null) {
                    if (usePercentage){
                        dietProducts[holder.adapterPosition].percentage = 0.0
                        //holder.etWeight.setText("0.0", TextView.BufferType.EDITABLE);
                        //dietProducts[holder.adapterPosition].weight = 0.0
                        updateTotalPercentage()
                        //updateTotalWeight()
                        verifyPercentage()
                    }
                }
            }
        })
    }

    override fun getItemCount(): Int {

        return dietProducts.size
    }

    fun dietList(list: List<DietProductDetail>){
        dietProducts = list.toMutableList()
        filteredDietProducts = list.toMutableList()
        originalProducts = list.toMutableList()
        notifyDataSetChanged()
    }

    fun updateList(list: List<DietProductDetail>){
        dietProducts = list.toMutableList()
        filteredDietProducts = list.toMutableList()
        notifyDataSetChanged()
    }

    fun getItems(): List<DietProductDetail> {
        return dietProducts.sortedBy {
            it.order
        }
    }

    fun getOriginalItems(): List<DietProductDetail> {
        return originalProducts.sortedBy {
            it.order
        }
    }

    fun addItem(dietProductDetail: DietProductDetail) {
        dietProducts.add(dietProductDetail)
        notifyDataSetChanged()
    }

    fun getItemFrom(position: Int): DietProductDetail {
        return dietProducts[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredDietProducts = dietProducts else {
                    val filteredList = ArrayList<DietProductDetail>()
                    dietProducts
                        .filter {
                            (it.productName.lowercase().contains(charString.lowercase()))
                        }
                        .forEach { filteredList.add(it) }
                    filteredDietProducts = filteredList

                }
                return FilterResults().apply { values = dietProducts }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                dietProducts = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<DietProductDetail>
                notifyDataSetChanged()
            }
        }
    }

    fun onItemSwiped(position: Int) {
        Log.i("FAT", "onItemSwiped $position size ${dietProducts.size}")
        dietProducts.removeAt(position)
        //filteredDietProducts.removeAt(position)
        Log.i("FAT", "onItemSwiped $position size ${dietProducts.size}")

        var currentTotalWeight = 0.0
        var currentTotalPercentage = 0.0
        dietProducts.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
            if (roundCorralDetail.percentage >= 0){
                currentTotalPercentage += roundCorralDetail.percentage
            }
        }

        for (index in 0 until dietProducts.size){

            if (index >= position){
                dietProducts[index].order = dietProducts[index].order-1
            }

            Log.i("FAT", "Se actualiza al borrar ${usePercentage} ${dietProducts[index].weight} / $currentTotalWeight")
            if (usePercentage){

            } else {
                dietProducts[index].percentage = Helper.getNumberWithDecimals(dietProducts[index].weight * 100 / currentTotalWeight,2).toDouble()
            }

        }

        notifyDataSetChanged()
    }

}