package com.auto.christianasarhasa.presentation.results


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.auto.christianasarhasa.R
import com.auto.christianasarhasa.common.Constants
import com.auto.christianasarhasa.common.Utilities
import com.auto.christianasarhasa.databinding.FragmentResultsBinding
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.*


@AndroidEntryPoint
class ResultsFragment : Fragment() {

    private var _binding : FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private val args : ResultsFragmentArgs by navArgs()
    private lateinit var viewModel : ResultsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        this.activity?.findViewById<TextView>(R.id.textView_toolbarTitle)?.text = getString(R.string.fragment_title_results)
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        viewModel =  ViewModelProvider(requireActivity()).get(ResultsViewModel::class.java)
        viewModel.setUpViewModel(
            args.weight.toDouble(),
            args.height.toDouble(),
            args.name
        )

        initializeViewElements()

        // Inflate the layout for this fragment
        return binding.root
    }

    // Initialize the view elements
    private fun initializeViewElements() {

        val ponderalIndex : String = Utilities.roundOffToTwoDp(viewModel.getPonderalCalculation())
        val formattedBmi = Utilities.getFormatBmiResult(viewModel.bmi)
        val adLoader: AdLoader = AdLoader.Builder(requireActivity(), getString(R.string.ads_id_native))
            .forNativeAd { nativeAd ->
                val styles =
                    NativeTemplateStyle.Builder().build()
                val template = binding.nativeAd
                template.setStyles(styles)
                template.setNativeAd(nativeAd)
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        binding.apply {
            textViewUserMessage.text = viewModel.getBmiMessage()
            textViewBmiRange.text = viewModel.getBmiRangeMessage()
            textViewPonderalResult.text = getString(R.string.message_ponderal_index, ponderalIndex)
            textViewBmiWhole.text = formattedBmi[0]
            textViewBmiDecimal.text = formattedBmi[1]
            // Save image and store on phone gallery when the share button is clicked
            buttonShare.setOnClickListener{
                saveImage()
                viewModel.file?.let {
                    shareFile(it)
                }
            }
            // run the rate intent function to open google play store when the rate button is clicked
            buttonRate.setOnClickListener{
                rateApp()
            }
        }
    }

    // function to rate app i.e, opening google play store
    private fun rateApp() {
        try{
            val uri : Uri = Uri.parse(Constants.PLAYSTORE_APP_URL + Constants.TEST_PACKAGE_NAME)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }catch(e : ActivityNotFoundException) {
            val uri : Uri = Uri.parse(Constants.PLAYSTORE_WEB_URL + Constants.TEST_PACKAGE_NAME)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }catch (e : Exception){
            e.printStackTrace()
            showErrorToast()
        }
    }

    //function to share file
    private fun shareFile(file: File) {
        val uri : Uri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            FileProvider.getUriForFile(requireActivity(), requireActivity().packageName + ".provider", file)
        }else {
            Uri.fromFile(file)
        }

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_image_subject))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_image_text))
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_image_intent_title)))
        }catch (e : Exception) {
            e.printStackTrace()
            showErrorToast()
        }
    }

    //function to override request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            binding.buttonShare.performClick()
        }else {
            Toast.makeText(requireActivity(), getString(R.string.message_permission_denied), Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // function to save image on phone storage
    private fun saveImage(){
        if(!checkPermission()) return

        if(viewModel.file != null) return

        try {
            val path : String = Environment.getExternalStorageDirectory().toString() + Constants.IMAGE_LOCATION_SUBDIRECTORIES + getString(R.string.app_name)
            val fileDir = File(path)
            if(!fileDir.exists())
                fileDir.mkdir()

            val mPath : String = path + Constants.IMAGE_SUFFIX + Calendar.getInstance().timeInMillis.toString() + ".png"

            val bitmap : Bitmap = getScreenshot()
            val file = File(mPath)
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            viewModel.setSavedFile(file)

            Toast.makeText(requireActivity(), getString(R.string.message_save_success), Toast.LENGTH_LONG).show()



        } catch (e : Exception) {
            e.printStackTrace()
            showErrorToast()
        }
    }

    //function to take screenshots
    private fun getScreenshot(): Bitmap {
        val view : View = this.requireView().findViewById(R.id.linearLayout_results)
        val bitmap : Bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // function to check for permission
    private fun checkPermission() : Boolean {
        val permission : Int = ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    //function to show error toast
    private fun showErrorToast() {
        Toast.makeText(requireActivity(), getString(R.string.message_error), Toast.LENGTH_SHORT).show()
    }



}