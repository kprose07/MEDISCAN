package com.example.mediscan.Fragments


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.*
import com.example.mediscan.Adapter.NotesAdapter
import com.example.mediscan.Adapter.ProfileRemindAdapter
import com.example.mediscan.Adapter.SavedAdapter
import com.example.mediscan.Data.*
import com.example.mediscan.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.mediscan.Data.Notes
import com.example.mediscan.Data.ProfileRemind
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_saved.*
import java.util.*


class SavedFragment : Fragment() {
    private var TAG = "Saved Fragment"
    private lateinit var savedMedicineDB: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var comm:Communicator
    private lateinit var specialNotes: LinearLayout
    private lateinit var saveNote: Button
    private lateinit var notesTitle: TextInputEditText
    private lateinit var notesBody: TextInputEditText
    private lateinit var addNote: ImageView
    private lateinit var saveNotesDB: DatabaseReference


    private val savedMedicineList = mutableListOf<SavedMedicine>()
    val remindList = ArrayList<ProfileRemind>()
    val notesList = ArrayList<Notes>()
    val savedNotesList = ArrayList<Note>()
    var item:String = ""
    private lateinit var spinner: Spinner
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_saved, container,false)
        comm = requireActivity() as Communicator
        firebaseAuth = FirebaseAuth.getInstance()
        specialNotes = view.findViewById(R.id.specialNotes)
        saveNote = view.findViewById(R.id.notesSave)
        notesTitle = view.findViewById(R.id.noteTitleInput)
        notesBody = view.findViewById(R.id.notesBodyInput)
        addNote = view.findViewById(R.id.addNote)
        loadSavedMedicines()
        notesddata()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //createNotificationChannel()

        // access the items of the list
        val meds = resources.getStringArray(R.array.allergies)

        // access the spinner
        spinner = view.findViewById(R.id.spinner_remind_meds)
        val arrayAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(requireActivity().applicationContext, R.layout.spinnertext, meds)
        arrayAdapter.setDropDownViewResource(R.layout.spinnertext)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                item = parent.getItemAtPosition(position).toString()
                remind_text.text = item
                Toast.makeText(context, "Selected: $item", Toast.LENGTH_SHORT).show()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, "Noting Selcted", Toast.LENGTH_SHORT).show()

            }
        }


        //remind adapter
        profile_reminder.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        profile_reminder.adapter = ProfileRemindAdapter(remindList,remind_popupcard, spinner, remind_close)

        //Saved Medicine
        saved_medication.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        saved_medication.adapter = SavedAdapter(savedMedicineList, comm, savedMedicineDB)
        //Notes
        notes_medication.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        notes_medication.adapter = NotesAdapter(savedNotesList)

        //static data
        if (remindList.isEmpty()) reminddata()
        if (notesList.isEmpty()) notesddata()
        if (savedNotesList.isEmpty()) loadNotes()


        //createNotificationChannel()
        createchannelnotify()
        ser_remind_button.setOnClickListener {
            //select time
            showTimePicker()
        }
        add_remind_button.setOnClickListener {
            //add alarm
            //setAlarm()
            scheduleNotify()
        }
        cancle_remind.setOnClickListener {
            //cancle alarm
            //cancleAlarm()

        }
        remind_close.setOnClickListener{
            //remind_popupcard.visibility = View.GONE
            //remind_card_title.setText(item)
        }

        saveNote.setOnClickListener{v: View ->
            saveNote()
            specialNotes.visibility = View.GONE
        }
        addNote.setOnClickListener{v: View ->
            specialNotes.visibility = View.VISIBLE
        }
    }

    private fun scheduleNotify() {
        val intent = Intent(requireActivity().applicationContext,NotificationRecieve::class.java)
        val title = item
        val detail = description_remind_input.text.toString()
        intent.putExtra(titleExtra,title)
        intent.putExtra(messageEtra,detail)

        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity().applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        )
        val alarmManager = requireActivity().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time,title,detail)

    }

    private fun showAlert(time: Long, title: String, detail: String) {
        val date = Date(time)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireActivity().applicationContext)
        AlertDialog.Builder(requireActivity().applicationContext)
            .setTitle("Reminder Set")
            .setMessage(
                "Title: "+title+
                        "\nMessgae: " + detail+
                        "\nAt: "+ timeFormat.format(date)
            )
            .setPositiveButton("Okay"){_,_ ->}
            .show()
        Toast.makeText(context,"Reminder Created",Toast.LENGTH_SHORT).show()
    }
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Selct Time")
            .build()

        picker.show(parentFragmentManager ,"LillyMedicines")
        picker.addOnPositiveButtonClickListener{
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }

    }
    private fun getTime(): Long {

        return calendar.timeInMillis
    }

    private fun createchannelnotify() {
        val name = "Saaved Remind"
        val desc = "Details..."
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID,name,importance)
        channel.description = desc

        val notificationManager = requireActivity().applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
    /*
    private fun cancleAlarm() {
        alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity().applicationContext, AlarmRecivever::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireActivity().applicationContext,0,intent,0)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context,"Alarm Cancelled", Toast.LENGTH_SHORT).show()


    }
    private fun setAlarm() {
        alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity().applicationContext, AlarmRecivever::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireActivity().applicationContext,0,intent,0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(context,"Alarm Set Sucessfuly", Toast.LENGTH_SHORT).show()


    }
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Selct Time")
            .build()

        picker.show(parentFragmentManager ,"LillyMedicines")
        picker.addOnPositiveButtonClickListener{
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }

    }

    private fun createNotificationChannel() {
        // the NotificationChannel class is new and not in the support library
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence ="Test"
            val description = "Test"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("LillyMedicines", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(requireActivity().applicationContext,DestinationActivity::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }*/

    private fun reminddata(){
        remindList.add(
            ProfileRemind(
                "Rose",
                1,
                true

            )
        )
        remindList.add(
            ProfileRemind(
                "Humilin",
                1,
                true

            )
        )
        remindList.add(
            ProfileRemind(
                "Humilin",
                1,
                true

            )
        )
    }

    private fun loadNotes() {

        val loadNotesDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("notes")

        loadNotesDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (mdsnapshot in snapshot.children) {
                    savedNotesList.add(
                        Note(
                            mdsnapshot.key.toString(),
                            mdsnapshot.child("title").value.toString(),
                            mdsnapshot.child("body").value.toString())
                    )

                }
                // TODO: edit adapter to read savedNotes data
                notes_medication.adapter = NotesAdapter(savedNotesList)            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


    }
    private fun saveNote() {

        saveNotesDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("notes")

        //getting values
        val nTitle = notesTitle.text.toString()
        val nBody = notesBody.text.toString()

        if (notesTitle.text?.isEmpty() == true) {
            notesTitle.error = "Please enter name"

        }
        if (notesBody.text?.isEmpty() == true) {
            notesBody.error = "Please enter age"
        }
        else {
            val noteId = saveNotesDB.push().key!!

            val note = Note(noteId, nTitle, nBody)

            saveNotesDB.child(noteId).setValue(note)
                .addOnCompleteListener {
                    Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    notesTitle.text?.clear()
                    notesBody.text?.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            saveNote.visibility = View.GONE
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
        saveNote.visibility = View.GONE
    }


    private fun notesddata(){
        notesList.add(
            Notes(
                "Monjoro",
                1,
                true

            )
        )
        notesList.add(
            Notes(
                "Humilin",
                1,
                false

            )
        )
    }



    private fun loadSavedMedicines() {
        savedMedicineDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("saved_medicines")
        //val myRef = database.getReference("narrow_search")
        //Toast.makeText(context,"Data from firebase: $myRef",Toast.LENGTH_LONG).show()


        savedMedicineDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (mdsnapshot in snapshot.children) {
                    savedMedicineList.add(
                        SavedMedicine(
                            mdsnapshot.child("name").value.toString(),
                            mdsnapshot.child("id").value.toString(),
                            mdsnapshot.child("brandName").value.toString())
                        )

                }
                saved_medication.adapter = SavedAdapter(savedMedicineList,comm, savedMedicineDB)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}