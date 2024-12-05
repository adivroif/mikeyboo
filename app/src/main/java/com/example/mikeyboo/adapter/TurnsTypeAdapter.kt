package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackType
import com.example.mikeyboo.callbacks.CallbackUser
import com.example.mikeyboo.databinding.DetailsTypeTurnBinding
import com.example.mikeyboo.databinding.DetailsUserBinding
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User

class TurnsTypeAdapter:RecyclerView.Adapter<TurnsTypeAdapter.PlayerViewHolder>() {
    private var context: Context? = null
    private var listOfType: List<Type>? = null
    var callbackOfType: CallbackType? = null

    fun TypeAdapter(context: Context?, typeList: List<Type>?)
    {
        this.context = context
        this.listOfType = typeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = DetailsTypeTurnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfType!!.size
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            with(listOfType!![position]) {
                binding.nameType.text = listOfType!![position].type + " - " + listOfType!![position].price + "₪"
            }

        }

    }

    fun getItem(position: Int): Type {
        return listOfType!![position]
    }

    fun updatePlayerList(newTypes: ArrayList<Type>?) {
        this.listOfType = newTypes
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: DetailsTypeTurnBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackOfType != null)
                    callbackOfType!!.typeClicked(
                        getItem(adapterPosition).toString(),
                        adapterPosition
                    )
                notifyDataSetChanged()
            }
        }
    }
}