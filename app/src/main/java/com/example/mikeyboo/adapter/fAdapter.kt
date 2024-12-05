package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackTurn
import com.example.mikeyboo.databinding.ForcancelBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.sharedData.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class fAdapter : RecyclerView.Adapter<fAdapter.PlayerViewHolder>() {
    private var context: Context? = null
    var callbackOfTurn: CallbackTurn? = null
    private var turns: ArrayList<Turn>? = null
    private var listOfAllTurns: ArrayList<Turn> = ArrayList()
    private var listOfUserTurns: ArrayList<Turn> = ArrayList()
    var dataManager = DataManager()
    val currentDate = LocalDate.now()
    var dayOf:Int = 0
    var monthOf:Int = 0
    var yearOf: Int = 0
    private var firebaseConnections = firebaseConnections()
    var turnsRef = firebaseConnections.initDBConnection("turns")

    fun FutureAdapter(context: Context?, turns: ArrayList<Turn>?,allTurns: ArrayList<Turn>,listOfUserTurns: ArrayList<Turn>) {
        this.listOfUserTurns = listOfUserTurns
        this.listOfAllTurns = allTurns
        this.context = context
        this.turns = turns

        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val formattedDate = currentDate.format(formatter)
        dayOf = formattedDate.split(" ")[0].toInt()
        monthOf = formattedDate.split(" ")[1].toInt()
        yearOf = formattedDate.split(" ")[2].toInt()
    }

    fun setPlayerCallback(callbackOfTurn: CallbackTurn?): fAdapter {
        this.callbackOfTurn = callbackOfTurn
        return this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ForcancelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfUserTurns.size
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            with(listOfUserTurns[position]) {
                    binding.type.text = listOfUserTurns[position].type.split("-")[0].substring(
                        1,
                        listOfUserTurns[position].type.split("-")[0].length - 1
                    )
                    if (listOfUserTurns[position].minutes == 0)
                        binding.time.text =
                            listOfUserTurns[position].hours.toString() + ":" + listOfUserTurns[position].minutes.toString() + "0"
                    else
                        binding.time.text =
                            listOfUserTurns[position].hours.toString() + ":" + listOfUserTurns[position].minutes.toString()
                    binding.date.text =
                        listOfUserTurns[position].day.toString() + "/" + listOfUserTurns[position].month.toString() + "/" + listOfUserTurns[position].year.toString()
                }
            }
        }

    fun getItem(position: Int): Turn {
        return listOfUserTurns[position]
    }

    fun cancelTurn(adapterPosition: Int, turn: Turn) {
        turns!!.removeAt(adapterPosition)
        for (i in 0 ..< listOfAllTurns.size) {
            if (listOfAllTurns[i].day == turn.day && listOfAllTurns[i].month == turn.month && listOfAllTurns[i].year == turn.year && listOfAllTurns[i].hours == turn.hours && listOfAllTurns[i].minutes == turn.minutes) {
                listOfAllTurns.removeAt(i)
                break
            }
        }
        for (i in 0 ..< listOfUserTurns.size) {
            if (listOfUserTurns[i].day == turn.day && listOfUserTurns[i].month == turn.month && listOfUserTurns[i].year == turn.year && listOfUserTurns[i].hours == turn.hours && listOfUserTurns[i].minutes == turn.minutes) {
                listOfUserTurns.removeAt(i)
                break
            }
        }
        dataManager.setListOfTurns(listOfAllTurns)
        dataManager.setListOfUsersTurns(listOfUserTurns)
        turnsRef.setValue(listOfAllTurns)
        SharedViewModel().setListOfTurns(turns!!)
        dataManager.setWaitListToPresent(turn, adapterPosition)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("מחיקת תור!")
        builder.setMessage("מחיקת התור בוצע בהצלחה")
        builder.setPositiveButton("אישור") { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: ForcancelBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackOfTurn != null)
                    callbackOfTurn!!.turnClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
            binding.cancel.setOnClickListener{cancelTurn(adapterPosition, getItem(adapterPosition))}
        }
    }
}