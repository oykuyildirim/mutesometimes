package com.kotlinegitim.mutesometimes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kotlinegitim.mutesometimes.*
import com.kotlinegitim.mutesometimes.customadaptors.LocationCustomAdaptor
import com.kotlinegitim.mutesometimes.customadaptors.MuteClockCustomAdaptor
import com.kotlinegitim.mutesometimes.database.ClockDatabase
import com.kotlinegitim.mutesometimes.database.LocationDatabase
import com.kotlinegitim.mutesometimes.databinding.FragmentHomeBinding
import com.kotlinegitim.mutesometimes.models.Location
import com.kotlinegitim.mutesometimes.models.MuteClock

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    companion object{
        lateinit var clockList: ListView
        lateinit var locatedList: ListView
    }


    lateinit var emptyList : TextView
    lateinit var set : Button
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //var list = mutableListOf<MuteClock>()
   // var list2 = mutableListOf<Location>()

    lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        //_binding = FragmentHomeBinding.inflate(inflater, container, false)
        //val root: View = binding.root

        val root: View = layoutInflater.inflate(R.layout.fragment_home,null,true)

        clockList = root.findViewById(R.id.clocklist)
        locatedList = root.findViewById(R.id.locatedlist)
        emptyList = root.findViewById(R.id.EmptyList)


        val db = Room.databaseBuilder(
            requireActivity(),
            ClockDatabase::class.java,
            "AlarmDatabase"
        ).build()

        val db2 = Room.databaseBuilder(
            requireActivity(),
            LocationDatabase::class.java,
            "LocationDatabase"
        ).build()


        radioGroup = root.findViewById(R.id.listradio)



        loadTime(db)
        loadLocation(db2)


        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            val radio:RadioButton = group.findViewById(checkedId)

            if (radio.id == R.id.timelist){



                println("bu time")
                locatedList.visibility = View.INVISIBLE
                clockList.visibility=View.VISIBLE



            }
            else if (radio.id == R.id.locationlist){


                println("bu location")


                locatedList.visibility = View.VISIBLE
                clockList.visibility=View.INVISIBLE
            }

            println(radio.id.toString())

        }



        return root
    }

    fun checkNullList(list : List<Any>){

        if (list.isEmpty()){

            emptyList.visibility=View.VISIBLE

        }


        else{

            emptyList.visibility = View.INVISIBLE
        }


    }

    fun loadTime(db: ClockDatabase){

        val run = Runnable {

            var list = db.MuteClockDao().allClocks() as MutableList


            requireActivity().runOnUiThread{

                checkNullList(list)
                val adapter = MuteClockCustomAdaptor(requireActivity(),R.layout.list_clock_custom_layout,list)
                clockList.adapter = adapter
                adapter.notifyDataSetChanged()

            }


        }
        Thread(run).start()
        Thread(run).join()
    }

    fun loadLocation(db: LocationDatabase){

        val run = Runnable {

            var list2 = db.LocationDao().allLocation() as MutableList


            requireActivity().runOnUiThread{

                checkNullList(list2)
                val adapter = LocationCustomAdaptor(requireActivity(),R.layout.list_clock_custom_layout,list2)
                locatedList.adapter = adapter
                adapter.notifyDataSetChanged()

            }


        }
        Thread(run).start()
        Thread(run).join()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}