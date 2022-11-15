package com.basculasmagris.richiger.view.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.ActivityAddUpdateProductBinding
import com.basculasmagris.richiger.databinding.DialogCustomImageSelectionBinding
import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.viewmodel.ProductViewModel
import com.basculasmagris.richiger.viewmodel.ProductViewModelFactory
import com.karumi.dexter.Dexter
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.basculasmagris.richiger.databinding.DialogCustomListBinding
import com.basculasmagris.richiger.utils.Constants
import com.basculasmagris.richiger.view.adapter.CustomListItem
import com.basculasmagris.richiger.view.adapter.CustomListItemAdapter
import com.basculasmagris.richiger.viewmodel.ProductRemoteViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.runBlocking
import java.io.*
import java.util.*


class AddUpdateProductActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateProductBinding

    private var mImagePath: String = ""
    private var mProductDetails: Product? = null

    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((application as EnsiladoraApplication).productRepository)
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
        binding = ActivityAddUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_DETAILS)){
            mProductDetails = intent.getParcelableExtra(Constants.EXTRA_PRODUCT_DETAILS)
        }

        setupActionBar()

        mProductDetails?.let {
            if (it.id != 0L){
                mImagePath = it.image
                Glide.with(this@AddUpdateProductActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(binding.ivProductImage)

                binding.tiProductName.setText(it.name)
                binding.tiProductDescription.setText(it.description)
                binding.tiRfid.setText(it.rfid.toString())
                binding.tiSpecificWeight.setText(it.specificWeight.toString())
                binding.btnAddProduct.text = resources.getString(R.string.lbl_update_product)
            }
        }

        binding.btnAddProduct.setOnClickListener(this)
        binding.ivAddProductImage.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddUpdateProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mProductDetails != null && mProductDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_product)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_product)
            }
        }

        binding.toolbarAddUpdateProduct.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when(p0.id){
                R.id.btn_add_product -> {
                    val productName = binding.tiProductName.text.toString().trim { it <= ' ' }
                    val productDescription = binding.tiProductDescription.text.toString().trim { it <= ' ' }
                    val productSpecificWeight = binding.tiSpecificWeight.text.toString().trim { it <= ' ' }
                    val productRfId = binding.tiRfid.text.toString().trim { it <= ' ' }
                    var remoteId = 0L

                    mProductDetails?.let {
                        remoteId = it.remoteId
                    }

                    when {
                        TextUtils.isEmpty(productName) -> {
                            Toast.makeText(
                                this@AddUpdateProductActivity,
                                resources.getString(R.string.err_msg_product_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            var productId = 0L
                            var imageSource = Constants.PRODUCT_IMAGE_SOURCE_LOCAL
                            var updatedDate = ""

                            mProductDetails?.let {
                                if (it.id != 0L){
                                    productId = it.id
                                    imageSource = it.imageSource
                                    updatedDate = Date().toString()
                                }
                            }

                            val product = Product(
                                productName,
                                productDescription,
                                if (productSpecificWeight.isEmpty()) 0.0 else productSpecificWeight.toDouble(),
                                if (productRfId.isEmpty()) 0 else productRfId.toLong(),
                                mImagePath,
                                imageSource,
                                remoteId,
                                updatedDate,
                                "",
                                productId
                            )

                            runBlocking {
                                if (productId == 0L){
                                    mProductViewModel.insertSync(product)
                                } else {
                                    Log.i("sync", "Se actualiza producto $productName con fecha $updatedDate")
                                    mProductViewModel.updateSync(product)
                                    Log.i("sync", "Se actualiza producto $productName con fecha $updatedDate")

                                }
                                finish()
                            }
                        }
                    }
                }

                R.id.iv_add_product_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private  fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener{
            Dexter.withContext(this@AddUpdateProductActivity).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    report?.let {
                        if (report.areAllPermissionsGranted()){
                            val intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            resultCameraLauncher.launch(intent)
                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRelationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener{
                Dexter.withContext(this@AddUpdateProductActivity).withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object : PermissionListener{

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        resultGalleryLauncher.launch(galleryIntent)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateProductActivity, "Permiso denegado", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        showRelationalDialogForPermissions()
                    }

                }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    var resultCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.extras?.let {
                val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap

                Glide.with(this)
                    .load(thumbnail)
                    .centerCrop()
                    .into(binding.ivProductImage)

                mImagePath = saveImageToInternalStorage(thumbnail)

                binding.ivAddProductImage.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
            }
        }
    }

    var resultGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedPhotoUri = data?.data
            Glide.with(this)
                .load(selectedPhotoUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            val bitmap: Bitmap = resource.toBitmap()
                            mImagePath = saveImageToInternalStorage(bitmap)
                        }

                        return false
                    }

                })
                .centerCrop()
                .into(binding.ivProductImage)
            binding.ivAddProductImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
        }
    }

    private fun showRelationalDialogForPermissions(){
        AlertDialog.Builder(this)
            .setMessage("SPI Mixer solicita permisos para acceder a la cámara. Puedes habilitarlo accediendo a la configuración")
            .setPositiveButton("Configuración")
            {_,_ -> try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancelar"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) : String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }

        return file.absolutePath
    }

    companion object {
        private const val IMAGE_DIRECTORY = "SpiMixerImages"
    }
}

