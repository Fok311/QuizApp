package com.fok.quizappproject.ui.Register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fok.quizappproject.R
import com.fok.quizappproject.databinding.FragmentRegisterBinding
import com.fok.quizappproject.ui.Login.LoginFragmentDirections
import com.fok.quizappproject.ui.Register.viewModel.RegisterViewModelImpl
import com.fok.quizappproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.run {

            btnRegister.setOnClickListener {
                viewModel.register(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPass.text.toString(),
                    role = when (radioGroup.checkedRadioButtonId) {
                        R.id.studentRadioButton -> "Student"
                        R.id.teacherRadioButton -> "Teacher"
                        else -> ""
                    }
                )
            }
            tvLogin.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)
        lifecycleScope.launch {
            viewModel.success.collect {
                val action = RegisterFragmentDirections.toLogin()
                navController.navigate(action)
            }
        }
    }
}