package com.basculasmagris.richiger.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ActivityAddUpdateEstablishmentBinding
import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.viewmodel.EstablishmentRemoteViewModel
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModel
import com.basculasmagris.richiger.viewmodel.EstablishmentViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class AddUpdateEstablishmentActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateEstablishmentBinding

    private var mImagePath: String = ""
    private lateinit var mCustomListDialog : Dialog
    private var mEstablishmentDetails: Establishment? = null
    private var mNewEstablishmentDetails: Establishment? = null

    private val mEstablishmentViewModel: EstablishmentViewModel by viewModels {
        EstablishmentViewModelFactory((application as EnsiladoraApplication).establishmentRepository)
    }

    private lateinit var mEstablishmentViewModelRemote: EstablishmentRemoteViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateEstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS)){
            mEstablishmentDetails = intent.getParcelableExtra(Constants.EXTRA_ESTABLISHMENT_DETAILS)
        }

        setupActionBar()

        mEstablishmentDetails?.let {
            if (it.id != 0L){
                binding.tiEstablishmentName.setText(it.name)
                binding.tiEstablishmentDescription.setText(it.description)
                binding.btnAddEstablishment.text = resources.getString(R.string.lbl_update_establishment)
            }
        }
        binding.btnAddEstablishment.setOnClickListener(this)
    }
    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateEstablishment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mEstablishmentDetails != null && mEstablishmentDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_establishment)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_establishment)
            }
        }

        binding.toolbarAddUpdateEstablishment.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.btn_add_establishment -> {
                    val establishmentName = binding.tiEstablishmentName.text.toString().trim { it <= ' ' }
                    val establishmentDescription = binding.tiEstablishmentDescription.text.toString().trim { it <= ' ' }
                    var remoteId = 0L

                    mEstablishmentDetails?.let {
                        remoteId = it.remoteId
                    }

                    when {
                        TextUtils.isEmpty(establishmentName) -> {
                            Toast.makeText(
                                this@AddUpdateEstablishmentActivity,
                                resources.getString(R.string.err_msg_establishment_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            var establishmentId = 0L
                            var updatedDate = ""

                            mEstablishmentDetails?.let {
                                if (it.id != 0L){
                                    establishmentId = it.id
                                    updatedDate = Date().toString()
                                }
                            }

                            val establishment = Establishment(
                                establishmentName,
                                establishmentDescription,
                                remoteId,
                                updatedDate,
                                "",
                                establishmentId
                            )

                            mNewEstablishmentDetails = establishment

                            runBlocking {
                                if (establishmentId == 0L){
                                    mEstablishmentViewModel.insertSync(establishment)
                                } else {
                                    mEstablishmentViewModel.updateSync(establishment)
                                }

                                Log.i("SYNC", "Se modifica establecimiento con fecha ${establishment.updatedDate}")
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
