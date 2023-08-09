package com.kotlinegitim.mutesometimes.dbOperations

import android.app.Activity
import android.widget.ListView
import android.widget.Toast
import com.kotlinegitim.mutesometimes.customadaptors.MuteClockCustomAdaptor
import com.kotlinegitim.mutesometimes.R
import com.kotlinegitim.mutesometimes.database.ClockDatabase
import com.kotlinegitim.mutesometimes.models.MuteClock
import com.kotlinegitim.mutesometimes.services.TimeService

class DBOperationsTime {


    fun addToDatabase(db2: ClockDatabase, time : MuteClock, act: Activity){



        val run = Runnable {

            try {
                db2.MuteClockDao().Insert(time)
                TimeService.added = false
            }
            catch (e : Exception){

                act.runOnUiThread {

                    Toast.makeText(act,act.getString(R.string.sametime), Toast.LENGTH_SHORT).show()

                }

            }
        }
        Thread(run).start()


    }
    fun allClock(clockList: ListView, act:Activity, db : ClockDatabase) {




        val run = Runnable {

            var list = db.MuteClockDao().allClocks() as MutableList

            act.runOnUiThread {

                val adapter = MuteClockCustomAdaptor(act, R.layout.list_clock_custom_layout, list)
                clockList.adapter = adapter
                adapter.notifyDataSetChanged()

            }


        }
        Thread(run).start()
        Thread(run).join()
    }

    fun delete(mute: MuteClock, db : ClockDatabase){



        val run = Runnable {

            var alarm = MuteClock(mute.id,
                mute.clock,
                mute.title,
                mute.active
            )
            db.MuteClockDao().Delete(alarm)

            TimeService.added = false

        }

        Thread(run).start()
        Thread(run).join()


    }
    fun update(mute: MuteClock, active:Boolean, db : ClockDatabase){



        val run = Runnable {

            var alarm = MuteClock(mute.id,
                mute.clock,
                mute.title,
                active
            )
            db.MuteClockDao().Update(alarm)

            TimeService.added = false

        }

        Thread(run).start()
        Thread(run).join()


    }
}