package com.example.mikeyboo.fragments_of_customer_side

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mikeyboo.activities.CalendarActivity
import com.example.mikeyboo.R
import com.example.mikeyboo.activities.OptionsActivty
import com.example.mikeyboo.adapter.reviewsAdapter
import com.example.mikeyboo.sharedData.SharedViewModel
import com.example.mikeyboo.models.User
import com.example.mikeyboo.databinding.FragmentHomeCustomerSideBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.waitListsDB
import com.example.mikeyboo.sharedData.DataManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private lateinit var mikeyText : MaterialTextView

    private lateinit var cancel_turn :MaterialButton

    private lateinit var change_turn :MaterialButton

    private lateinit var main_LST_scores : MaterialTextView

    private lateinit var address : MaterialTextView

    private lateinit var instagram_icon : ShapeableImageView

    private lateinit var whatsapp_icon : ShapeableImageView

    private lateinit var phone_icon : ShapeableImageView

    private lateinit var googleMap_icon : ShapeableImageView

    private lateinit var worksText : MaterialTextView

    private lateinit var what_do_today_text : MaterialTextView

    private lateinit var lak_gel : MaterialButton

    private lateinit var milui : MaterialButton

    private lateinit var menikur : MaterialButton

    private lateinit var tikun : MaterialButton

    private lateinit var hasara : MaterialButton

    private lateinit var tzior : MaterialButton

    private lateinit var bnia_hadash : MaterialButton

    private var _binding: FragmentHomeCustomerSideBinding? = null

    private var firebaseConnections = firebaseConnections()

    private var turnsRef = firebaseConnections.initDBConnection("turns")

    private var reviewsRef = firebaseConnections.initDBConnection("reviews")

    private var waitListRef = firebaseConnections.initDBConnection("waitList")

    private var listOfWaitList = ArrayList<waitListsDB>()

    private var listOfTurns = ArrayList<Turn>()

    private var listOfReviews = ArrayList<String>()

    private var adapter: reviewsAdapter = reviewsAdapter()

    private var listOfUser = ArrayList<User>()

    private val viewModel: SharedViewModel by viewModels()

    private var closeTurn : Turn? = null


    private val listOfButton: ArrayList<MaterialButton> = ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var dataManager = DataManager()

    private var listOfType = dataManager.getListOfTypes()
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeCustomerSideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.mikeyText
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        findViews(binding.root)
        listOfReviews = dataManager.getListOfReviews()
        initViews()

        binding.cancelTurn.visibility = View.INVISIBLE
        binding.changeTurn.visibility = View.INVISIBLE
        user = SharedViewModel().getUser()
        binding.reviewsList.adapter = adapter
        binding.reviewsList.layoutManager = LinearLayoutManager(context)

        loadDat()
        val getFromLogin: String? = viewModel.getData()
        if (getFromLogin != null) {
            Log.d("Get From Login Home", getFromLogin)
        }
        if (getFromLogin != null) {
            user = User(
                getFromLogin.split(",")[1].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[0].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[3].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[5].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[2].split(":")[1].split(" ")[0],
                getFromLogin.split(",")[4].split(":")[1].split(" ")[0]
            )
            user = User(
                user!!.name.substring(1, user!!.name.length - 1),
                user!!.mail.substring(1, user!!.mail.length - 1),
                user!!.phoneNumber.substring(1, user!!.phoneNumber.length - 1),
                user!!.userName.substring(1, user!!.userName.length - 2),
                user!!.password.substring(1, user!!.password.length - 1),
                user!!.token.substring(1, user!!.token.length - 1)
            )
            mikeyText.text = "היי " + user!!.name + ", "
        }
        user = dataManager.getUser()
        listOfButton.clear()
        listOfButton.add(lak_gel)
        listOfButton.add(milui)
        listOfButton.add(menikur)
        listOfButton.add(tzior)
        listOfButton.add(hasara)
        listOfButton.add(tikun)
        listOfButton.add(bnia_hadash)

            for (i in 0..<listOfButton.size) {
                if (listOfType.size > i)
                    listOfButton[i].text =
                        listOfType[i].type + " - " + listOfType[i].price + "₪"
                else
                    listOfButton[i].visibility = View.INVISIBLE
            }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findViews(view : View) {
        mikeyText = view.findViewById(R.id.mikeyText)
        cancel_turn = view.findViewById(R.id.cancel_turn)
        change_turn = view.findViewById(R.id.change_turn)
        main_LST_scores = view.findViewById(R.id.main_LST_scores)
        instagram_icon = view.findViewById(R.id.instagram_icon)
        whatsapp_icon = view.findViewById(R.id.whatsapp_icon)
        phone_icon = view.findViewById(R.id.phone_icon)
        googleMap_icon = view.findViewById(R.id.googleMap_icon)
        worksText = view.findViewById(R.id.worksText)
        what_do_today_text = view.findViewById(R.id.what_do_today_text)
        lak_gel = view.findViewById(R.id.lak_gel)
        milui = view.findViewById(R.id.milui)
        menikur = view.findViewById(R.id.menikur)
        tikun = view.findViewById(R.id.tikun)
        hasara = view.findViewById(R.id.hasara)
        tzior = view.findViewById(R.id.tzior)
        bnia_hadash = view.findViewById(R.id.bnia_hadash)
        //fragment_history_turns = findViewById(R.id.fragment_history_turns)
    }

    private fun initViews() {
        binding.btnAddReview.setOnClickListener{addReview(binding.userEnterReview.text.toString())}
        instagram_icon.setOnClickListener {MoveToInstagram()}
        whatsapp_icon.setOnClickListener {MoveToWhatsapp()}
        phone_icon.setOnClickListener {MoveToPhone()}
        googleMap_icon.setOnClickListener {MoveToGoogleMaps()}
        lak_gel.setOnClickListener{OpenCalendar(lak_gel)}
        milui.setOnClickListener{OpenCalendar(milui)}
        menikur.setOnClickListener{OpenCalendar(menikur)}
        tikun.setOnClickListener{OpenCalendar(tikun)}
        hasara.setOnClickListener{OpenCalendar(hasara)}
        tzior.setOnClickListener{OpenCalendar(tzior)}
        cancel_turn.setOnClickListener{Cancel(closeTurn)}
        change_turn.setOnClickListener{change(closeTurn)}
        bnia_hadash.setOnClickListener{OpenCalendar(bnia_hadash)}
        adapter = reviewsAdapter()
        adapter.reviewAdapter(context, listOfReviews)

            var linearManager = LinearLayoutManager(this.getContext())
            linearManager.setOrientation(LinearLayoutManager.VERTICAL)
            binding.reviewsList.setLayoutManager(linearManager)
            binding.reviewsList.setAdapter(adapter)
    }

    private fun addReview(userEnterReview: String) {
        if (userEnterReview.isNotEmpty()) {
            listOfReviews.add(userEnterReview)
            reviewsRef.setValue(listOfReviews)
            dataManager.updateReviewsFromDB()
            binding.userEnterReview.setText("")
            adapter = reviewsAdapter()
            adapter.reviewAdapter(context, listOfReviews)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("הוספת ביקורת!")
            builder.setMessage("הביקורת נוספה בהצלחה")
            builder.setPositiveButton("אישור") { dialog, which ->
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            binding.userEnterReview.setText("")
            adapter = reviewsAdapter()
            adapter.reviewAdapter(context, listOfReviews)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("הוספת ביקורת!")
            builder.setMessage("על מנת להוסיף ביקורת הזיני ביקורת")
            builder.setPositiveButton("אישור") { dialog, which ->
            }
            val dialog = builder.create()
            dialog.show()
            }
        }

    private fun moveToOptions()
    {
        val intent = Intent(requireContext(), OptionsActivty::class.java)
        val gson = Gson()
        val json = gson.toJson(user)
        intent.putExtra("user from homeFragment", json)
        intent.putExtra("yes", "1")
        startActivity(intent)
    }

    private fun change(closeTurn: Turn?) {
        val gson = Gson()
        val json = gson.toJson(user)
        Log.d("JSON", json)
        val jsonOfButton = gson.toJson(closeTurn?.type)
        val jsonOfCloseTurn = gson.toJson(closeTurn)
        val intent = Intent(requireContext(), CalendarActivity::class.java)
        intent.putExtra("change","1")
        intent.putExtra("turnToChange",jsonOfCloseTurn)
        intent.putExtra("user", json)
        intent.putExtra("button", jsonOfButton)
        startActivity(intent)
        return
    }

    fun Cancel(closeTurn: Turn?) {
        listOfWaitList = dataManager.getListOfWaitList()
        for (i in 0..<listOfWaitList.size) {
            if (listOfWaitList[i].day == closeTurn!!.day && listOfWaitList[i].month == closeTurn.month && listOfWaitList[i].year == closeTurn.year) {
                listOfWaitList[i].waitList[closeTurn.indexInWaitList]--
                if (listOfWaitList[i].listOfTurnWaiting.isNotEmpty()) {
                    for (j in 0..<listOfWaitList[i].listOfTurnWaiting.size) {
                        if (listOfWaitList[i].listOfTurnWaiting[j].day == closeTurn.day && listOfWaitList[i].listOfTurnWaiting[j].month == closeTurn.month && listOfWaitList[i].listOfTurnWaiting[j].year == closeTurn.year && listOfWaitList[i].listOfTurnWaiting[j].hours == closeTurn.hours && listOfWaitList[i].listOfTurnWaiting[j].minutes == closeTurn.minutes) {
                            listOfTurns.add(listOfWaitList[i].listOfTurnWaiting[j])
                            listOfWaitList[i].listOfTurnWaiting.removeAt(j)
                        }
                    }
                    val notificationData = mapOf(
                        "title" to "Mikeyboo",
                        "body" to "Notification Body"
                    )

                    val message = user?.let {
                        RemoteMessage.Builder(it.token)
                            .setData(notificationData) // Use setData() instead of setNotification()
                            .build()
                    }

                    if (message != null) {
                        FirebaseMessaging.getInstance().send(message)
                    }
                }
                /*
                var notification = NotificationCompat.Builder(this,"Channel")
                notification.setSmallIcon(R.drawable.baseline_access_time_24)
                    .setContentTitle("MikeyBoo")
                    .setContentText("התור לו המתנת התפנה ונקבע , על מנת לבטל/לצפות נא ללחוץ על ההודעה")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                var intent = Intent(this, OptionsActivty::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                var pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE)
                notification.setContentIntent(pendingIntent)
                var manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    var channel = manager.getNotificationChannel("Channel")
                    if(channel == null) {
                        channel = NotificationChannel("Channel","name",NotificationManager.IMPORTANCE_HIGH)
                    }
                    channel.lightColor = R.color.pink
                    channel.enableVibration(true)
                    manager.createNotificationChannel(channel)
                }
                manager.notify(0,notification.build())

               */
                viewModel.setWaitListTurns(listOfWaitList)
                waitListRef.setValue(listOfWaitList)
                break
            }
        }

        listOfTurns = dataManager.getListOfTurns()
        for (i in listOfTurns) {
            if (i.day == closeTurn!!.day && i.month == closeTurn.month && i.year == closeTurn.year && i.hours == closeTurn.hours && i.minutes == closeTurn.minutes) {
                listOfTurns.remove(i)
                dataManager.setListOfTurns(listOfTurns)
                viewModel.setListOfTurns(listOfTurns)
                turnsRef.setValue(listOfTurns)
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("ביטול תור!")
                builder.setMessage("התור בוטל בהצלחה")
                builder.setPositiveButton("אישור") { dialog, which ->
                    moveToOptions()
                }
                val dialog = builder.create()
                dialog.show()

            }
        }
    }

    private fun OpenCalendar(button: MaterialButton) {
        val gson = Gson()
        val jsonOfUser = gson.toJson(user)
        Log.d("JSON", jsonOfUser)
        val jsonOfButton = gson.toJson(button.text.split("-")[0].substring(0,button.text.split("-")[0].length-1))
        val intent = Intent(requireContext(), CalendarActivity::class.java)
        intent.putExtra("button", jsonOfButton)
        intent.putExtra("user", jsonOfUser)
        startActivity(intent)
    }

    private fun loadDat() {
        turnsRef.addValueEventListener(object : ValueEventListener { // For realtime data fetching from DB
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value: ArrayList<Turn>? = dataSnapshot.getValue(object : GenericTypeIndicator<ArrayList<Turn>>() {})
                //Log.d("Turns in DataManager", value?.size.toString())
                var listOfUser: ArrayList<Turn>? = ArrayList()
                if (value != null) {
                    listOfTurns = value

                    Log.d("Turns list in DataManager", listOfTurns.size.toString())
                    for (i in listOfTurns) {
                        if (i.user?.userName == user!!.userName) {
                            listOfUser?.add(i)
                            if (listOfUser != null) {
                                dataManager.setListOfUsersTurns(listOfUser)
                            }
                        }
                    }
                }
                Log.d("Turns usersssssss1 in DataManager", listOfUser?.size.toString())
                dataManager.setListOfTurns(listOfTurns)
                if (listOfUser != null) {
                    dataManager.setListOfUsersTurns(listOfUser)
                }
                listOfUser?.sortWith(compareBy<Turn> { it.year }.thenBy { it.month }.thenBy { it.day }.thenBy { it.hours }.thenBy { it.minutes })

                listOfUser = listOfUser?.let { dataManager.getFutureListOfUsersTurns(it) }
                if(listOfUser?.isNotEmpty() == true) {
                    cancel_turn.visibility = View.VISIBLE
                    change_turn.visibility = View.VISIBLE
                    closeTurn = listOfUser[0]
                    if (closeTurn?.minutes == 0)
                        binding.mainLSTScores.text =
                            closeTurn?.day.toString() + "/" + closeTurn?.month.toString() + "/" + closeTurn?.year.toString() + "      " + closeTurn?.hours.toString() + ":" + closeTurn?.minutes.toString() + "0" + "      " + closeTurn?.type?.substring(
                                1,
                                closeTurn?.type?.length!! - 1
                            )
                    else
                        main_LST_scores.text =
                            closeTurn?.day.toString() + "/" + closeTurn?.month.toString() + "/" + closeTurn?.year.toString() + "      " + closeTurn?.hours.toString() + ":" + closeTurn?.minutes.toString() + "      " + closeTurn?.type?.substring(
                                1,
                                closeTurn?.type?.length!! - 1
                            )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Data Error", "Failed to read value.", error.toException())
            }
        })
    }

    private fun MoveToGoogleMaps() {
        val latitude = 32.0853 // החלף בקו רוחב הרצוי
        val longitude = 34.7818 // החלף בקו אורך הרצוי
        val address = "רחוב דיזנגוף 100, תל אביב" // החלף בכתובת הרצויה

        try {
            // ניסיון פתיחת Waze עם מיקום
            val url = "https://waze.com/ul?ll=$latitude,$longitude&navigate=yes"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                // ניסיון פתיחת Waze עם כתובת
                val url = "https://waze.com/ul?q=$address"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // טיפול במקרה ש-Waze לא מותקן במכשיר
            }
        }
    }

    private fun MoveToPhone() {
        val phoneNumber = "+972545250413" // החלף במספר הטלפון הרצוי

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // טיפול במקרה שאפליקציית החייגן לא נמצאה
        }
    }

    private fun MoveToWhatsapp() {
        val phoneNumber = "+972545250413" // החלף במספר הטלפון הרצוי
        val message = "שלום!" // החלףבהודעה הרצויה

        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // טיפול במקרה ש-WhatsApp לא מותקן במכשיר
        }
    }

    private fun MoveToInstagram() {
        val username = "mikeyboo__" // החלף בשם המשתמש הרצוי

        val uri = Uri.parse("http://instagram.com/_u/$username")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.instagram.android") // הגדרת חבילת Instagram

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // טיפול במקרה ש-Instagram לא מותקן במכשיר
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$username")))
        }
    }
}