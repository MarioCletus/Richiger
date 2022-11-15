package com.basculasmagris.richiger.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.richiger.R
import com.basculasmagris.richiger.application.EnsiladoraApplication
import com.basculasmagris.richiger.databinding.FragmentDietDetailBinding
import com.basculasmagris.richiger.utils.Helper
import com.basculasmagris.richiger.view.adapter.DietProductAdapter
import com.basculasmagris.richiger.view.adapter.ProductAdapter
import com.basculasmagris.richiger.viewmodel.DietViewModel
import com.basculasmagris.richiger.viewmodel.DietViewModelFactory
import com.basculasmagris.richiger.viewmodel.ProductViewModel
import com.basculasmagris.richiger.viewmodel.ProductViewModelFactory
import com.bumptech.glide.Glide


class DietDetailFragment : Fragment() {

    private var mBinding: FragmentDietDetailBinding? = null

    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as EnsiladoraApplication).productRepository)
    }

    private val mDietViewModel: DietViewModel by viewModels {
        DietViewModelFactory((requireActivity().application as EnsiladoraApplication).dietRepository)
    }

    private fun getLocalProductByDiet(dietId: Long){

        mBinding?.let { view ->
            view.rvProductsList.layoutManager = GridLayoutManager(requireActivity(), 1)
            val productAdapter =  DietProductAdapter(requireActivity(),
                usePercentage = false,
                null,
                null,
                null,
                readOnly = true
            )
            view.rvProductsList.adapter = productAdapter
            mDietViewModel.getProductsBy(dietId).observe(viewLifecycleOwner) { products ->
                products.let{

                    if (products.isNotEmpty()){
                        if (products.isEmpty()){
                            view.tvProductCount.text = getString(R.string.no_products)
                            view.tvNoAdditionalData.visibility = View.VISIBLE
                            view.rvProductsList.visibility = View.GONE
                        } else {
                            view.tvProductCount.text = getString(R.string.product_diet_count, products.size.toString())
                            view.rvProductsList.visibility = View.VISIBLE
                            view.tvNoAdditionalData.visibility = View.GONE
                        }
                        productAdapter.dietList(products)
                    } else {
                        view.rvProductsList.visibility = View.GONE
                        view.tvNoAdditionalData.visibility = View.VISIBLE
                        view.tvProductCount.text = getString(R.string.no_products)
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDietDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DietDetailFragmentArgs by navArgs()

        args.dietDetail?.let { diet ->
            mBinding?.let {

                it.tvDietTitle.text = diet.name
                it.tvDietDescription.text = diet.description
                getLocalProductByDiet(diet.id)

                if (diet.remoteId > 0) {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.sync)
                } else {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red_500_primary), android.graphics.PorterDuff.Mode.SRC_IN);
                    it.tvSyncInfo.text = resources.getString(R.string.pending_sync)
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}