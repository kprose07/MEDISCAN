package com.example.mediscan.Fragments


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


class SavedFragment : Fragment(), ProfileRemindAdapter.OnItemClickedListener,
    NotesAdapter.OnItemClickedListener {
    val remindList = ArrayList<ProfileRemind>()
    val savedList = ArrayList<SavedMedicine>()
    val notesList = ArrayList<Notes>()
    var item:String = ""


    private var TAG = "Saved Fragment"
    private lateinit var savedMedicineDB: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var comm:Communicator
    private lateinit var specialNotes: LinearLayout
    private lateinit var saveNote: Button
    private lateinit var notesTitle: TextInputEditText
    private lateinit var notesBody: TextInputEditText
    private lateinit var dbRef: DatabaseReference

    private val savedMedicineList = mutableListOf<SavedMedicine>()
    private lateinit var addNote: ImageView
    private lateinit var saveNotesDB: DatabaseReference


    val savedNotesList = ArrayList<Note>()
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




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //createNotificationChannel()
        val saverecycle: RecyclerView = view.findViewById(R.id.saved_medication)
        if (savedMedicineList.isEmpty())  loadSavedMedicines(view)
        if (savedNotesList.isEmpty()) loadNotes(view)
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
        profile_reminder.adapter = ProfileRemindAdapter(remindList,this)

        //Saved Medicine

        saverecycle.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        saverecycle.adapter = SavedAdapter(savedMedicineList, comm, savedMedicineDB)
        //Notes
        notes_medication.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        notes_medication.adapter = NotesAdapter(savedNotesList,this)

        //static data
        if (remindList.isEmpty()) reminddata()
//        if (notesList.isEmpty()) notesddata()
//        if (savedNotesList.isEmpty()) loadNotes()


        //createNotificationChannel()
        createchannelnotify()
        ser_remind_button.setOnClickListener {
            //select time
            showTimePicker()
        }

        cancle_remind.setOnClickListener {
            //cancle alarm
            //cancleAlarm()

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

        showAlert()

    }

    private fun showAlert() {

        AlertDialog.Builder(requireActivity())
            .setTitle("Reminder Creted for: ${item}")
            .setMessage(
                "This will remind you evryday at the selected time."
            )
            .setPositiveButton("Okay"){_,_ ->}
            .show()
        //Toast.makeText(context,"Reminder Created",Toast.LENGTH_SHORT).show()
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

    private fun loadNotes(view: View) {

        val loadNotesDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("notes")
        val emptynote: TextView = view.findViewById(R.id.emptynotes)
        ///emptynote.visibility = View.GONE

        loadNotesDB.addValueEventListener(object : ValueEventListener,
            NotesAdapter.OnItemClickedListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                savedNotesList.clear()
                if(snapshot.hasChildren()){
                    emptynote.visibility = View.GONE

                }else{
                    emptynote.visibility = View.VISIBLE
                }
                for (mdsnapshot in snapshot.children) {
                    savedNotesList.add(
                        Note(
                            mdsnapshot.key.toString(),
                            mdsnapshot.child("title").value.toString(),
                            mdsnapshot.child("body").value.toString())
                    )

                }
                // TODO: edit adapter to read savedNotes data
                notes_medication.adapter = NotesAdapter(savedNotesList,this)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }

            fun deletNotes(noteId: String, nTitle: String, nBody: String) {

                saveNotesDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("notes")
                val note = Note(noteId, nTitle, nBody)
                saveNotesDB.child(note.id).removeValue().addOnSuccessListener {
                    Toast.makeText(context,"Deleted Note", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(context, "NOT DELETED", Toast.LENGTH_SHORT).show()

                }

            }

            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                deletNotes(saveNotesDB.push().key!!.toString(),notesTitle.text.toString(),notesBody.text.toString())
            }
            override fun onCLickNotes(position: Int) {
                Toast.makeText(context, "CLICK", Toast.LENGTH_LONG).show()
                AlertDialog.Builder(requireActivity())
                    .setTitle(savedNotesList[position].title)
                    .setMessage(savedNotesList[position].body)
                    .setPositiveButton("Okay") { _, _ -> }
                    //.setNegativeButton("Delete", negativeButtonClick)
                    .show()
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




    private fun loadSavedMedicines(v: View) {
        val saverecycle: RecyclerView = v.findViewById(R.id.saved_medication)
        val emptysave: TextView = v.findViewById(R.id.saveMed_seal)
        savedMedicineDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("saved_medicines")
        val savedMedicineDBTEXT = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!)

        //val myRef = database.getReference("narrow_search")
        //Toast.makeText(context,"Data from firebase: $myRef",Toast.LENGTH_LONG).show()

       // saveMed_seal.visibility = View.GONE
        savedMedicineDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                savedMedicineList.clear()
                if(snapshot.hasChildren()){
                    emptysave.visibility = View.GONE

                }else{
                    emptysave.visibility = View.VISIBLE
                }
                for (mdsnapshot in snapshot.children) {
                    savedMedicineList.add(
                        SavedMedicine(
                            mdsnapshot.child("name").value.toString(),
                            mdsnapshot.child("id").value.toString(),
                            mdsnapshot.child("brandName").value.toString(),
                            mdsnapshot.child("pdfLink").value.toString()
                        )
                    )

                }

                saverecycle.adapter = SavedAdapter(savedMedicineList,comm, savedMedicineDB)
                //saved_medication!!.setAdapter(SavedAdapter(savedMedicineList,comm, savedMedicineDB))

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
    override fun onCLick(position: Int) {
        //Toast.makeText(context,"Reminder Created${position}",Toast.LENGTH_SHORT).show()
        remind_popupcard?.visibility = View.VISIBLE

        remind_close.setOnClickListener {
            remind_popupcard?.visibility = View.GONE
            Toast.makeText(context,"Reminder Cancled",Toast.LENGTH_SHORT).show()
        }
        add_remind_button.setOnClickListener {


            if(item != "Select Medicine") {
                remindList[position].isprEmpty = false
                remindList[position].title = item
                scheduleNotify()
                profile_reminder.adapter?.notifyItemChanged(position)


            }else {
                remindList[position].isprEmpty = true
                remindList[position].title = "Empty"
                Toast.makeText(context,"Alarm not set${position}",Toast.LENGTH_SHORT).show()
                profile_reminder.adapter?.notifyItemChanged(position)
            }
            remind_popupcard?.visibility = View.GONE
            spinner.setSelection(0)
            description_remind_input.getText()?.clear()
        }

    }

    override fun onCLickNotes(position: Int) {
        Toast.makeText(context, "CLICK", Toast.LENGTH_LONG).show()
        Toast.makeText(context, "CLICK", Toast.LENGTH_LONG).show()
        AlertDialog.Builder(requireActivity())
            .setTitle(savedNotesList[position].title)
            .setMessage(savedNotesList[position].body)
            .setPositiveButton("Okay") { _, _ -> }
            //.setNegativeButton("Delete", negativeButtonClick)
            .show()

    }



}