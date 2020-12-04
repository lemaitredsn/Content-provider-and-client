package ru.lemaitre.listcontacts.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_detail_contact.*
import ru.lemaitre.listcontacts.R
import ru.lemaitre.listcontacts.presentation.list.ContactListViewModel

class DetailContactFragment: Fragment(R.layout.fragment_detail_contact) {
    private val viewModel: DetailContactViewModel by viewModels()

    private val args: DetailContactFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initContact()

        deleteButton.setOnClickListener {
            viewModel.delete(args.contact.id)
            findNavController().popBackStack()
        }
    }

    private fun initToolbar(){
        toolbar.title = getString(R.string.detail_contact_title, args.contact.name)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initContact(){
        nameContactTV.text = args.contact.name
        numbersContactTV.text = args.contact.phones.joinToString(separator = "\n")
        emailContactTV.text = args.contact.email.joinToString(separator = "\n")
    }
}