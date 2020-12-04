package ru.lemaitre.listcontacts.presentation.list

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_contact_list.*
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.data.Contact
import ru.lemaitre.listcontacts.presentation.adapter.ListAdapter
import ru.lemaitre.listcontacts.utils.AutoClearedValue
import ru.lemaitre.listcontacts.utils.toast


class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private val viewModel: ContactListViewModel by viewModels()

    private var contactAdapter: ListAdapter by AutoClearedValue(this)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initList()
        addContactButton.setOnClickListener {
            findNavController().navigate(R.id.action_contactListFragment2_to_contactAddFragment)
        }
        bindViewModel()

        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                Manifest.permission.READ_CONTACTS,
                onShowRationale = ::onContactPermissionShowRationale,
                onPermissionDenied = ::onContactPermissionDenied,
                onNeverAskAgain = ::onContactPermissionNeverAskAgain,
                requiresPermission = { viewModel.loadList() }
            )
                .launch()
        }
        toolbar.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_contactListFragment2_to_fileFragment)
            true
        }
    }



    private fun initToolbar() {
        toolbar.setTitle(R.string.contact_list_title)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.inflateMenu(R.menu.main_menu)
        val item = toolbar.menu.getItem(0)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }



    private fun initList() {
        contactAdapter = ListAdapter(viewModel::getDetailInfo)
        with(listContacts) {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        viewModel.contactsLiveData.observe(viewLifecycleOwner) { contactAdapter.items = it }
        viewModel.detailLiveData.observe(viewLifecycleOwner, ::showDetailInfo)
        viewModel.isLoading.observe(viewLifecycleOwner, ::isEnabledUI)
    }

    private fun isEnabledUI(load: Boolean) {
        progressBar.isVisible = load
        addContactButton.isVisible = !load
        listContacts.isVisible = !load

    }

    private fun showDetailInfo(contact: Contact) {
        val action =
            ContactListFragmentDirections.actionContactListFragment2ToDetailContactFragment(contact)
        findNavController().navigate(action)
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