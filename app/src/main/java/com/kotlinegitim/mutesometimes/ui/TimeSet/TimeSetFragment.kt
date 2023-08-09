package com.kotlinegitim.mutesometimes.ui.TimeSet

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kotlinegitim.mutesometimes.*
import com.kotlinegitim.mutesometimes.database.ClockDatabase
import com.kotlinegitim.mutesometimes.databinding.FragmentTimesetBinding
import com.kotlinegitim.mutesometimes.dbOperations.DBOperationsTime
import com.kotlinegitim.mutesometimes.models.MuteClock
import com.kotlinegitim.mutesometimes.services.TimeService
import java.util.*

class TimeSetFragment : Fragment() {

    private var _binding: FragmentTimesetBinding? = null

    lateinit var timePicker: TimePicker
    lateinit var button : Button
    lateinit var titleText : EditText

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object{
        var muteList = mutableListOf<MuteClock>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val TimeSetViewModel =
            ViewModelProvider(this).get(TimeSetViewModel::class.java)

        //_binding = FragmentDashboardBinding.inflate(inflater, container, false)
        //val root: View = binding.root

        val root: View = layoutInflater.inflate(R.layout.fragment_timeset,null,true)
        timePicker = root.findViewById(R.id.timePicker)
        button = root.findViewById(R.id.addClock)
        titleText = root.findViewById(R.id.muteName)



        val db = Room.databaseBuilder(
            requireActivity(),
            ClockDatabase::class.java,
            "AlarmDatabase"
        ).build()


        button.setOnClickListener{

            var hour : String =""
            var minute : String =""
            val clock :String


            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
            calendar.set(Calendar.MINUTE, timePicker.currentMinute)


            if(calendar.time.hours < 10){
                hour = "0"+calendar.time.hours
            }
            else{
                hour = calendar.time.hours.toString()
            }


            if (calendar.time.minutes<10){
                minute = "0"+calendar.time.minutes
            }
            else{
                minute = calendar.time.minutes.toString()
            }

            clock = hour+":"+minute+":"+"00"
            ActivateAlert(clock,db)

        }




        return root
    }

    fun ActivateAlert( clock: String, db : ClockDatabase){

        val builder = AlertDialog.Builder(context)

        builder.setTitle(getString(R.string.active2))

        builder.setMessage(getString(R.string.active))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(getString(R.string.Yes)){dialogInterface, which ->

            var loc = MuteClock(
                null,
                clock,
                titleText.text.toString(),
                true
            )

            DBOperationsTime().addToDatabase(db,loc,requireActivity())

        }


        builder.setNegativeButton(getString(R.string.No)){dialogInterface, which ->

            var loc = MuteClock(
                null,
                clock,
                titleText.text.toString(),
                false
            )

            DBOperationsTime().addToDatabase(db,loc,requireActivity())

        }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun addToDatabase(db2: ClockDatabase, time : MuteClock){



            val run = Runnable {

                try {
                    db2.MuteClockDao().Insert(time)
                    TimeService.added = false
                }
                catch (e : Exception){

                    requireActivity().runOnUiThread {

                        Toast.makeText(requireActivity(),"Same clock is avaiable on list",Toast.LENGTH_SHORT).show()

                    }

                }
            }
            Thread(run).start()


    }

    override fun onResume() {

        button.setText(getString(R.string.add_time_button))
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}