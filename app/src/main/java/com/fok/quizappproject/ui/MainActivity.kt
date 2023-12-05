package com.fok.quizappproject.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.fok.quizappproject.R
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.core.service.NetworkManager
import com.fok.quizappproject.data.Model.User
import com.fok.quizappproject.data.repo.UserRepo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var repo: UserRepo

    private val _user = MutableSharedFlow<User>()
    val user: SharedFlow<User> = _user

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHostFragment)

        if (authService.getCurrentUser() != null) {
            getUserRole()

            lifecycleScope.launch {
                user.collect {
                    if (it.role == "Teacher") navController.navigate(R.id.dashBoardFragment)
                    else navController.navigate(R.id.homeFragment)
                }
            }
        }

        dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(R.layout.network)
            .setCancelable(false)
            .create()

        val networkManager = NetworkManager(this)
        networkManager.observe(this){
            if(!it){
                if (!dialog.isShowing) dialog.show()
            } else{
                if(dialog.isShowing) dialog.hide()
            }
        }

        window.statusBarColor = Color.BLACK;
    }

    fun getUserRole() {
        lifecycleScope.launch {
            repo.getUser(uid = toString())?.let {
                _user.emit(it)
            }
        }

    }
}