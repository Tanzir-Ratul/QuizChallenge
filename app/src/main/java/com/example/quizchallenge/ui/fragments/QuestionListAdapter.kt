package com.example.quizchallenge.ui.fragments

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizchallenge.R
import com.example.quizchallenge.databinding.QuestionListAdapterBinding
import com.example.quizchallenge.ui.models.QuizModel
import com.example.quizchallenge.utils.customizeClass.dpToPxx

class QuestionListAdapter(val ctx: Context) :
    RecyclerView.Adapter<QuestionListAdapter.QuestionViewHolder>() {


    private var questionList: MutableList<QuizModel.Question?> = mutableListOf()

    var ansMap = HashMap<String,Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(
            QuestionListAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questionList[position])
    }

    fun setData(list: List<QuizModel.Question?>) {
        questionList.clear()
        questionList.addAll(list)
        //   Log.d("listCheck", "setData: ${questionList.size}")
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(private val binding: QuestionListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuizModel.Question?) {
            binding.pointsTV.text = question?.score.toString()
            binding.questionTV.text = question?.question
            setOptionViewDynamically(question)


        }

        private fun onAnswerSelected(key: String) {

            val correctAnswer = questionList[adapterPosition]?.correctAnswer
            Log.d("Answer", "Selected Answer: $selectedAnswer")
            Log.d("Answer", "Correct Answer: $correctAnswer")
            if (selectedAnswer == correctAnswer) {
                //  binding.containerLL.getChildAt(adapterPosition).setBackgroundColor(ContextCompat.getColor(ctx, R.color.green))
               // binding.containerLL.getChildAt(adapterPosition).setBackgroundResource(R.drawable.option_bg_correct)
            } else {
                //  binding.containerLL.getChildAt(adapterPosition).setBackgroundColor(ContextCompat.getColor(ctx, R.color.red))
               // binding.containerLL.getChildAt(adapterPosition).setBackgroundResource(R.drawable.option_bg_wrong)
            }
        }

        private fun setOptionViewDynamically(question: QuizModel.Question?) {
            val typeface = ResourcesCompat.getFont(itemView.context, R.font.poppins_regular)
            binding.containerLL.removeAllViews()
            question?.answers?.let { answers ->
                val shuffledOptions = listOf(answers.A, answers.B, answers.C, answers.D,).shuffled()
                for ((index, shuffledOption) in shuffledOptions.withIndex()) {
                    shuffledOption?.let {str->
                        val answerTextView = TextView(itemView.context)
                        answerTextView.text = str // Set answer text

                        answerTextView.textSize = 18f
                        answerTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        ansMap[str] = index
                        answerTextView.setOnClickListener { onAnswerSelected(str) }

                        answerTextView.setBackgroundResource(R.drawable.option_bg)
                        answerTextView.setPadding(5, 5, 5, 5)
                        answerTextView.id = index
                        val layoutParams = LinearLayout.LayoutParams(
                            (ctx.resources.displayMetrics.widthPixels * 0.7).toInt(),
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        val marginBetweenViews = dpToPxx(itemView.context, 10) // Set the desired margin between TextViews
                        Log.d("Margin", "Top Margin: $marginBetweenViews")

                        if (index == 0) {
                            layoutParams.setMargins(0, 0, 0, 0)
                        } else {
                            layoutParams.setMargins(0, marginBetweenViews, 0, 0)
                        }

                        answerTextView.layoutParams = layoutParams
                        answerTextView.gravity = Gravity.CENTER
                        answerTextView.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.black
                            )
                        )
                        answerTextView.typeface = typeface

                        binding.containerLL.addView(answerTextView)
                    }

                }
            }
        }
    }


}
