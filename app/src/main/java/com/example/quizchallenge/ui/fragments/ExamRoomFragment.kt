package com.example.quizchallenge.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizchallenge.R
import com.example.quizchallenge.databinding.FragmentExamRoomBinding
import com.example.quizchallenge.ui.models.QuizModel
import com.example.quizchallenge.ui.repository.SessionManager
import com.example.quizchallenge.ui.viewmodels.QuizViewModel
import com.example.quizchallenge.utils.customizeClass.SmoothSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExamRoomFragment : Fragment() {
    private lateinit var binding: FragmentExamRoomBinding
    private val quizViewModel: QuizViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SessionManager
    private var questionListAdapter: QuestionListAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var llCompat: LinearLayoutCompat? = null
    private var countDownTimer: CountDownTimer? = null
    private var questionList: MutableList<QuizModel.Question?> = mutableListOf()
    private var answerMap: Map<Int, Map<String, String?>> = mapOf()
    private var selectedAnswer: String? = ""
    private var correctAns: String? = ""
    private var currentPosition = 0
    private var isAnswered = false
    private var score: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        setOnClickListeners()
        setupCountDownTimer()

    }

    private fun setOnClickListeners() {
        questionListAdapter?.setTextViewCallback =
            { question, list, linearLayout, position, score ->
                quizViewModel.scorePerQuestion.value = score
                answerMap = mapOf<Int, Map<String, String?>>().plus(
                    (position to mapOf(
                        "A" to question?.answers?.A,
                        "B" to question?.answers?.B,
                        "C" to question?.answers?.C,
                        "D" to question?.answers?.D
                    ))
                )
//           val temp =  binding.recyclerView.findViewHolderForAdapterPosition(layoutManager.findFirstCompletelyVisibleItemPosition())
                val correctAns =
                    answerMap[position]?.get(questionList[position]?.correctAnswer?.trim())
                this.correctAns = correctAns
                llCompat = linearLayout
                Log.d("cPosition", "setOnClickListeners: ${(position)}")
                val innerAnsMap = answerMap[position]
                if (innerAnsMap != null) {
                    for ((key, value) in innerAnsMap) {
                        if (value != null) {
                            val textView = when (key) {
                                "A" -> list[0]
                                "B" -> list[1]
                                "C" -> list[2]
                                "D" -> list[3]
                                else -> null
                            }
                            textView?.visibility = View.GONE
                            if (textView != null && innerAnsMap[key] != null) {
                                Log.d("innerKey", "${key} ${value}")
                                textView.text = answerMap[position]?.get(key)?.trim()
                                textView.visibility = View.VISIBLE
                                textView.setBackgroundResource(R.drawable.option_bg)
                                textView.isClickable = true

                                textView.setOnClickListener {
                                    isAnswered = true
                                    selectedAnswer = value
                                    onAnswerSelected(value, linearLayout, correctAns, score)
                                }

                            } else {
                                textView?.visibility = View.GONE
                            }
                        }


                    }
                }

            }


        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (shouldNavigateBack()) {
                    findNavController().navigateUp()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

/*
        questionListAdapter?.imageSetCallBack = { url, image ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    if (url.isNotEmpty()) {
                        Glide.with(requireContext())
                            .asBitmap()
                            .load(url)
                            .error(R.drawable.image_placeholder_ot_error)
                            .submit() // here  are making request to load image
                            .get()
                            .let {
                                image.setImageBitmap(it)
                            }

                    }
                }
            }


        }
*/
    }

    private fun shouldNavigateBack(): Boolean {
        return true
    }

    private fun onAnswerSelected(
        selectedAnswer: String?,
        containerLL: LinearLayoutCompat?,
        correctAns: String?,
        score: Int?,
    ) {

        containerLL?.children?.forEach { view ->

            if (view is AppCompatTextView) {

                if (selectedAnswer == correctAns) {
                    if (view.text == selectedAnswer) {
                        quizViewModel.checkHighScoreBit(score)
                        view.setBackgroundResource(R.drawable.correct_option_bg)
                    }

                } else {
                    if (view.text == selectedAnswer) {
                        view.setBackgroundResource(R.drawable.incorrect_option_bg)
                    }
                    if (view.text == correctAns) {
                        view.setBackgroundResource(R.drawable.correct_option_bg)
                    }

                }
                view.isClickable = false // Disable clicking on other options
            }
        }
        if (isAnswered) {
            Handler(Looper.getMainLooper()).postDelayed({
                countDownTimer?.cancel()
                scrollToNextPosition()
                countDownTimer?.start() // Restart the timer
            }, 2000)
        }
    }

    private fun initData() {
        //recyclerView init
        questionListAdapter = QuestionListAdapter(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = questionListAdapter

        val smoothScroller = SmoothSnapHelper(requireContext())
        smoothScroller.attachToRecyclerView(binding.recyclerView)


    }

  /*  private fun warningDialog(showMsg: String?, fromlastPos: Boolean) {
       val warningDialog = Dialog(requireContext())
        warningDialog.setContentView(R.layout.warning_dialog)
        // warningDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        warningDialog?.setCancelable(false)
        warningDialog?.show()
        val msg = warningDialog?.findViewById<TextView>(R.id.warningTV)
        msg?.text = showMsg
        val yesBtn = warningDialog?.findViewById<Button>(R.id.yesBtn)
        yesBtn?.setOnClickListener {
            warningDialog?.dismiss()
            findNavController().popBackStack()
        }
        if (fromlastPos) {
            yesBtn?.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                warningDialog?.dismiss()
                findNavController().navigate(R.id.action_examRoomFragment_to_homeFragment)
            }, 3000)
        }

    }*/

    private fun setupCountDownTimer() {
        countDownTimer = object : CountDownTimer(TIME_LIMIT, TIME_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progress.progress = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                countDownTimer?.cancel()
                scrollToNextPosition()
                countDownTimer?.start() // Restart the timer
            }
        }
        countDownTimer?.start()
    }

    private fun scrollToNextPosition() {
        if (quizViewModel.currentPos.value != (questionListAdapter?.itemCount?.minus(1))) {
            binding.recyclerView.smoothScrollToPosition(quizViewModel.getCurrentPosition())
        } else {
            countDownTimer?.cancel()
            binding.progress.visibility = View.GONE
            Toast.makeText(requireContext(), "Congratulations You have completed your Quiz", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel() // Cancel the timer to prevent memory leaks
    }

    private fun setObserver() {
        quizViewModel.apply {
            question.observe(viewLifecycleOwner) { question ->
                if (!question.isNullOrEmpty()) {
                    questionList = question as MutableList<QuizModel.Question?>
                    questionListAdapter?.setData(question)
                }
            }
        }
    }

    companion object {
        const val TIME_LIMIT = 2000L
        const val TIME_INTERVAL = 1000L
    }
}