package com.fok.quizappproject.ui.AddQuiz

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fok.quizappproject.databinding.FragmentAddQuizBinding
import com.fok.quizappproject.ui.AddQuiz.viewModel.AddQuizViewModelImpl
import com.fok.quizappproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@AndroidEntryPoint
class AddQuizFragment : BaseFragment<FragmentAddQuizBinding>() {
    override val viewModel: AddQuizViewModelImpl by viewModels()
    private var shouldNavigateBack = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddQuizBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.run {
            btnImport.setOnClickListener {
                val quizId = etQuizId.text.toString()
                val title = etTitle.text.toString()
                val timerMinutes = binding.tvTimer.text.toString().toLongOrNull()

                // Check if quizId, title, and CSV file are not empty before proceeding
                if (quizId.isNotBlank() && title.isNotBlank() && tvSelectedFile.text.isNotBlank()) {
                    viewModel.addQuiz(quizId, title, timerMinutes)
                    val action = AddQuizFragmentDirections.toDashBoard()
                    navController.navigate(action)
                } else {
                    // Show a Toast message for the error
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields and select a CSV file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnCsv.setOnClickListener {
                getContent.launch("text/*")
            }

        }

    }


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {

                val documentFile = DocumentFile.fromSingleUri(requireContext(), it)
                val originalFileName = documentFile?.name
                Log.d("debugging", "Original File Name: $originalFileName")

                binding.run {
                    tvSelectedFile.text = originalFileName.toString()?: "CSV File Name"
                }


                val csvFile = requireActivity().contentResolver.openInputStream(it)
                val isr = InputStreamReader(csvFile)

                BufferedReader(isr).readLines().let { lines ->
                    Log.d("debugging", lines.toString())
                    viewModel.readCsv(lines)

                }
            }
        }


    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)
        lifecycleScope.launch {
            viewModel.success.collect {
                if (shouldNavigateBack) {
                    navController.popBackStack()
                    shouldNavigateBack = false // Reset the flag
                }
            }
        }
    }
}


