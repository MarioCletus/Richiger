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
import com.basculasmagris.richiger.databinding.ActivityAddUpdateMixerBinding
import com.basculasmagris.richiger.model.entities.Mixer
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.viewmodel.MixerRemoteViewModel
import com.basculasmagris.richiger.viewmodel.MixerViewModel
import com.basculasmagris.richiger.viewmodel.MixerViewModelFactory
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateMixerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateMixerBinding
    private var mMixerDetails: Mixer? = null
    private var mNewMixerDetails: Mixer? = null

    private val mMixerViewModel: MixerViewModel by viewModels {
        MixerViewModelFactory((application as EnsiladoraApplication).mixerRepository)
    }

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
        binding = ActivityAddUpdateMixerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_MIXER_DETAILS)){
            mMixerDetails = intent.getParcelableExtra(Constants.EXTRA_MIXER_DETAILS)
        }

        setupActionBar()

        mMixerDetails?.let {
            if (it.id != 0L){
                binding.tiMixerName.setText(it.name)
                binding.tiMixerDescription.setText(it.description)
                binding.tiRfid.setText(it.rfid.toString())
                binding.btnAddMixer.text = resources.getString(R.string.lbl_update_mixer)
                binding.etBtBox.setText(it.btBox)
                binding.etCalibration.setText(it.calibration.toString())
                binding.etMac.setText(it.mac)
                binding.etTara.setText(it.tara.toString())
            }
        }

        binding.btnAddMixer.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateMixer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mMixerDetails != null && mMixerDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_mixer)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_mixer)
            }
        }

        binding.toolbarAddUpdateMixer.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.btn_add_mixer -> {
                    val mixerName = binding.tiMixerName.text.toString().trim { it <= ' ' }
                    val mixerDescription = binding.tiMixerDescription.text.toString().trim { it <= ' ' }
                    val mixerRfId = binding.tiRfid.text.toString().trim { it <= ' ' }
                    var remoteId = 0L
                    var mixerMac = binding.etMac.text.toString().trim { it <= ' ' }
                    var mixerBtBox = binding.etBtBox.text.toString().trim { it <= ' ' }
                    var mixerTara = binding.etTara.text.toString().trim { it <= ' ' }
                    var mixerCalibration = binding.etCalibration.text.toString().trim { it <= ' ' }

                    mMixerDetails?.let {
                        remoteId = it.remoteId
                    }

                    when {
                        TextUtils.isEmpty(mixerName) -> {
                            Toast.makeText(
                                this@AddUpdateMixerActivity,
                                resources.getString(R.string.err_msg_mixer_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            var mixerId = 0L
                            var updatedDate = ""

                            mMixerDetails?.let {
                                if (it.id != 0L){
                                    mixerId = it.id
                                    updatedDate = Date().toString()
                                }
                            }

                            val mixer = Mixer(
                                mixerName,
                                mixerDescription,
                                mixerMac,
                                mixerBtBox,
                                if (mixerTara.isNullOrEmpty()) 0 else mixerTara.toInt(),
                                if (mixerCalibration.isNullOrEmpty()) 0.toFloat() else mixerCalibration.toFloat(),
                                if (mixerRfId.isNullOrEmpty()) 0 else mixerRfId.toLong(),
                                remoteId,
                                updatedDate,
                                "",
                                mixerId
                            )

                            mNewMixerDetails = mixer

                            runBlocking {
                                if (mixerId == 0L){
                                    mMixerViewModel.insertSync(mixer)
                                } else {
                                    mMixerViewModel.updateSync(mixer)
                                }
                                Log.i("SYNC", "Se actualiza mixer con fecha ${mixer.updatedDate}")
                                finish()
                            }

                        }
                    }
                }
            }
        }
    }
}