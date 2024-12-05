package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackTurn
import com.example.mikeyboo.databinding.ForhistoryBinding
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.databinding.HorizontalBinding

class hAdapter : RecyclerView.Adapter<hAdapter.PlayerViewHolder>() {
    private var context: Context? = null
    private var turns: List<Turn>? = null
    var callbackOfTurn: CallbackTurn? = null



    fun HistoryAdapter(context: Context?, turns: List<Turn>?) {
        this.context = context
        this.turns = turns
    }

    fun setPlayerCallback(callbackOfTurn: CallbackTurn?): hAdapter {
        this.callbackOfTurn = callbackOfTurn
        return this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ForhistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return turns!!.size
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            with(turns!![position]) {
                Log.d("TAG", turns!![position].type.split("-")[0])
                binding.type.text = turns!![position].type.split("-")[0].substring(1,turns!![position].type.split("-")[0].length-1)
                if(turns!![position].minutes == 0)
                    binding.time.text = turns!![position].hours.toString() + ":" + turns!![position].minutes.toString() + "0"
                else
                    binding.time.text = turns!![position].hours.toString() + ":" + turns!![position].minutes.toString()
                binding.date.text = turns!![position].day.toString() + "/" + turns!![position].month.toString() + "/" + turns!![position].year.toString()
            }

        }
    }

    fun getItem(position: Int): Turn {
        return turns!![position]
    }

    fun updatePlayerList(newPlayers: List<Turn>?) {
        this.turns = newPlayers
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: ForhistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackOfTurn != null)
                    callbackOfTurn!!.turnClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
    }
}