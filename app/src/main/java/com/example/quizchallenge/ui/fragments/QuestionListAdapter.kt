package com.example.quizchallenge.ui.fragments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizchallenge.databinding.QuestionListAdapterBinding
import com.example.quizchallenge.ui.models.QuizModel
import com.squareup.picasso.Picasso

class QuestionListAdapter(val ctx: Context) :
    RecyclerView.Adapter<QuestionListAdapter.QuestionViewHolder>() {

    var setTextViewCallback: ((QuizModel.Question?, List<AppCompatTextView>, LinearLayoutCompat, Int) -> Unit)? =
        null
    var imageSetCallBack:((String, ImageView)->Unit)? = null
    private var questionList: MutableList<QuizModel.Question?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(
            QuestionListAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        Log.d("position", "bind: $position")
        holder.bind(questionList[position], position)
    }

    fun setData(list: List<QuizModel.Question?>) {
        questionList.clear()
        questionList.addAll(list)
        notifyDataSetChanged()
    }


    inner class QuestionViewHolder(private val binding: QuestionListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuizModel.Question?, position: Int) {
            Log.d("modPos", "$adapterPosition")
            if (adapterPosition != RecyclerView.NO_POSITION) {
                binding.pointsTV.text = "${question?.score} points"
                binding.questionTV.text = question?.question
                setTextViewCallback?.invoke(
                    question, listOf(
                        binding.textView1, binding.textView2, binding.textView3, binding.textView4
                    ).shuffled(), binding.containerLL, adapterPosition
                )
                question?.questionImageUrl?.let { setImage(it,adapterPosition) }
                //question?.questionImageUrl?.let { imageSetCallBack?.invoke(it, binding.imageView) }
            }


        }

        private fun setImage(link: String, adapterPosition: Int) {
            if (link.isNotEmpty()) {
                Log.d("link", "setImage: $link pos:$adapterPosition")
                Picasso.get().load(link).into(binding.imageView)

            }
        }
    }

}
