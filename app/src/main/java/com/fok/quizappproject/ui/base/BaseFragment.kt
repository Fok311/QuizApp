package com.fok.quizappproject.ui.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.fok.quizappproject.R
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseFragment<T:ViewBinding> : Fragment() {


    abstract val viewModel: BaseViewModel
    lateinit var binding : T
    protected lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        setupUiComponents(view)
        setupViewModelObserver(view)
    }

    protected open fun onFragmentResult() {}
    open fun setupUiComponents(view: View) {}

    open fun setupViewModelObserver(view: View) {
        lifecycleScope.launch {
            viewModel.error.collect {
                showSnackbar(view, it)
            }
        }
        lifecycleScope.launch {
            viewModel.success.collect {
                showSnackbar(view, it)
            }
        }
    }

    fun showSnackbar(view: View, msg: String, isError: Boolean = false) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)

        if (isError) {
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error
                )
            )
        } else {
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                )
            ).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.ok
                )
            )
        }
        snackbar.show()
    }

    fun logMsg(msg: String, tag: String = "debugging") {
        Log.d(tag, msg)
    }

}
