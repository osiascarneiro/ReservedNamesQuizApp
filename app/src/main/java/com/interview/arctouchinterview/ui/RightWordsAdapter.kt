package com.interview.arctouchinterview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.interview.arctouchinterview.R
import com.interview.arctouchinterview.model.Keyword
import kotlinx.android.synthetic.main.right_word_item.view.*

class RightWordsAdapter(private var list: MutableList<Keyword>): RecyclerView.Adapter<RightWordsAdapter.RightWordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RightWordsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RightWordsViewHolder(inflater.inflate(R.layout.right_word_item, parent, false))
    }

    override fun onBindViewHolder(holder: RightWordsViewHolder, position: Int) {
        holder.text.text = list[position].value
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: MutableList<Keyword>) {
        this.list = newList
        notifyDataSetChanged()
    }

    fun addWord(word: Keyword) {
        this.list.add(word)
        notifyItemChanged(list.size-1)
    }

    class RightWordsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val text = view.rightAnswerText
    }

}