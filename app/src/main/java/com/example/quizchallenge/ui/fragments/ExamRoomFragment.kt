package com.example.quizchallenge.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.bumptech.glide.Glide
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
    private var countDownTimer: CountDownTimer? = null
    private var questionList: MutableList<QuizModel.Question?> = mutableListOf()
    private var answerMap: Map<Int, Map<String, String?>> = mapOf()
    private var selectedAnswer: String? = ""
    private var correctAns: String? = ""
    private var isAnswered = false
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

               try {
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
                    val innerAnsMap = answerMap[position]

                    // Log.d("innerAnsMap", "innerAnsMap: ${(innerAnsMap)}")

                    if (innerAnsMap != null) {
                        val positionWiseSize = innerAnsMap.values.count { it != null } ?: 0
                        // Log.d("innerAnsMap", "size: ${positionWiseSize}")
                        if (positionWiseSize > 0) {
                            val keys = innerAnsMap.keys.toList()
                            for (i in 0 until positionWiseSize) {
                                val key = keys[i]
                                val value = innerAnsMap[key]


                                if (value != null) {
                                    val textView = when (key) {
                                        "A" -> list[0]
                                        "B" -> list[1]
                                        "C" -> list[2]
                                        "D" -> list[3]
                                        else -> null
                                    }
                                    textView?.setBackgroundResource(R.drawable.option_bg)
                                    if (textView != null && innerAnsMap[key] != null) {
                                        // Log.d("innerKey", "${key} ${value}")
                                        textView.text = innerAnsMap[key]?.trim()
                                        textView.visibility = View.VISIBLE
                                        textView.isClickable = true

                                        textView.setOnClickListener {
                                            isAnswered = true
                                            selectedAnswer = value
                                            onAnswerSelected(value, linearLayout, correctAns, score)
                                        }

                                    }
                                }
                                // Hide remaining TextViews
                                for (index in positionWiseSize until list.size) {
                                    list[index].visibility = View.GONE
                                }
                            }
                        } else {
                            //if there is no option for the question
                            for (textView in list) {
                                textView.visibility = View.GONE
                            }
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }


        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (shouldNavigateBack()) {
                    findNavController().navigateUp()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )


        questionListAdapter?.imageSetCallBack = { url, imageView ->
            try {
                if (url.isNotEmpty()) {
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.logo_hero)
                        .error(R.drawable.image_placeholder_ot_error)
                        .into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.logo_hero)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

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
            countDownTimer?.cancel()
            Handler(Looper.getMainLooper()).postDelayed({
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
        try{
            if (quizViewModel.currentPos.value != (questionListAdapter?.itemCount?.minus(1))) {
                binding.recyclerView.smoothScrollToPosition(quizViewModel.getCurrentPosition())
            } else {
                countDownTimer?.cancel()
                binding.progress.visibility = View.GONE
                if(context!=null){
                    Toast.makeText(
                        requireContext(),
                        "Congratulations You have completed your test",
                        Toast.LENGTH_LONG
                    ).show()
                }
                findNavController().popBackStack()
            }
        }catch (e:Exception){
            e.printStackTrace()
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
        const val TIME_LIMIT = 10000L
        const val TIME_INTERVAL = 1000L
    }
}