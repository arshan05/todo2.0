package com.example.projecttodo

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.HORIZONTAL
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment

class TimerFragment : Fragment() {
    var start = 1500000L
    var timer = start
    lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_timer, container, false)
//        val view2 =  layoutInflater.inflate(R.layout.time_picker, container, false)

        val playBtn = view.findViewById<ImageButton>(R.id.playBtn)
        val pauseBtn = view.findViewById<ImageButton>(R.id.pauseBtn)
        val stopBtn = view.findViewById<ImageButton>(R.id.stopBtn)

//        val minuteText = view2.findViewById<EditText>(R.id.temp1)
//        val secondText = view2.findViewById<EditText>(R.id.temp2)

//        minuteText.editText?.filters  = arrayOf(InputFilterMinMax(1, 100))
//        secondText.editText?.filters  = arrayOf(InputFilterMinMax(1, 60))
//
//        minuteText.filters = arrayOf(InputFilterMinMax(0,100))
//        secondText.filters = arrayOf(InputFilterMinMax(0,60))


        view.findViewById<TextView>(R.id.timerText).setOnLongClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            builder.setTitle("Set Timer")

            val linearLayout = LinearLayout(view.context)
            linearLayout.setOrientation(HORIZONTAL)
            linearLayout.gravity = Gravity.CENTER


            val min = EditText(view.context)
            min.inputType = InputType.TYPE_CLASS_NUMBER
            min.textSize = 36F
            min.filters = arrayOf(InputFilterMinMax(0,99))
            min.hint = "10"

            val col = TextView(view.context)
            col.text = " : "
            col.textSize = 36F

            val sec = EditText(view.context)
            sec.inputType = InputType.TYPE_CLASS_NUMBER
            sec.textSize = 36F
            sec.filters = arrayOf(InputFilterMinMax(0,59))
            sec.hint = "10"

            linearLayout.addView(min)
            linearLayout.addView(col)
            linearLayout.addView(sec)

            builder.setPositiveButton("ok"){ dialog, which ->
                val minIn = min.text
                val secIn = sec.text
                start = (Integer.parseInt(minIn.toString())*60*1000 + Integer.parseInt(secIn.toString())*1000).toLong()
                timer = start
                setTextTimer()
            }
                .setNegativeButton("cancel") { dialog, which ->
                    // Respond to negative button press
                    dialog.dismiss()
                }
            builder.setView(linearLayout)
            builder.show()

            return@setOnLongClickListener true
        }


        playBtn.setOnClickListener{
            pauseBtn.visibility = VISIBLE
            stopBtn.visibility = VISIBLE
            val right = ObjectAnimator.ofFloat(stopBtn,View.TRANSLATION_X,50f)
            right.duration = 100
            right.start()

            val left = ObjectAnimator.ofFloat(pauseBtn,View.TRANSLATION_X,-50f)
            left.duration = 100
            left.start()



//            var asyncTask = AsyncTimerTask()
//            asyncTask.execute("")

            countDownTimer = object : CountDownTimer(timer,1000){
                //            end of timer
                override fun onFinish() {
                    Toast.makeText(view.context,"completed",Toast.LENGTH_SHORT).show()
                }

                override fun onTick(millisUntilFinished: Long) {
                    timer = millisUntilFinished
                    setTextTimer()
                }

            }.start()


        }
        pauseBtn.setOnClickListener{
            countDownTimer.cancel()
        }

        stopBtn.setOnClickListener{

            val right = ObjectAnimator.ofFloat(pauseBtn,View.TRANSLATION_X,70f)
            right.duration = 200
            right.start()

            right.doOnEnd {
                pauseBtn.visibility = GONE
                playBtn.visibility = VISIBLE
            }

            val left = ObjectAnimator.ofFloat(stopBtn,View.TRANSLATION_X,-70f)
            left.duration = 200
            left.start()
            left.doOnEnd {
                stopBtn.visibility = GONE
            }

            countDownTimer.cancel()
            timer = start
            setTextTimer()



        }


        return view
    }



    private fun setTextTimer(){
        val minute = (timer / 1000) / 60
        val second = (timer / 1000) % 60

        val format = String.format("%02d:%02d", minute, second)

        view?.findViewById<TextView>(R.id.timerText)?.setText(format)
    }

//    inner class AsyncTimerTask: AsyncTask<String?,String?,Void?>(){
//        override fun onPreExecute() {
//            super.onPreExecute()
//        }
//
//        override fun doInBackground(vararg params: String?): Void? {
//            countDownTimer = object : CountDownTimer(timer,1000){
//                //            end of timer
//                override fun onFinish() {
////                    Toast.makeText(view?.context,"completed",Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onTick(millisUntilFinished: Long) {
//                    timer = millisUntilFinished
//                    setTextTimer()
//                }
//
//            }.start()
//            return null
//        }
//
//        override fun onPostExecute(result: Void?) {
//            super.onPostExecute(result)
//        }
//
//    }

}