package com.example.quizchallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quizchallenge.R
import com.example.quizchallenge.databinding.FragmentHomeBinding
import com.example.quizchallenge.databinding.FragmentHomeBinding.inflate
import com.example.quizchallenge.ui.repository.SessionManager
import com.example.quizchallenge.ui.viewmodels.QuizViewModel
import com.example.quizchallenge.utils.customizeClass.addPluralOrSingularity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var sharedPreferencesManager: SessionManager
    private var backPressedTime: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        onClick()
    }

    private fun initData() {
        binding.apply {
            textView.text = addPluralOrSingularity(sharedPreferencesManager.highScore,"point")
      }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (shouldNavigateBack()) {
                requireActivity().finish()
            }
        }
    }
    private fun shouldNavigateBack(): Boolean {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < 2000) { // Adjust the time interval as needed
            return true
        } else {
            backPressedTime = currentTime
            Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_LONG).show()
        }
        return false
    }
    private fun onClick() {
        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_examRoomFragment)
        }
    }


}