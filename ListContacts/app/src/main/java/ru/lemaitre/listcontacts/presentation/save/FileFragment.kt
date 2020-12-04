package ru.lemaitre.listcontacts.presentation.save

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_file.*
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.utils.toast

class FileFragment : Fragment(R.layout.fragment_file){
    private val viewModel: FileViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.stateLiveData.observe(viewLifecycleOwner){state -> toast(state)}
        viewModel.uriLiveData.observe(viewLifecycleOwner, ::shareIntent)

        storageBtn.setOnClickListener {
            val link = storageEt.text.toString()
            if(link.isNullOrBlank().not()){
                viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
                viewModel.downloadFile(link)
            }else{
                toast("нет данных")
            }
        }

        shareFileBtn.setOnClickListener {
            viewModel.shareFile()

        }
    }

    private fun shareIntent(uri: Uri){
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            type = requireContext().contentResolver.getType(uri)
            setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun updateIsLoading(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        storageBtn.isEnabled = !isLoading
        shareFileBtn.isEnabled = !isLoading
    }
}