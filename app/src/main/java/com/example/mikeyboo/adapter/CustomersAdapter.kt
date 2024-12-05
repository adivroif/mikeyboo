package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackUser
import com.example.mikeyboo.callbacks.CallbackWait
import com.example.mikeyboo.databinding.DetailsUserBinding
import com.example.mikeyboo.databinding.HorizontalBinding
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.User
import com.example.mikeyboo.models.waitListsDB
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.bumptech.glide.Glide
import com.example.mikeyboo.sharedData.DataManager

class CustomersAdapter:RecyclerView.Adapter<CustomersAdapter.PlayerViewHolder>() {
    private var context: Context? = null
    private var listOfUsers: List<User>? = null
    private var listOfTurns: List<Turn>? = null
    var callbackOfUser: CallbackUser? = null
    val storage = Firebase.storage
    var dataManager: DataManager = DataManager()
    var listOfPhotos=dataManager.getListOfStoragePhotos()
    var storageRef = storage.reference

    fun UserAdapter(context: Context?, usersList: List<User>?,turnsList: List<Turn>?)
    {
        this.context = context
        this.listOfTurns = turnsList
        this.listOfUsers = usersList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = DetailsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfUsers!!.size
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        var photoRef :String? = null
        with(holder) {
            with(listOfUsers!![position]) {
                binding.name.text = listOfUsers!![position].name
                binding.ifThereIsPhoto.visibility = View.INVISIBLE
                for (i in 0..<listOfTurns!!.size) {
                    if (listOfUsers!![position].userName == listOfTurns!![i].user?.userName && listOfTurns!![i].day == listOfPhotos[i].StoragePhotos.turn.day && listOfTurns!![i].month == listOfPhotos[i].StoragePhotos.turn.month && listOfTurns!![i].year == listOfPhotos[i].StoragePhotos.turn.year) {
                            if (listOfPhotos[i].StoragePhotos.ref != null) {
                                var user = listOfUsers!![position]
                                Log.d("TAG", user.toString())
                                Glide.with(context!!)
                                    .load(listOfPhotos[i].StoragePhotos.ref)
                                    .centerCrop()
                                    .into(binding.photoToManager)
                                binding.ifThereIsPhoto.visibility = View.VISIBLE
                                if (listOfTurns!![i].minutes == 0) {
                                    binding.ifThereIsTurn.text = "תור קרוב : " + listOfTurns!![i].day + "/" + listOfTurns!![i].month + "/" +listOfTurns!![i].year + " " + listOfTurns!![i].hours + ":00"
                                } else {
                                    binding.ifThereIsTurn.text = "תור קרוב : " + listOfTurns!![i].day + "/" + listOfTurns!![i].month + "/" +listOfTurns!![i].year + " " + listOfTurns!![i].hours + ":" + listOfTurns!![i].minutes
                                break
                                }
                        }
                    }
                    }
                }
            }
    }

    fun getItem(position: Int): User {
        return listOfUsers!![position]
    }

    fun updatePlayerList(newUsers: List<User>?) {
        this.listOfUsers = newUsers
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: DetailsUserBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackOfUser != null)
                    callbackOfUser!!.userClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
    }
}