package com.fok.quizappproject.ui.DashBoard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fok.quizappproject.R
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.databinding.FragmentDashBoardBinding
import com.fok.quizappproject.ui.DashBoard.viewModel.DashBoardViewModelImpl
import com.fok.quizappproject.ui.adapter.QuizAdapter
import com.fok.quizappproject.ui.base.BaseFragment
import com.fok.quizappproject.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {

    override val viewModel: DashBoardViewModelImpl by viewModels()
    private lateinit var adapter: QuizAdapter
    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var fileName: String = "DefaultFileName"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)
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
        setupAdapter()

        binding.cvAdd.setOnClickListener {
            val action = DashBoardFragmentDirections.actionDashBoardToAddQuiz()
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
            viewModel.quiz.collect {
                adapter.setQuiz(it)
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

    private fun setupAdapter() {
        Log.d("FileName", "Value of fileName: $fileName")
        adapter = QuizAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuiz.adapter = adapter
        binding.rvQuiz.layoutManager = layoutManager
    }
}