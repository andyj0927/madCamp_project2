package com.madcamp.project2.Game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.madcamp.project2.Data.Card
import com.madcamp.project2.Global
import com.madcamp.project2.R

class CardAdapter(
    private var list: MutableList<Card>
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var numberTextView: TextView
        private var backImageView: ImageView

        init {
            numberTextView = itemView.findViewById(R.id.card_front)
            backImageView = itemView.findViewById(R.id.card_back)
            itemView.setOnClickListener(this)
        }

        fun bind(item: Card){
            numberTextView.text = item.num.toString()

            if(item.status) {
                backImageView.visibility = View.GONE
            } else {
                backImageView.visibility = View.VISIBLE
            }
        }

        override fun onClick(v: View?) {
            if(Global.pos1 == -1) Global.pos1 = Integer.parseInt(numberTextView.text.toString())
            else if(Global.pos2 == -1) Global.pos2 = Integer.parseInt(numberTextView.text.toString())
            else return
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card, parent, false)

        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}