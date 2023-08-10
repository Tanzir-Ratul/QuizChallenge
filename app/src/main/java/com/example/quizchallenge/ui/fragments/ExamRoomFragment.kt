package com.example.quizchallenge.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizchallenge.databinding.FragmentExamRoomBinding
import com.example.quizchallenge.ui.viewmodels.QuizViewModel
import com.example.quizchallenge.utils.customizeClass.SmoothSnapHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamRoomFragment : Fragment() {
    private lateinit var binding: FragmentExamRoomBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private var questionListAdapter: QuestionListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExamRoomBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = quizViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        setObserver()
    }

    private fun initData() {
        //recyclerView init
        questionListAdapter = QuestionListAdapter(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = questionListAdapter

        val smoothScroller = SmoothSnapHelper(requireContext())
        smoothScroller.attachToRecyclerView(binding.recyclerView)

    }

    private fun setObserver() {
        quizViewModel.apply {
            question.observe(viewLifecycleOwner) { question ->
                if (question != null && question.isNotEmpty()) {
                     questionListAdapter?.setData(question)
                }
            }
        }
    }

}