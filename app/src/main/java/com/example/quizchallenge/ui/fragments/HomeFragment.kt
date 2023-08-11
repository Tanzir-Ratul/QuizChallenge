package com.example.quizchallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        var tempString = ""
        binding.apply {
            textView.text = addPluralOrSingularity(sharedPreferencesManager.highScore,"point")
      }
    }

    private fun onClick() {
        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_examRoomFragment)
        }
    }


}