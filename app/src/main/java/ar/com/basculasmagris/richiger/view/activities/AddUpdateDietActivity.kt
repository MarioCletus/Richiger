package com.basculasmagris.richiger.view.activities

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ActivityAddUpdateDietBinding
import com.basculasmagris.richiger.databinding.DialogCustomListBinding
import com.basculasmagris.richiger.model.entities.Diet
import com.basculasmagris.richiger.model.entities.DietProduct
import com.basculasmagris.richiger.model.entities.DietProductDetail
import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.adapter.CustomListItem
import com.basculasmagris.richiger.view.adapter.CustomListItemAdapter
import com.basculasmagris.richiger.view.adapter.DietProductAdapter
import com.basculasmagris.richiger.view.adapter.RoundCorralAdapter
import com.basculasmagris.richiger.viewmodel.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class AddUpdateDietActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDietBinding

    private var mImagePath: String = ""
    private lateinit var mCustomListDialog : Dialog
    private var mNewDietDetails: Diet? = null
    private var dietProducts: DietProductDetail? = null
    private var currentDiet: Diet? = null
    private var positionFrom = -1
    private var positionTo = -1
    private var saveNewDiet = false

    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((application as EnsiladoraApplication).dietRepository)
    }

    private lateinit var mDietViewModelRemote: DietRemoteViewModel
    private var mProgressDialog: Dialog? = null

    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((this.application as EnsiladoraApplication).productRepository)
    }

    private var mLocalDietProductDetail: List<DietProductDetail>? = null
    private var mLocalProduct: List<Product>? = null
    private val mCustomListItem: ArrayList<CustomListItem> = ArrayList<CustomListItem>()
    private var dietProductAdapter: DietProductAdapter? = null

    // Drop and drag

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                Log.i("FAT", "onMove")
                //getting the adapter
                val adapter = recyclerView.adapter as DietProductAdapter

                //the position from where iteSwipeRecyclerViewHelperCallbackm has been moved
                val from = viewHolder.adapterPosition

                //the position where the item is moved
                val to = target.adapterPosition

                if (positionFrom == -1){
                    positionFrom = from
                }

                positionTo = to

                Log.i("FAT", "from ${positionFrom} to ${positionTo}")
                //telling the adapter to move the item
                adapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.i("FAT", "onSwiped")

                if ((binding.rvDietProductList.adapter as DietProductAdapter).getItems().size == 1) {
                    binding.rvDietProductList.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.llTotal.visibility = View.GONE
                }

                dietProductAdapter?.onItemSwiped(viewHolder.layoutPosition)

            }


            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                Log.i("FAT", "onSelectedChanged")
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
                }
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int = makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START or ItemTouchHelper.END
            )


            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                Log.i("FAT", "clearView")
                viewHolder?.itemView?.setBackgroundColor(Color.WHITE)

                val adapter = recyclerView.adapter as DietProductAdapter

                if (positionFrom != -1 || positionTo != -1){
                    if (positionFrom < positionTo){

                        for (index in positionFrom..positionTo){
                            val dietProductDetailFrom = adapter.getItemFrom(position = index)
                            var newOrder = index
                            if (index == positionFrom){
                                newOrder = positionTo+1
                            }
                            dietProductDetailFrom.order = newOrder

                            Log.i("FAT", "Se actualiza ${dietProductDetailFrom.productName} con " +
                                    "\n --> orden: ${newOrder}" +
                                    "\n --> weight: ${dietProductDetailFrom.weight}" +
                                    "\n --> percentage: ${dietProductDetailFrom.percentage}" +
                                    "")
                        }
                    } else {
                        Log.i("FAT", "clearView ${positionFrom} to ${positionTo}")
                        for (index in positionFrom downTo positionTo){

                            val dietProductDetailFrom = adapter.getItemFrom(position = index)
                            Log.i("FAT", "index ${index}")
                            var newOrder = dietProductDetailFrom.order + 1
                            if (index == positionFrom){
                                newOrder = positionTo+1
                            }

                            dietProductDetailFrom.order = newOrder

                            Log.i("FAT", "Se actualiza ${dietProductDetailFrom.productName} con " +
                                    "\n --> orden: ${newOrder} / ${adapter.getItemFrom(position = index).order}" +
                                    "\n --> weight: ${dietProductDetailFrom.weight}" +
                                    "\n --> percentage: ${dietProductDetailFrom.percentage}" +
                                    "")
                        }
                    }

                    adapter.updateList(adapter.getItems())
                }

                positionFrom = -1
                positionTo = -1
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }


    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(this)
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

    private fun fetchLocalData(dietId: Long): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mDietViewModel.getProductsBy(dietId)) {
            if (it != null) {
                liveDataMerger.value = DietProductDetailData(it)
            }
        }
        liveDataMerger.addSource(mProductViewModel.allProductList) {
            if (it != null) {
                liveDataMerger.value = ProductData(it)
            }
        }
        return liveDataMerger
    }

    private fun getLocalData(dietId: Long){
        // Sync local data
        val liveData = fetchLocalData(dietId)
        liveData.observe(this, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is DietProductDetailData -> mLocalDietProductDetail = it.dietProductsDetail
                    is ProductData -> mLocalProduct = it.products
                    else -> ""
                }

                if (mLocalDietProductDetail != null && mLocalProduct != null) {

                    mLocalProduct?.forEach {
                        if (it.archiveDate.isNullOrEmpty()){
                            val item = CustomListItem(it.id, it.remoteId, it.name, it.description, R.drawable.ic_product)
                            mCustomListItem.add(item)
                        }
                    }
                    setDietProducts()
                    liveData.removeObserver(this)
                    liveData.value = null
                }
            }
        })
    }

    fun setDietProducts(){
        mLocalDietProductDetail?.let {
            val adapter = binding.rvDietProductList.adapter as DietProductAdapter


            if (it.isNotEmpty()){
                binding.rvDietProductList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.llTotal.visibility = View.VISIBLE
                adapter.dietList(it)
            } else {
                binding.rvDietProductList.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
                binding.llTotal.visibility = View.GONE
            }

            var totalWeight = 0.0
            var totalPercentage = 0.0
            it.forEach { dietProductDetail ->
                totalWeight += dietProductDetail.weight
                totalPercentage += dietProductDetail.percentage
            }
            binding.etSummaryWeight.setText(Helper.getNumberWithDecimals(totalWeight, 2))
            binding.etSummaryPer.setText(Helper.getNumberWithDecimals(totalPercentage, 0))
            adapter.updateTotalWeight()
            adapter.verifyPercentage()

        }


    }

    fun saveDiet(){
        val dietName = binding.tiDietName.text.toString().trim { it <= ' ' }
        val dietDescription = binding.tiDietDescription.text.toString().trim { it <= ' ' }
        val usePercentage = binding.switchPercentage.isChecked
        var remoteId: Long = 0

        currentDiet?.let {
            remoteId = it.remoteId
        }

        when {
            TextUtils.isEmpty(dietName) -> {
                Toast.makeText(
                    this@AddUpdateDietActivity,
                    resources.getString(R.string.err_msg_diet_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                var dietId = 0L
                var updatedDate = ""

                currentDiet?.let {
                    if (it.id != 0L){
                        dietId = it.id
                        updatedDate = Date().toString()
                    }
                }

                val diet = Diet(
                    dietName,
                    dietDescription,
                    remoteId,
                    updatedDate,
                    "",
                    usePercentage,
                    dietId
                )

                mNewDietDetails = diet


                runBlocking {
                    if (dietId == 0L){
                        //mDietViewModelRemote.addDietFromAPI(diet)
                        //saveNewDiet = true
                        val dietId = mDietViewModel.insertSync(diet)
                        saveProductsInDiet(dietId)
                    } else {
                        // Se actualizan datos base de la dieta
                        mDietViewModel.updateSync(diet)
                        Log.i("SYNC", "Se actualiza dieta con fecha ${diet.updatedDate}")
                        saveProductsInDiet(dietId)
                        //mDietViewModelRemote.updateDietFromAPI(diet)
                    }

                    finish()
                }



            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentDietId: Long = 0

        binding = ActivityAddUpdateDietBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_DIET_DETAILS)){
            currentDiet = intent.getParcelableExtra(Constants.EXTRA_DIET_DETAILS)
        }

        // Navigation Menu
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_add_update_diet, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_save_diet -> {
                        saveDiet()
                        return true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)


        binding.rvDietProductList.layoutManager = GridLayoutManager(this, 1)

        setupActionBar()

        currentDiet?.let {
            if (it.id != 0L){
                currentDietId = it.id
                binding.tiDietName.setText(it.name)
                binding.tiDietDescription.setText(it.description)
                binding.switchPercentage.isChecked = it.usePercentage
                binding.etSummaryWeight.visibility = if (it.usePercentage) View.INVISIBLE else View.VISIBLE
                binding.tiSummaryWeight.visibility = if (it.usePercentage) View.INVISIBLE else View.VISIBLE
            }
        }



        dietProductAdapter =  DietProductAdapter(
            this@AddUpdateDietActivity,
            binding.switchPercentage.isChecked,
            binding.imgVerify,
            binding.etSummaryWeight,
            binding.etSummaryPer
        )
        binding.rvDietProductList.adapter = dietProductAdapter
        itemTouchHelper.attachToRecyclerView(binding.rvDietProductList);

        binding.switchPercentage.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked){
                binding.etSummaryWeight.visibility = View.INVISIBLE
                binding.tiSummaryWeight.visibility = View.INVISIBLE
            } else {
                binding.etSummaryWeight.visibility = View.VISIBLE
                binding.tiSummaryWeight.visibility = View.VISIBLE
            }
            binding.rvDietProductList.forEach {
                var etPer = it.findViewById<EditText>(R.id.et_percentage)
                var etWeight = it.findViewById<EditText>(R.id.et_weight)
                var tiWeight = it.findViewById<TextInputLayout>(R.id.ti_weight)

                if (isChecked){
                    etPer.isEnabled = true
                    etWeight.isEnabled = false
                    etWeight.visibility = View.INVISIBLE
                    tiWeight.visibility = View.INVISIBLE
                } else {
                    etWeight.isEnabled = true
                    etPer.isEnabled = false
                    etWeight.visibility = View.VISIBLE
                    tiWeight.visibility = View.VISIBLE
                }

                (binding.rvDietProductList.adapter as DietProductAdapter).updatePercentageUse(isChecked)

            }
        }

        binding.etSummaryWeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, afRoundCorralAdapterter: Int
            ) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var newTotal = 0.0
                val adapter = (binding.rvDietProductList.adapter as DietProductAdapter)
                if (s.toString().toDoubleOrNull() != null){
                    newTotal = s.toString().toDouble()
                    //adapter.update(s.toString().toDouble())
                } else {
                    //adapter.update(0.0)
                    newTotal = 0.0
                }

                Log.i("FAT", "etSummaryWeight Nuevo total: $newTotal / Cant.: ${binding.rvDietProductList.size} / Cant. Adapter: ${adapter.getItems().size}")

                binding.rvDietProductList.forEach {
                    var etPer = it.findViewById<EditText>(R.id.et_percentage)
                    var etWeight = it.findViewById<EditText>(R.id.et_weight)
                    var etOrder = it.findViewById<TextView>(R.id.tv_product_order)
                    val orderValue = etOrder.text.toString().toInt()
                    Log.i("FAT", "etSummaryWeight orderValue: $orderValue")
                    Log.i("FAT", "etSummaryWeight ---------------------")

                    if (binding.switchPercentage.isChecked){
                        if (etPer.text.toString().toDoubleOrNull() != null){
                            val currentPercentageValue = etPer.text.toString().toDouble()
                            val newWeightValue = Helper.getNumberWithDecimals(currentPercentageValue * newTotal / 100, 2)
                            etWeight.setText(newWeightValue)
                            adapter.updateWeightToItem(orderValue, newWeightValue.toDouble())
                            adapter.verifyPercentage()
                        } else {
                            etWeight.setText("0")
                            adapter.updateWeightToItem(orderValue, 0.0)
                            adapter.verifyPercentage()
                        }
                    } else {

                        if (etWeight.text.toString().toDoubleOrNull() != null){
                            val currentWeightValue = etWeight.text.toString().toDouble()
                            val newPercentageValue = Helper.getNumberWithDecimals((currentWeightValue * 100 / newTotal), 2)
                            Log.i("FAT", "etSummaryWeight currentWeightValue: $currentWeightValue / $newPercentageValue")
                            etPer.setText(newPercentageValue);
                            adapter.updatePercentageToItem(orderValue, newPercentageValue.toDouble())
                            adapter.verifyPercentage()
                        } else {
                            etPer.setText("0.0");
                            adapter.updatePercentageToItem(orderValue, 0.0)
                            adapter.verifyPercentage()
                        }

                        //roundCorrals[holder.adapterPosition].percentage = newValue.toDouble()
                    }

                }


            }
        })

        /*
        mDietViewModel.allDietList.observe(this, { diets ->

            Log.i("FAT", "Flag de guardado $saveNewDiet / ${diets.size}")

            if (saveNewDiet) {
                Log.i("FAT", "saveProductsInDiet 02 ${diets[0].id}")
                saveProductsInDiet(diets[0].id)
                finish()
            }
        })

         */



        getLocalData(currentDietId)
        binding.btnAddProductToDiet.setOnClickListener(this)

        mDietViewModelRemote = ViewModelProvider(this)[DietRemoteViewModel::class.java]
        dietViewModelRemoteObserver()

    }

    private fun dietViewModelRemoteObserver(){
        mDietViewModelRemote.addDietsLoad.observe(this, {
                loadDiet -> loadDiet?.let {

            if (loadDiet) {
                showCustomProgressDialog()
            } else {
                hideCustomProgressDialog()
            }
        }
        })
        mDietViewModelRemote.addDietsResponse.observe(this, {
                dietsResponse -> dietsResponse?.let { remoteDiet ->
            Log.i("Add diet ID", remoteDiet.id.toString())

            val diet = Diet(
                remoteDiet.name,
                remoteDiet.description,
                remoteDiet.id,
                "",
                remoteDiet.archiveDate,
                remoteDiet.usePercentage,
            )

            Log.i("FAT", "El id es ${diet.id}")
            mDietViewModel.insert(diet)

        }

        })
        mDietViewModelRemote.addDietErrorResponse.observe(this, {
                errorData -> errorData?.let {

            if (errorData) {
                Log.e("Add diets error", "$errorData")
                mNewDietDetails?.let {
                    val diet = Diet(
                        it.name,
                        it.description,
                        0,
                        "",
                        "",
                        it.usePercentage
                    )

                    Log.i("FAT [error]", "El id es ${diet.id}")
                    saveNewDiet = true
                    mDietViewModel.insert(diet)
                }
            }
        }
        })

        mDietViewModelRemote.updateDietsLoad.observe(this, {
                loadDiet -> loadDiet?.let {

            if (loadDiet) {
                showCustomProgressDialog()
            } else {
                hideCustomProgressDialog()
            }
        }
        })
        mDietViewModelRemote.updateDietsResponse.observe(this, {
                dietsResponse -> dietsResponse?.let { remoteDiet ->
            Log.i("Updated diet ID", remoteDiet.id.toString())
            finish()
        }
        })
        mDietViewModelRemote.updateDietsErrorResponse.observe(this, {
                errorData -> errorData?.let {

            if (errorData) {
                Log.e("Add diets error", "$errorData")
                mNewDietDetails?.let {
                    val diet = Diet(
                        it.name,
                        it.description,
                        it.remoteId,
                        Date().toString(),
                        "",
                        it.usePercentage,
                        it.id
                    )
                    mDietViewModel.update(diet)
                    finish()
                }
            }
        }
        })
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateDiet)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (currentDiet != null && currentDiet!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_diet)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_diet)
            }
        }

        binding.toolbarAddUpdateDiet.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(this)
        val dialogBinding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(dialogBinding.root)
        dialogBinding.tvTitle.text = title
        dialogBinding.rvList.layoutManager = LinearLayoutManager(this)

        val currentValues = (binding.rvDietProductList.adapter as DietProductAdapter).getItems()
        val availableValues = ArrayList<CustomListItem>()
        itemsList.forEach { itemList ->
            val alreadyExist = currentValues.firstOrNull{
                it.productId == itemList.id
            }
            if (alreadyExist == null){
                availableValues.add(itemList)
            }
        }

        val adapter = CustomListItemAdapter(this, availableValues, selection)
        dialogBinding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        when (selection){
            Constants.PRODUCT_REF -> {

                val dietProduct = DietProductDetail(
                    item.id,
                    item.remoteId,
                    item.name,
                    item.description,
                    0,
                    0,
                    0.0,
                    0.0,
                    (binding.rvDietProductList.adapter as DietProductAdapter).getItems().size+1)
                //mDietViewModel.insertDietProduct(dietProduct)
                Log.i("FAT", "Se ingresa al adapter " +
                        "\n --> Dieta / Producto: ${dietProduct.dietId} / ${dietProduct.productId} " +
                        "\n --> orden: ${dietProduct.order}" +
                        "\n --> weight: ${dietProduct.weight}" +
                        "\n --> percentage: ${dietProduct.percentage}" +
                        "")
                (binding.rvDietProductList.adapter as DietProductAdapter).addItem(dietProduct)

                binding.rvDietProductList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.llTotal.visibility = View.VISIBLE

                mCustomListDialog.dismiss()
            }
        }
    }

    private fun saveProductsInDiet(dietId : Long){
        Log.i("FAT", "Se guarda dieta con id $dietId ")
        // Se actualizan productos de las dietas.
        var originalProducts = (dietProductAdapter as DietProductAdapter).getOriginalItems()
        var updatedProducts = (dietProductAdapter as DietProductAdapter).getItems()
        var deletedProducts: ArrayList<DietProductDetail> = ArrayList()

        // Buscamos los productos que se borraron
        originalProducts.forEach { originalProduct ->
            val hasProduct = updatedProducts.any{ updatedProduct ->
                updatedProduct.productId == originalProduct.productId
            }

            if (!hasProduct){
                deletedProducts.add(originalProduct)
            }
        }

        // Se borran los productos que se quitaron de la dieta
        deletedProducts.forEach{ deletedProduct ->
            Log.i("FAT", "Se Borra " +
                    "\n --> Dieta / Producto: ${deletedProduct.dietId} / ${deletedProduct.productId} " +
                    "")
            mDietViewModel.deleteProductBy(deletedProduct.dietId, deletedProduct.productId)
        }

        // Se actualizan los productos que quedaron en la dieta
        updatedProducts.forEach {
            Log.i("FAT", "Se actualiza " +
                    "\n --> Dieta / Producto: $dietId / ${it.productId} " +
                    "\n --> orden: ${it.order}" +
                    "\n --> weight: ${it.weight}" +
                    "\n --> percentage: ${it.percentage}" +
                    "")

            val hasProduct = originalProducts.any{ originalProduct ->
                originalProduct.productId == it.productId
            }

            if (!hasProduct){
                val dietProduct = DietProduct(
                    dietId,
                    it.productId,
                    0,
                    0,
                    it.order,
                    it.weight,
                    it.percentage,
                    0,
                    "",
                    null)
                mDietViewModel.insertDietProduct(dietProduct)
            } else {
                mDietViewModel.updateProductBy(
                    dietId,
                    it.productId,
                    it.order,
                    it.weight,
                    it.percentage
                )
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.btn_add_product_to_diet -> {
                    customItemsLDialog("Productos disponibles", mCustomListItem, Constants.PRODUCT_REF)
                }
            }
        }
    }
}