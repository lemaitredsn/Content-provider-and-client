package ru.lemaitre.clientmycontentprovider.presentation


import android.os.Bundle
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_client_ui.*
import ru.lemaitre.clientmycontentprovider.R
import ru.lemaitre.clientmycontentprovider.adapter.ListAdapter
import ru.lemaitre.clientmycontentprovider.data.AutoClearedValue
import ru.lemaitre.clientmycontentprovider.extensions.toast

class ClientFragment: Fragment(R.layout.fragment_client_ui) {
    private val viewModel: ClientViewModel by viewModels()
    private var listAdapter: ListAdapter by AutoClearedValue(this)
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.coursesLiveData.observe(viewLifecycleOwner){
            listAdapter.items = it.sortedBy { it.id }
        }
        initList()
        bindViewModel()

    }
    
    private fun initList(){
        listAdapter = ListAdapter()
        with(listCourses){
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    
    private fun bindViewModel(){
        viewModel.isLoading.observe(viewLifecycleOwner, ::isLoading)

        getAllCoursesBtn.setOnClickListener {
            viewModel.loadList()
        }
        getCourseOnIdBtn.setOnClickListener {
            val id = idTextField.editText?.text.toString()
            if(id.isNotEmpty() && id.contains(Regex("\\d+"))){
                viewModel.getCourseById(id.toLong())
            }else{
                toast(getString(R.string.enter_id_course))
            }
        }

        addCourseBtn.setOnClickListener {
            if(nameTextField.editText?.text.toString().isNotEmpty()){
                viewModel.addCourse(nameTextField.editText?.text.toString())
            }else{
                toast(getString(R.string.enter_name_course))
            }
        }

        deleteCourseOnIdBtn.setOnClickListener {
            val id = idTextField.editText?.text.toString()
            if(id.isNotEmpty() && id.contains(Regex("\\d+"))){
                viewModel.deleteById(idTextField.editText?.text.toString().toLong())
            }else{
                toast(getString(R.string.enter_id_course))            }
        }

        deleteAllCoursesBtn.setOnClickListener {
            viewModel.deleteAllCourses()
        }
        updateCourseOnIdBtn.setOnClickListener {
            val id = idTextField.editText?.text.toString()
            val name = nameTextField.editText?.text.toString()
            if(id.isNotEmpty() && id.contains(Regex("\\d+")) && name.isNotEmpty()){
                viewModel.updateCourseById(
                        idTextField.editText?.text.toString().toLong(),
                        nameTextField.editText?.text.toString())
            }else{
                toast(getString(R.string.enter_correct_data))
            }

        }
    }

    private fun isLoading(load:Boolean){
        progressBar.isVisible = load
        getAllCoursesBtn.isEnabled = !load
        idTextField.isEnabled = !load
        getCourseOnIdBtn.isEnabled = !load
        nameTextField.isEnabled = !load
        addCourseBtn.isEnabled = !load
        deleteCourseOnIdBtn.isEnabled = !load
        deleteAllCoursesBtn.isEnabled = !load
        updateCourseOnIdBtn.isEnabled = !load
    }
}