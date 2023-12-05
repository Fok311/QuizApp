package com.fok.quizappproject.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fok.quizappproject.R
import com.fok.quizappproject.databinding.FragmentHomeBinding
import com.fok.quizappproject.ui.base.BaseFragment
import com.fok.quizappproject.ui.home.viewModel.HomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val viewModel: HomeViewModelImpl by viewModels()
    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                viewModel.updateProfilePic(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.Leaderboard.setOnClickListener {
            val action = HomeFragmentDirections.toLeaderBoard()
            navController.navigate(action)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.icEditProfile.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.joinQuiz.setOnClickListener {
            val action = HomeFragmentDirections.toJoinQuiz()
            navController.navigate(action)
        }
    }



    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.user.collect {
                val greetingText = "Hello, "
                val fullName = "$greetingText${it.name}!"
                binding.tvUser.text = fullName
            }
        }


        lifecycleScope.launch {
            viewModel.finish.collect {
                val action = HomeFragmentDirections.toLogin()
                navController.navigate(action)
            }
        }

        lifecycleScope.launch {
            viewModel.profileUri.collect {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivProfile)
            }
        }
    }
}
