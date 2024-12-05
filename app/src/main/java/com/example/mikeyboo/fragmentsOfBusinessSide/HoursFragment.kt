package com.example.mikeyboo.fragmentsOfBusinessSide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.mikeyboo.databinding.FragmentHoursBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.WorkDayHour
import com.example.mikeyboo.sharedData.DataManager
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class HoursFragment : Fragment() {
    private lateinit var listOfWorksHours : ArrayList<WorkDayHour>
    private var _binding: FragmentHoursBinding? = null
    private val binding get() = _binding!! // Use View Binding
    private var firebaseConnections = firebaseConnections()
    private var workHoursRef = firebaseConnections.initDBConnection("workHours")
    private var dataManager = DataManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHoursBinding.inflate(inflater, container, false)
        initViews()
        binding.isPermanentCheckBox.isChecked = false
        invisibleWeekData()
        invisiblePermenentData()


        return binding.root
    }

    private fun initViews() {
        binding.isPermanentCheckBox.setOnClickListener{if (binding.isPermanentCheckBox.isChecked) openPermenentVisible() else closePermenentVisible()}
        binding.sendBtnHoursPermenent.setOnClickListener {setTimesPermenent(binding.userEnterStartHourTextPermenent.text.toString(),binding.userEnterEndHourTextPermenent.text.toString(),binding.userEnterBreakHourTextPermenent.text.toString(),binding.userEnterBreakHourTimeTextPermenent.text.toString())}
        binding.sendBtnHoursWeek.setOnClickListener {setTimesWeek(binding.userEnterStartHourTextSunday.text.toString()
            ,binding.userEnterEndHourTextSunday.text.toString()
            ,binding.userEnterBreakHourTextSunday.text.toString()
            ,binding.userEnterBreakHourTimeTextSunday.text.toString()
            ,binding.userEnterStartHourTextMonday.text.toString()
            ,binding.userEnterEndHourTextMonday.text.toString()
            ,binding.userEnterBreakHourTextMonday.text.toString()
            ,binding.userEnterBreakHourTimeTextMonday.text.toString()
            ,binding.userEnterStartHourTextTuesday.text.toString()
            ,binding.userEnterEndHourTextTuesday.text.toString()
            ,binding.userEnterBreakHourTextTuesday.text.toString()
            ,binding.userEnterBreakHourTimeTextTuesday.text.toString()
            ,binding.userEnterStartHourTextWednesday.text.toString()
            ,binding.userEnterEndHourTextWednesday.text.toString()
            ,binding.userEnterBreakHourTextWednesday.text.toString()
            ,binding.userEnterBreakHourTimeTextWednesday.text.toString()
            ,binding.userEnterStartHourTextThursday.text.toString()
            ,binding.userEnterEndHourTextThursday.text.toString()
            ,binding.userEnterBreakHourTextThursday.text.toString()
            ,binding.userEnterBreakHourTimeTextThursday.text.toString()
            ,binding.userEnterStartHourTextFriday.text.toString()
            ,binding.userEnterEndHourTextFriday.text.toString()
            ,binding.userEnterBreakHourTextFriday.text.toString()
            ,binding.userEnterBreakHourTimeTextFriday.text.toString())
        }

    }

    private fun closePermenentVisible() {
        invisiblePermenentData()
        visibleWeekData()
    }

    private fun openPermenentVisible() {
        visiblePermenentData()
        invisibleWeekData()
    }

    private fun invisibleWeekData() {
        binding.daySundayText.visibility = View.INVISIBLE
        binding.dayMondayText.visibility = View.INVISIBLE
        binding.dayTuesdayText.visibility = View.INVISIBLE
        binding.dayWednesdayText.visibility = View.INVISIBLE
        binding.dayThursdayText.visibility = View.INVISIBLE
        binding.dayFridayText.visibility = View.INVISIBLE
        binding.userEnterStartHourTextSunday.visibility = View.INVISIBLE
        binding.userEnterEndHourTextSunday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextSunday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextSunday.visibility = View.INVISIBLE
        binding.breakHourTimeTextSunday.visibility = View.INVISIBLE
        binding.userEnterStartHourTextMonday.visibility = View.INVISIBLE
        binding.userEnterEndHourTextMonday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextMonday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextMonday.visibility = View.INVISIBLE
        binding.breakHourTimeTextMonday.visibility = View.INVISIBLE
        binding.userEnterStartHourTextTuesday.visibility = View.INVISIBLE
        binding.userEnterEndHourTextTuesday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextTuesday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextTuesday.visibility = View.INVISIBLE
        binding.breakHourTimeTextTuesday.visibility = View.INVISIBLE
        binding.userEnterStartHourTextWednesday.visibility = View.INVISIBLE
        binding.userEnterEndHourTextWednesday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextWednesday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextWednesday.visibility = View.INVISIBLE
        binding.breakHourTimeTextWednesday.visibility = View.INVISIBLE
        binding.userEnterStartHourTextThursday.visibility = View.INVISIBLE
        binding.userEnterEndHourTextThursday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextThursday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextThursday.visibility = View.INVISIBLE
        binding.breakHourTimeTextThursday.visibility = View.INVISIBLE
        binding.userEnterStartHourTextFriday.visibility = View.INVISIBLE
        binding.userEnterEndHourTextFriday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextFriday.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextFriday.visibility = View.INVISIBLE
        binding.breakHourTimeTextFriday.visibility = View.INVISIBLE
        binding.sendBtnHoursWeek.visibility = View.INVISIBLE
        binding.startHourTextSunday.visibility = View.INVISIBLE
        binding.endHourTextSunday.visibility = View.INVISIBLE
        binding.breakHourTextSunday.visibility = View.INVISIBLE
        binding.startHourTextMonday.visibility = View.INVISIBLE
        binding.endHourTextMonday.visibility = View.INVISIBLE
        binding.breakHourTextMonday.visibility = View.INVISIBLE
        binding.startHourTextTuesday.visibility = View.INVISIBLE
        binding.endHourTextTuesday.visibility = View.INVISIBLE
        binding.breakHourTextTuesday.visibility = View.INVISIBLE
        binding.startHourTextWednesday.visibility = View.INVISIBLE
        binding.endHourTextWednesday.visibility = View.INVISIBLE
        binding.breakHourTextWednesday.visibility = View.INVISIBLE
        binding.startHourTextThursday.visibility = View.INVISIBLE
        binding.endHourTextThursday.visibility = View.INVISIBLE
        binding.breakHourTextThursday.visibility = View.INVISIBLE
        binding.startHourTextFriday.visibility = View.INVISIBLE
        binding.endHourTextFriday.visibility = View.INVISIBLE
        binding.breakHourTextFriday.visibility = View.INVISIBLE
    }

    private fun visiblePermenentData() {
        binding.startHourLayoutPermenent.visibility = View.VISIBLE
        binding.userEnterStartHourTextPermenent.visibility = View.VISIBLE
        binding.startHourTextPermenent.visibility = View.VISIBLE
        binding.userEnterEndHourTextPermenent.visibility = View.VISIBLE
        binding.endHourTextPermenent.visibility = View.VISIBLE
        binding.breakHourLayoutPermenent.visibility = View.VISIBLE
        binding.userEnterBreakHourTextPermenent.visibility = View.VISIBLE
        binding.breakHourTextPermenent.visibility = View.VISIBLE
        binding.sendBtnHoursPermenent.visibility = View.VISIBLE
        binding.endHourLayoutPermenent.visibility = View.VISIBLE
        binding.breakHourLayoutPermenent.visibility = View.VISIBLE
        binding.breakHourTimeTextPermenent.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextPermenent.visibility = View.VISIBLE

    }

    private fun invisiblePermenentData() {
        binding.startHourLayoutPermenent.visibility = View.INVISIBLE
        binding.userEnterStartHourTextPermenent.visibility = View.INVISIBLE
        binding.startHourTextPermenent.visibility = View.INVISIBLE
        binding.userEnterEndHourTextPermenent.visibility = View.INVISIBLE
        binding.endHourTextPermenent.visibility = View.INVISIBLE
        binding.breakHourLayoutPermenent.visibility = View.INVISIBLE
        binding.userEnterBreakHourTextPermenent.visibility = View.INVISIBLE
        binding.breakHourTextPermenent.visibility = View.INVISIBLE
        binding.sendBtnHoursPermenent.visibility = View.INVISIBLE
        binding.endHourLayoutPermenent.visibility = View.INVISIBLE
        binding.breakHourLayoutPermenent.visibility = View.INVISIBLE
        binding.breakHourTimeTextPermenent.visibility = View.INVISIBLE
        binding.userEnterBreakHourTimeTextPermenent.visibility = View.INVISIBLE
    }

    private fun visibleWeekData() {
        binding.daySundayText.visibility = View.VISIBLE
        binding.dayMondayText.visibility = View.VISIBLE
        binding.dayTuesdayText.visibility = View.VISIBLE
        binding.dayWednesdayText.visibility = View.VISIBLE
        binding.dayThursdayText.visibility = View.VISIBLE
        binding.dayFridayText.visibility = View.VISIBLE
        binding.userEnterStartHourTextSunday.visibility = View.VISIBLE
        binding.userEnterEndHourTextSunday.visibility = View.VISIBLE
        binding.userEnterBreakHourTextSunday.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextSunday.visibility = View.VISIBLE
        binding.breakHourTimeTextSunday.visibility = View.VISIBLE
        binding.userEnterStartHourTextMonday.visibility = View.VISIBLE
        binding.userEnterEndHourTextMonday.visibility = View.VISIBLE
        binding.userEnterBreakHourTextMonday.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextMonday.visibility = View.VISIBLE
        binding.breakHourTimeTextMonday.visibility = View.VISIBLE
        binding.userEnterStartHourTextTuesday.visibility = View.VISIBLE
        binding.userEnterEndHourTextTuesday.visibility = View.VISIBLE
        binding.userEnterBreakHourTextTuesday.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextTuesday.visibility = View.VISIBLE
        binding.breakHourTimeTextTuesday.visibility = View.VISIBLE
        binding.userEnterStartHourTextWednesday.visibility = View.VISIBLE
        binding.userEnterEndHourTextWednesday.visibility = View.VISIBLE
        binding.userEnterBreakHourTextWednesday.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextWednesday.visibility = View.VISIBLE
        binding.breakHourTimeTextWednesday.visibility = View.VISIBLE
        binding.userEnterStartHourTextThursday.visibility = View.VISIBLE
        binding.userEnterEndHourTextThursday.visibility = View.VISIBLE
        binding.userEnterBreakHourTextThursday.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextThursday.visibility = View.VISIBLE
        binding.breakHourTimeTextThursday.visibility = View.VISIBLE
        binding.userEnterStartHourTextFriday.visibility = View.VISIBLE
        binding.userEnterEndHourTextFriday.visibility = View.VISIBLE
        binding.userEnterBreakHourTextFriday.visibility = View.VISIBLE
        binding.userEnterBreakHourTimeTextFriday.visibility = View.VISIBLE
        binding.breakHourTimeTextFriday.visibility = View.VISIBLE
        binding.sendBtnHoursWeek.visibility = View.VISIBLE
        binding.startHourTextSunday.visibility = View.VISIBLE
        binding.endHourTextSunday.visibility = View.VISIBLE
        binding.breakHourTextSunday.visibility = View.VISIBLE
        binding.startHourTextMonday.visibility = View.VISIBLE
        binding.endHourTextMonday.visibility = View.VISIBLE
        binding.breakHourTextMonday.visibility = View.VISIBLE
        binding.startHourTextTuesday.visibility = View.VISIBLE
        binding.endHourTextTuesday.visibility = View.VISIBLE
        binding.breakHourTextTuesday.visibility = View.VISIBLE
        binding.startHourTextWednesday.visibility = View.VISIBLE
        binding.endHourTextWednesday.visibility = View.VISIBLE
        binding.breakHourTextWednesday.visibility = View.VISIBLE
        binding.startHourTextThursday.visibility = View.VISIBLE
        binding.endHourTextThursday.visibility = View.VISIBLE
        binding.breakHourTextThursday.visibility = View.VISIBLE
        binding.startHourTextFriday.visibility = View.VISIBLE
        binding.endHourTextFriday.visibility = View.VISIBLE
        binding.breakHourTextFriday.visibility = View.VISIBLE
    }

    private fun setTimesWeek(
        userEnterStartHourTextSunday: String,
        userEnterEndHourTextSunday: String,
        userEnterBreakHourTextSunday: String,
        userEnterBreakHourTimeTextSunday: String,
        userEnterStartHourTextMonday: String,
        userEnterEndHourTextMonday: String,
        userEnterBreakHourTextMonday: String,
        userEnterBreakHourTimeTextMonday: String,
        userEnterStartHourTextTuesday: String,
        userEnterEndHourTextTuesday: String,
        userEnterBreakHourTextTuesday: String,
        userEnterBreakHourTimeTextTuesday: String,
        userEnterStartHourTextWednesday: String,
        userEnterEndHourTextWednesday: String,
        userEnterBreakHourTextWednesday: String,
        userEnterBreakHourTimeTextWednesday: String,
        userEnterStartHourTextThursday: String,
        userEnterEndHourTextThursday: String,
        userEnterBreakHourTextThursday: String,
        userEnterBreakHourTimeTextThursday: String,
        userEnterStartHourTextFriday: String,
        userEnterEndHourTextFriday: String,
        userEnterBreakHourTextFriday: String,
        userEnterBreakHourTimeTextFriday: String
    ) {
        listOfWorksHours
        listOfWorksHours.add(WorkDayHour(userEnterStartHourTextSunday.toInt(),userEnterEndHourTextSunday.toInt(),userEnterBreakHourTextSunday.toInt(),userEnterBreakHourTimeTextSunday.toInt()))
        listOfWorksHours.add(WorkDayHour(userEnterStartHourTextMonday.toInt(),userEnterEndHourTextMonday.toInt(),userEnterBreakHourTextMonday.toInt(),userEnterBreakHourTextMonday.toInt()))
        listOfWorksHours.add(WorkDayHour(userEnterStartHourTextTuesday.toInt(),userEnterEndHourTextTuesday.toInt(),userEnterBreakHourTextTuesday.toInt(),userEnterBreakHourTextTuesday.toInt()))
        listOfWorksHours.add(WorkDayHour(userEnterStartHourTextWednesday.toInt(),userEnterEndHourTextWednesday.toInt(),userEnterBreakHourTextWednesday.toInt(),userEnterBreakHourTimeTextWednesday.toInt()))
        listOfWorksHours.add(WorkDayHour(userEnterStartHourTextThursday.toInt(),userEnterEndHourTextThursday.toInt(),userEnterBreakHourTextThursday.toInt(),userEnterBreakHourTimeTextThursday.toInt()))
        listOfWorksHours.add(WorkDayHour(userEnterStartHourTextFriday.toInt(),userEnterEndHourTextFriday.toInt(),userEnterBreakHourTextFriday.toInt(),userEnterBreakHourTimeTextFriday.toInt()))
        workHoursRef.setValue(listOfWorksHours)
        dataManager.setListOfWorkHours(listOfWorksHours)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("הוספת שעות עבודה!")
        builder.setMessage("הוספת שעות עבודה בוצע בהצלחה")
        builder.setPositiveButton("אישור") { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setTimesPermenent(userEnterStartHourText: String, userEnterEndHourText: String, userEnterBreakHourText: String, userEnterBreakHourTimeText: String) {
        listOfWorksHours = ArrayList(20)
        for (i in 0..5) {
            listOfWorksHours.add(WorkDayHour(userEnterStartHourText.toInt(),userEnterEndHourText.toInt(),userEnterBreakHourText.toInt(),userEnterBreakHourTimeText.toInt()))
        }
        workHoursRef.setValue(listOfWorksHours)
        dataManager.setListOfWorkHours(listOfWorksHours)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("הוספת שעות עבודה!")
        builder.setMessage("הוספת שעות עבודה בוצע בהצלחה")
        builder.setPositiveButton("אישור") { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}