package com.auto.christianasarhasa.presentation.add_details

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.navigation.fragment.findNavController
import com.auto.christianasarhasa.R
import com.auto.christianasarhasa.common.Constants
import com.auto.christianasarhasa.common.Utilities
import com.auto.christianasarhasa.databinding.FragmentAddDetailsBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class AddDetailsFragment : Fragment() {

    private var _binding : FragmentAddDetailsBinding? = null
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val binding get() = _binding!!
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        this.activity?.findViewById<TextView>(R.id.textView_toolbarTitle)?.text = getString(R.string.fragment_title_addDetails)
        _binding = FragmentAddDetailsBinding.inflate(inflater, container, false)

        //load interstitial ad
        loadInterstitialAd()
        //load view
        initializeViewElements()

        // Inflate the layout for this fragment
        return binding.root
    }

    // Function to load interstitial ad
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireActivity(),getString(R.string.ads_id_interstitial), adRequest, object : InterstitialAdLoadCallback() {
            //if ad failed to load
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            // ad loads successfully
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

    }

    private fun initializeViewElements() {
        // all views from weight number picker
        binding.numberPickerWeight.apply {
            minValue = Constants.MIN_WEIGHT
            maxValue = Constants.MAX_WEIGHT
            value = Constants.BASE_WEIGHT
        }

        // all view from height number picker
        binding.numberPickerHeight.apply {
            minValue = Constants.MIN_HEIGHT
            maxValue = Constants.MAX_HEIGHT
            value = Constants.BASE_HEIGHT
        }

        // all views from gender number picker
        binding.numberPickerGender.apply {
            minValue = 0
            maxValue = Utilities.genders.size-1
            value = 0
            displayedValues = Utilities.getGenderString()
        }

        //calculate button on click listener to load interstitial ad and go to next activity
        binding.buttonCalculate.setOnClickListener {
            buttonCalculateOnClick()
        }
    }

    private fun buttonCalculateOnClick() {
        val name = binding.editTextName.text.toString()

        //if name is blank then shake an error
        if(name.isBlank()){
            binding.editTextName.error = getString(R.string.message_input_error)
            return
        }

        val weight : Float = binding.numberPickerWeight.value.toFloat()
        val height : Float = binding.numberPickerHeight.value.toFloat()
        setUpInterstitialAdCallback(weight, height, name)

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
        }else{
            navigateToResultsScreen(weight, height, name)
        }

    }

    private fun setUpInterstitialAdCallback(weight : Float, height: Float, name : String) {

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                mInterstitialAd = null
                navigateToResultsScreen(weight, height, name)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                mInterstitialAd = null
            }
        }
    }

    //fun to navigate to results screen
    private fun navigateToResultsScreen(weight : Float, height: Float, name : String) {
        val action  = AddDetailsFragmentDirections.actionAddDetailsFragmentToDetailsFragment(weight, height, name)
        binding.editTextName.text = Editable.Factory.getInstance().newEditable("")
        findNavController().navigate(action)
    }

}