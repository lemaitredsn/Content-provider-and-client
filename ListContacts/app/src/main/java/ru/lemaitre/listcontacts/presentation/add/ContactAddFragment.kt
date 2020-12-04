package ru.lemaitre.listcontacts.presentation.add

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_contact_list.*
import kotlinx.android.synthetic.main.fragment_contact_list.toolbar
import kotlinx.android.synthetic.main.fragment_detail_contact.*
import kotlinx.android.synthetic.main.fragmnet_add_contact.*
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.utils.toast

class ContactAddFragment : Fragment(R.layout.fragmnet_add_contact) {

    private val viewModel by viewModels<ContactAddViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        bindViewModel()
    }

    private fun initToolbar() {
        toolbar.setTitle(getString(R.string.add_contact_name_phone))
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        saveButton.setOnClickListener { saveContactWithPermissionCheck() }
        viewModel.saveErrorLiveData.observe(viewLifecycleOwner) { toast(it) }
        viewModel.saveSuccessLiveData.observe(viewLifecycleOwner) { findNavController().popBackStack() }
    }


    private fun saveContactWithPermissionCheck() {
        constructPermissionsRequest(
            Manifest.permission.WRITE_CONTACTS,
            onShowRationale = ::onContactPermissionShowRationale,
            onPermissionDenied = ::onContactPermissionDenied,
            onNeverAskAgain = ::onContactPermissionNeverAskAgain
        ) {
            viewModel.save(
                name = nameTextField.editText?.text.toString(),
                phone = phoneTextField.editText?.text.toString(),
                email = emailTextField.editText?.text.toString()
            )
        }.launch()
    }

    private fun onContactPermissionDenied() {
        toast(resources.getString(R.string.read_contacts_no_permission))
    }

    private fun onContactPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionNeverAskAgain() {
        toast(resources.getString(R.string.add_permission_on_option_app))
    }
}