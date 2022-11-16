package com.basculasmagris.richiger.view.adapter

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.databinding.ItemLineRoundCorralBinding
import com.basculasmagris.richiger.model.entities.RoundCorralDetail
import android.text.Editable

import android.text.TextWatcher
import android.util.Log
import android.view.*

import com.basculasmagris.richiger.view.activities.MainActivity

import android.view.View
import android.view.View.GONE
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.utils.Helper
import kotlin.math.round
import kotlin.math.roundToInt


class RoundCorralAdapter (
    private  val activity: Activity,
    private var usePercentage: Boolean,
    private var totalWeight: Double,
    private var imgVerify: ImageView?,
    private var summaryPercentage: EditText?,
    private var summaryWeight: EditText?,
    private var tvTotalWeight: EditText?,
    private var readOnly: Boolean = false

) : RecyclerView.Adapter<RoundCorralAdapter.ViewHolder>(),
    Filterable {

    private var originalCorrals: MutableList<RoundCorralDetail> = ArrayList()
    private var roundCorrals: MutableList<RoundCorralDetail> = ArrayList()
    private var filteredRoundCorrals: MutableList<RoundCorralDetail> = ArrayList()

    class ViewHolder (view: ItemLineRoundCorralBinding) : RecyclerView.ViewHolder(view.root){
        val tvCorralName = view.tvCorralName
        val tvCorralDescription = view.tvCorralDescription
        val etPercentage = view.etPercentage
        val etWeight = view.etWeight
        val tvOrder = view.tvCorralOrder
        val fbAdjust = view.fbAdjust
        val tvCorralAnimalCount = view.tvCorralAnimalCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLineRoundCorralBinding = ItemLineRoundCorralBinding.inflate(
            LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    fun calcPercentage(){
        var totalWeight = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            if (roundCorralDetail.weight >= 0){
                totalWeight += roundCorralDetail.weight
            }
        }

        roundCorrals.forEach { roundCorralDetail ->

            if (roundCorralDetail.weight >= 0 && totalWeight > 0){
                roundCorralDetail.percentage = round((roundCorralDetail.weight * 100 / totalWeight))
            } else {
                roundCorralDetail.percentage = 0.0
            }
        }
    }


    fun verifyPercentageAndWeight() {
        var currentTotalPercentage = 0.0
        var currentTotalWeight = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual del orden ${roundCorralDetail.order} ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.percentage >= 0){
                currentTotalPercentage += roundCorralDetail.percentage
            }
            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
        }

        Log.i("FAT", "PORCENTAJE: Valor actual $currentTotalPercentage / 100")
        Log.i("FAT", "PESO: Valor actual $currentTotalWeight / $totalWeight")
        if (currentTotalPercentage.roundToInt() != 100 || currentTotalWeight.roundToInt() != totalWeight.roundToInt()){
            imgVerify?.setColorFilter(ContextCompat.getColor(activity, R.color.red_500_light), android.graphics.PorterDuff.Mode.SRC_IN);

        } else {
            imgVerify?.setColorFilter(ContextCompat.getColor(activity, R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

     fun updateTotalPercentage(){
         var currentTotalPercentage = 0.0
         roundCorrals.forEach { roundCorralDetail ->
             Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

             if (roundCorralDetail.percentage >= 0){
                 currentTotalPercentage += roundCorralDetail.percentage
             }

         }
         summaryPercentage?.setText(currentTotalPercentage.roundToInt().toString())
     }

    fun updateTotalWeight(){
        var currentTotalWeight = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
        }
        summaryWeight?.setText(currentTotalWeight.toString())
        tvTotalWeight?.setText(currentTotalWeight.toString())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundCorral = roundCorrals[position]
        holder.etPercentage.setText(roundCorral.percentage.toString())
        holder.etWeight.setText(roundCorral.weight.toString())
        holder.tvCorralName.text = roundCorral.corralName
        holder.tvOrder.text = roundCorral.order.toString()
        holder.etWeight.isEnabled = !usePercentage and !readOnly
        holder.tvCorralAnimalCount.text = roundCorral.animalQuantity.toString()
        holder.tvCorralDescription.text = if (roundCorral.corralDescription.isEmpty()) activity.getString(R.string.no_description) else roundCorral.corralDescription
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
                    roundCorrals[holder.adapterPosition].weight = s.toString().toDouble()
                    if (totalWeight == 0.0 && s.toString().toDouble() == 0.0) {
                        return
                    }

                    var newValue = "0.0"
                    Log.i("FAT", "Total $totalWeight")
                    newValue = (s.toString().toDouble()  * 100 / totalWeight).roundToInt().toString()
                    Log.i("FAT", "Total $totalWeight. El nuevo valor en % es $newValue")
                    //holder.etPercentage.setText(newValue, TextView.BufferType.EDITABLE);
                    //roundCorrals[holder.adapterPosition].percentage = newValue.toDouble()
                    updateTotalWeight()
                    updateTotalPercentage()

                } else if (s.toString().toDoubleOrNull() == null) {

                    if (!usePercentage){
                        roundCorrals[holder.adapterPosition].weight = 0.0
                        //holder.etPercentage.setText("0.0", TextView.BufferType.EDITABLE);
                        //roundCorrals[holder.adapterPosition].percentage = 0.0
                        updateTotalWeight()
                        updateTotalPercentage()
                    }
                }
                verifyPercentageAndWeight()

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
                if (s.toString().toDoubleOrNull() != null && usePercentage){
                    roundCorrals[holder.adapterPosition].percentage = s.toString().toDouble()
                    var newValue = "0.0"
                    newValue = Helper.getNumberWithDecimals(totalWeight * s.toString().toDouble() / 100, 2)
                    holder.etWeight.setText(newValue, TextView.BufferType.EDITABLE);
                    Log.i("FAT", "Total $totalWeight. El nuevo valor es de peso es $newValue")
                    roundCorrals[holder.adapterPosition].weight = newValue.toDouble()
                    updateTotalPercentage()
                    //updateTotalWeight()
                } else if (s.toString().toDoubleOrNull() == null) {

                    if (usePercentage){
                        roundCorrals[holder.adapterPosition].percentage = 0.0
                        holder.etWeight.setText("0.0", TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].weight = 0.0
                        updateTotalPercentage()
                        //updateTotalWeight()
                    }
                }

                verifyPercentageAndWeight()
                //updateTotal()
            }
        })

        holder.fbAdjust.setOnClickListener{ view ->
            var currentTotalPercentage = 0.0
            var currentTotalWeight = 0.0
            roundCorrals.forEach { roundCorralDetail ->
                if (roundCorralDetail.percentage >= 0){
                    currentTotalPercentage += roundCorralDetail.percentage
                }
                if (roundCorralDetail.weight >= 0){
                    currentTotalWeight += roundCorralDetail.weight
                }
            }

            Log.i("FAT", "Total Por: $currentTotalPercentage / Peso: $currentTotalWeight")
            if (usePercentage){
                val currentValue = holder.etPercentage.text.toString().toDouble()
                when {
                    currentTotalPercentage > 100.0 -> {
                        val diffValue = currentTotalPercentage-100.0
                        val newValue = currentValue-diffValue
                        holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].percentage = newValue
                    }
                    currentTotalPercentage < 100.0 -> {
                        val diffValue = 100-currentTotalPercentage
                        val newValue = currentValue+diffValue
                        holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].percentage = newValue
                    }
                    else -> {
                        holder.etPercentage.setText(currentValue.toString(), TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].percentage = currentValue
                    }
                }
            } else {
                val currentValue = holder.etWeight.text.toString().toDouble()
                when {
                    currentTotalWeight > totalWeight -> {
                        val diffValue = currentTotalWeight-totalWeight
                        var newValue = currentValue-diffValue
                        newValue = if (newValue < 0) 0.0 else newValue
                        holder.etWeight.setText(newValue.toString(), TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].weight = newValue
                    }
                    currentTotalWeight < totalWeight -> {
                        val diffValue = totalWeight-currentTotalWeight
                        var newValue = currentValue+diffValue
                        newValue = if (newValue < 0) 0.0 else newValue
                        holder.etWeight.setText(newValue.toString(), TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].weight = newValue
                    }
                    else -> {
                        holder.etWeight.setText(currentValue.toString(), TextView.BufferType.EDITABLE);
                        roundCorrals[holder.adapterPosition].weight = currentValue
                    }
                }

                /*
                if (currentTotalPercentage > 100.0) {
                    val diffValue = currentTotalPercentage-100.0
                    val newValue = currentValue-diffValue
                    holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE);
                    roundCorrals[holder.adapterPosition].percentage = newValue
                } else if (currentTotalPercentage < 100.0) {
                    val diffValue = 100-currentTotalPercentage
                    val newValue = currentValue+diffValue
                    holder.etPercentage.setText(newValue.toString(), TextView.BufferType.EDITABLE);
                    roundCorrals[holder.adapterPosition].percentage = newValue
                } */
            }
        }

        if (readOnly){
            holder.fbAdjust.visibility = GONE
        }
        verifyPercentageAndWeight()
        updateTotalPercentage()
        //updateTotalWeight()
    }

    override fun getItemCount(): Int {
        return roundCorrals.size
    }

    fun roundList(list: List<RoundCorralDetail>){
        roundCorrals.forEach {
            Log.i("FAT", "roundList ${it.corralName} ${it.weight} ${it.percentage}")
        }
        roundCorrals = list.toMutableList()
        filteredRoundCorrals = list.toMutableList()
        originalCorrals = list.toMutableList()
        notifyDataSetChanged()
    }

    fun updateList(list: List<RoundCorralDetail>){
        roundCorrals.forEach {
            Log.i("FAT", "updateList ${it.corralName} ${it.weight} ${it.percentage}")
        }
        roundCorrals = list.toMutableList()
        filteredRoundCorrals = list.toMutableList()
        notifyDataSetChanged()
    }

    fun update(_totalWeight: Double){
        totalWeight = _totalWeight
    }

    fun updatePercentageToItem(order: Int, percentage: Double){
        val itemToUpdate = roundCorrals.first {
            it.order == order
        }

        if (itemToUpdate != null){
            itemToUpdate.percentage = percentage
        }
    }

    fun updateWeightToItem(order: Int, weight: Double){
        val itemToUpdate = roundCorrals.first {
            it.order == order
        }

        if (itemToUpdate != null){
            Log.i("FAT", "Se actualiza el orden $order con valor $weight")
            itemToUpdate.weight = weight
        }
    }

    fun updatePercentageUse(with: Boolean){
        usePercentage = with
    }

    fun updateCustomPercentage(with: Double){
        //customPercentage = with
    }

    /*
    fun reOrderItems(): List<RoundCorralDetail>{
        var order = 1
        roundCorrals.forEach { roundCorralDetail ->
            roundCorralDetail.order = order
            order++
        }
        filteredRoundCorrals = roundCorrals
        notifyDataSetChanged()
        return roundCorrals
    }
    */


    fun getItems(): List<RoundCorralDetail> {
        return roundCorrals.sortedBy {
            it.order
        }
    }

    fun getOriginalItems(): List<RoundCorralDetail> {
        return originalCorrals.sortedBy {
            it.order
        }
    }

    fun addItem(roundCorralDetail: RoundCorralDetail) {
        roundCorrals.add(roundCorralDetail)
        roundCorrals.forEach {
            Log.i("FAT", "ADD ${it.corralName} ${it.weight} ${it.percentage}")
        }
        notifyDataSetChanged()
    }

    fun getItemFrom(position: Int): RoundCorralDetail {
        return roundCorrals[position]
    }

    /*
    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
*/

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredRoundCorrals = roundCorrals else {
                    val filteredList = ArrayList<RoundCorralDetail>()
                    roundCorrals
                        .filter {
                            (it.corralName.lowercase().contains(charString.lowercase()))
                        }
                        .forEach { filteredList.add(it) }
                    filteredRoundCorrals = filteredList

                }
                return FilterResults().apply { values = roundCorrals }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                roundCorrals = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<RoundCorralDetail>
                notifyDataSetChanged()
            }
        }
    }

    fun onItemSwiped(position: Int) {
        Log.i("FAT", "onItemSwiped $position size ${roundCorrals.size}")
        roundCorrals.removeAt(position)
        //filteredRoundCorrals.removeAt(position)
        Log.i("FAT", "onItemSwiped $position size ${roundCorrals.size}")

        var currentTotalWeight = 0.0
        var currentTotalPercentage = 0.0
        roundCorrals.forEach { roundCorralDetail ->
            Log.i("FAT", "roundCorralDetail: Valor actual ${roundCorralDetail.weight} / ${roundCorralDetail.percentage}")

            if (roundCorralDetail.weight >= 0){
                currentTotalWeight += roundCorralDetail.weight
            }
            if (roundCorralDetail.percentage >= 0){
                currentTotalPercentage += roundCorralDetail.percentage
            }
        }

        for (index in 0 until roundCorrals.size){
            if (index >= position){
                roundCorrals[index].order = roundCorrals[index].order-1
            }

            if (usePercentage){
                roundCorrals[index].weight = Helper.getNumberWithDecimals(roundCorrals[index].percentage * currentTotalWeight / 100,2).toDouble()
            } else {
                roundCorrals[index].percentage = Helper.getNumberWithDecimals(roundCorrals[index].weight * 100 / currentTotalWeight,2).toDouble()
            }

        }


        notifyDataSetChanged()
    }

}