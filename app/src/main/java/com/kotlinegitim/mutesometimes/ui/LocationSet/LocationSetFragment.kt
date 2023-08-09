package com.kotlinegitim.mutesometimes.ui.LocationSet

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
import com.kotlinegitim.mutesometimes.database.LocationDatabase
import com.kotlinegitim.mutesometimes.databinding.FragmentLocationsetBinding
import com.kotlinegitim.mutesometimes.dbOperations.DBOperationsLoc
import com.kotlinegitim.mutesometimes.models.Location
import com.kotlinegitim.mutesometimes.services.LocationService


class LocationSetFragment : Fragment() {

    private var _binding: FragmentLocationsetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var radioGroup: RadioGroup

    lateinit var curRadio: RadioButton
    lateinit var manRadio : RadioButton
    lateinit var locBtn: Button
    lateinit var manuelBtn : Button
    lateinit var title: EditText
    lateinit var long : EditText
    lateinit var lat : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val TimeSetViewModel =
            ViewModelProvider(this).get(LocationSetViewModel::class.java)

        _binding = FragmentLocationsetBinding.inflate(inflater, container, false)
       // val root: View = binding.root
        val root : View = layoutInflater.inflate(R.layout.fragment_locationset,null,true)


        title = root.findViewById(R.id.Title)
        locBtn = root.findViewById(R.id.locationbtn)
        manuelBtn = root.findViewById(R.id.setLoc)
        lat = root.findViewById(R.id.latitude)
        long = root.findViewById(R.id.longtitude)
        curRadio = root.findViewById(R.id.automatic)
        manRadio = root.findViewById(R.id.manuel)


        val db2 = Room.databaseBuilder(
            requireContext(),
            LocationDatabase::class.java,
            "LocationDatabase"
        ).build()

        locBtn.setOnClickListener {


            var location = LocationService.lat_comp.toString()+","+ LocationService.long_comp.toString()


            ActivateAlert(location,db2)

        }

        currentLoctionUI()

        radioGroup = root.findViewById(R.id.Inputradio)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            val radio: RadioButton = group.findViewById(checkedId)

            if (radio.id == R.id.automatic) {

                currentLoctionUI()


            } else if (radio.id == R.id.manuel) {

                EnterManuellyUI()

            }
        }

        manuelBtn.setOnClickListener {


            var location =lat.text.toString()+","+long.text.toString()

            ActivateAlert(location,db2)




        }



        return root
    }

  fun ActivateAlert( location: String, db : LocationDatabase){

        val builder = AlertDialog.Builder(context)

        builder.setTitle(getString(R.string.active2))

        builder.setMessage(getString(R.string.active))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(getString(R.string.Yes)){dialogInterface, which ->

            var loc = Location(
                null,
                location,
                title.text.toString(),
                true
            )

            DBOperationsLoc().addToDatabase(db,loc,requireActivity())

        }


        builder.setNegativeButton(getString(R.string.No)){dialogInterface, which ->

            var loc = Location(
                null,
                location,
                title.text.toString(),
                false
            )

            DBOperationsLoc().addToDatabase(db,loc,requireActivity())

        }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun addToDatabase(db2: LocationDatabase, loc : Location){



            val run = Runnable {

                try {
                    db2.LocationDao().InsertLoc(loc)
                    LocationService.added_loc = false

                }
                catch ( e : Exception){


                    requireActivity().runOnUiThread {
                        println(e.message.toString())
                        Toast.makeText(requireActivity(),"Same location is avaiable on list",Toast.LENGTH_SHORT).show()
                    }


                }

            }
            Thread(run).start()



    }


    fun currentLoctionUI(){
        locBtn.visibility = View.VISIBLE
        title.visibility = View.VISIBLE
        long.visibility = View.INVISIBLE
        lat.visibility = View.INVISIBLE
        manuelBtn.visibility = View.INVISIBLE
    }

    fun EnterManuellyUI(){
        locBtn.visibility = View.INVISIBLE
        title.visibility = View.VISIBLE
        long.visibility = View.VISIBLE
        lat.visibility = View.VISIBLE
        manuelBtn.visibility = View.VISIBLE
    }

    override fun onResume() {
        locBtn.setText(getString(R.string.add_this_loc))
        manuelBtn.setText(getString(R.string.add_loc))
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

