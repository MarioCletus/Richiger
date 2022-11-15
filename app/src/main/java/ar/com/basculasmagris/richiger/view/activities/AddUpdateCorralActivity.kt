package com.basculasmagris.richiger.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ActivityAddUpdateCorralBinding
import com.basculasmagris.richiger.databinding.DialogCustomListBinding
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.view.adapter.CustomListItem
import com.basculasmagris.richiger.view.adapter.CustomListItemAdapter
import com.basculasmagris.richiger.view.adapter.EstablishmentAdapter
import com.basculasmagris.richiger.viewmodel.*
import kotlinx.coroutines.runBlocking
import java.util.*

class AddUpdateCorralActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateCorralBinding

    private var mImagePath: String = ""
    private lateinit var mCustomListDialog : Dialog
    private var mCorralDetails: Corral? = null
    private var mNewCorralDetails: Corral? = null
    private var selectedEstablishment: CustomListItem? = null

    private val mCorralViewModel: CorralViewModel by viewModels {
        CorralViewModelFactory((application as EnsiladoraApplication).corralRepository)
    }

    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((application as EnsiladoraApplication).establishmentRepository)
    }
    private val mEstablishmentList: ArrayList<CustomListItem> = ArrayList<CustomListItem>()

    private lateinit var mCorralViewModelRemote: CorralRemoteViewModel
    private var mProgressDialog: Dialog? = null

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

    private fun getLocalEstablishment(){
        mEstablishmentViewModel.allEstablishmentList.observe(this) {
                establishments ->
            Log.i("OBSERVER", "[add corral] mEstablishmentViewModel.allEstablishmentList")
            establishments.let{
                    establishments.forEach {

                        if (it.archiveDate.isNullOrEmpty()){
                            val item = CustomListItem(it.id, it.remoteId, it.name, it.description, R.drawable.ic_local_dining)
                            mEstablishmentList.add(item)
                        }

                    }

                    mCorralDetails?.let { it ->
                        if (it.id != 0L){
                            val establishment =  mEstablishmentList.filter { establishment ->
                                establishment.id == it.establishmentId
                            }
                            establishment.let {  _establishment ->
                                if (_establishment.isNotEmpty()){
                                    selectedEstablishment = _establishment[0]
                                    binding.tiEstablishmentRef.setText(_establishment[0].name)
                                } else {
                                    selectedEstablishment = mEstablishmentList[0]
                                    binding.tiEstablishmentRef.setText(mEstablishmentList[0].name)
                                }

                            }
                        }
                    }

                    if (mCorralDetails == null && mEstablishmentList.isNotEmpty()) {
                        selectedEstablishment = mEstablishmentList[0]
                        binding.tiEstablishmentRef.setText(mEstablishmentList[0].name)
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateCorralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_CORRAL_DETAILS)){
            mCorralDetails = intent.getParcelableExtra(Constants.EXTRA_CORRAL_DETAILS)
        }

        setupActionBar()

        // Obtenemos los establecimientos disponibles
        getLocalEstablishment()

        mCorralDetails?.let {
            if (it.id != 0L){
                binding.tiCorralName.setText(it.name)
                binding.tiCorralDescription.setText(it.description)
                binding.btnAddCorral.text = resources.getString(R.string.lbl_update_corral)
                binding.tiRfid.setText(it.rfid.toString())
                binding.tiAnimalQuantity.setText(it.animalQuantity.toString())
            }
        }

        binding.btnAddCorral.setOnClickListener(this)
        binding.tiEstablishmentRef.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateCorral)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mCorralDetails != null && mCorralDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_corral)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_corral)
            }
        }

        binding.toolbarAddUpdateCorral.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.ti_establishment_ref -> {
                    Log.i("corral TOQUÉ LISA", "corral de lista")
                    customItemsLDialog("Establecimientos disponibles", mEstablishmentList, Constants.ESTABLISHMENT_REF)
                }
                R.id.btn_add_corral -> {
                    var corralEstablishmentRef = 0L
                    var corralEstablishmentRemoteId = 0L
                    selectedEstablishment?.let {
                        corralEstablishmentRef = it.id
                        corralEstablishmentRemoteId = it.remoteId
                    }
                    val corralName = binding.tiCorralName.text.toString().trim { it <= ' ' }
                    val corralDescription = binding.tiCorralDescription.text.toString().trim { it <= ' ' }
                    val corralRfId = binding.tiRfid.text.toString().trim { it <= ' ' }
                    val corralAnimalQuantity = binding.tiAnimalQuantity.text.toString().trim { it <= ' ' }
                    var remoteId = 0L

                    mCorralDetails?.let {
                        remoteId = it.remoteId
                    }

                    when {
                        TextUtils.isEmpty(corralName) -> {
                            Toast.makeText(
                                this@AddUpdateCorralActivity,
                                resources.getString(R.string.err_msg_corral_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            var corralId = 0L
                            var updatedDate = ""

                            mCorralDetails?.let {
                                if (it.id != 0L){
                                    corralId = it.id
                                    updatedDate = Date().toString()
                                }
                            }

                            var corral = Corral(
                                corralEstablishmentRef,
                                corralEstablishmentRemoteId,
                                corralName,
                                corralDescription,
                                remoteId,
                                updatedDate,
                                "",
                                if (corralAnimalQuantity.isEmpty()) 0 else corralAnimalQuantity.toInt(),
                                if (corralRfId.isEmpty()) 0 else corralRfId.toLong(),
                                corralId
                            )

                            mNewCorralDetails = corral

                            runBlocking {
                                if (corralId == 0L){
                                    mCorralViewModel.insertSync(corral)
                                } else {
                                    mCorralViewModel.update(corral)
                                }

                                finish()
                            }

                        }
                    }
                }
            }
        }
    }

    private fun customItemsLDialog(title: String, itemsList: List<CustomListItem>, selection: String){
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: CustomListItem, selection: String){
        when (selection){
            Constants.ESTABLISHMENT_REF -> {
                selectedEstablishment = item
                binding.tiEstablishmentRef.setText(item.name)
                mCustomListDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mEstablishmentViewModel.allEstablishmentList.removeObservers(this)
    }
}
