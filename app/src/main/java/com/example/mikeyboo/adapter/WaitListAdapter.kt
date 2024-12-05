package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackWait
import com.example.mikeyboo.databinding.HorizontalBinding
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.sharedData.DataManager

class WaitListAdapter  : RecyclerView.Adapter<WaitListAdapter.PlayerViewHolder>(){
    private var context: Context? = null
    private var list_of_turn_with_wait_list: ArrayList<Turn>? = null
    var callbackOfWait: CallbackWait? = null
    var dataManager = DataManager()

    fun WaitAdapter(context: Context?, list: ArrayList<Turn>?)
    {
        this.context = context
        this.list_of_turn_with_wait_list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = HorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list_of_turn_with_wait_list!!.size
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            if (list_of_turn_with_wait_list != null)
                for (i in 0..<list_of_turn_with_wait_list!!.size) {
                            var turn = list_of_turn_with_wait_list!![position]
                            Log.d("TAG", turn.type)
                            binding.type.text = turn.type.split("-")[0].substring(
                                1,
                                turn.type.split("-")[0].length - 1
                            )
                            if (turn.minutes == 0)
                                binding.time.text =
                                    turn.hours.toString() + ":" + turn.minutes.toString() + "0"
                            else
                                binding.time.text =
                                    turn.hours.toString() + ":" + turn.minutes.toString()
                            binding.date.text =
                                turn.day.toString() + "/" + turn.month.toString() + "/" + turn.year.toString()
                        }
                    }
                }

    fun deleteFromList(turn: Turn, index: Int) {
        list_of_turn_with_wait_list!!.remove(turn)
        dataManager.setWaitListInWaitList(turn,list_of_turn_with_wait_list)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("מיקום ברשימת המתנה הוסר!")
        builder.setMessage("הפעולה בוצעה בהצלחה")
        builder.setPositiveButton("אישור") { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
        notifyDataSetChanged()
    }



    fun getItem(position: Int): Turn {
        return list_of_turn_with_wait_list!![position]
    }

    fun updatePlayerList(newPlayers: ArrayList<Turn>?) {
        this.list_of_turn_with_wait_list = newPlayers
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: HorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackOfWait != null)
                    callbackOfWait!!.turnClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
                notifyDataSetChanged()
            }
            binding.delete.setOnClickListener{
                deleteFromList(getItem(adapterPosition),adapterPosition)
            }
        }
    }
}