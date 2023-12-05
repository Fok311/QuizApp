package com.fok.quizappproject.ui.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fok.quizappproject.R
import com.fok.quizappproject.databinding.FragmentLoginBinding
import com.fok.quizappproject.ui.Login.viewModel.LoginViewModelImpl
import com.fok.quizappproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: LoginViewModelImpl by viewModels()
    private var forgotMode = false

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if(authService.getCurrentUser() != null) {
//            val action = LoginFragmentDirections.toHome()
//            navController.navigate(action)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            if (forgotMode){
                viewModel.resetPassword(email)
            }else {
                viewModel.login(email, pass)
            }
        }

        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToRegister()
            navController.navigate(action)
        }

        binding.tvForgot.setOnClickListener {
            if (forgotMode){
                binding.tilPassword.visibility = View.VISIBLE
                binding.tvForgot.text = getString(R.string.forgot_password)
//                binding.tvTitle.text = getString(R.string.please_login)
                binding.btnLogin.text = getString(R.string.login)

                forgotMode = false

            } else {
                binding.tilPassword.visibility = View.GONE
                binding.tvForgot.text = getString(R.string.use_password_to_login)
//                tvTitle.text = getString(R.string.reset_password)
                binding.btnLogin.text = getString(R.string.send)

                forgotMode = true
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)
        lifecycleScope.launch {
            viewModel.user.collect {
                if (!forgotMode) {
                    val action = when (it.role) {
                        "Student" -> LoginFragmentDirections.toHome()
                        "Teacher" -> LoginFragmentDirections.toDashBoard()
                        else -> null
                    }
                    action?.let { navController.navigate(action) }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.success.collect {
                viewModel.getCurrentUser()
            }
        }
    }
}