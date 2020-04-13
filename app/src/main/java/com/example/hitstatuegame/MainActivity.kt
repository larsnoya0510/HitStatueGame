package com.example.hitstatuegame

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var ballList: MutableList<Ball>
    lateinit var ballHandler : Handler
    var mThread: HandlerThread = HandlerThread("BallThread")
    lateinit var viewList:MutableList<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewList = mutableListOf<View>()
        viewList.add(bossImageView)
        viewList.add(weapnImageView)
        mThread.start()
        ballHandler = Handler(mThread.getLooper())
        ballList = mutableListOf<Ball>()


    }

    override fun onResume() {
        super.onResume()
        Thread{
            Thread.sleep(3000)
            runOnUiThread { createBall() }
        }.start()
    }

    private fun createBall() {
        var mBall = Ball(this@MainActivity, ballHandler, viewList)
        ballList.add(mBall)
        for (i in 0 until ballList.size) {
            ballList[i].start()
        }
    }

    fun killBalls(mBall: Ball) {
        var index = ballList.indexOfFirst { it == mBall }
        var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
        relative!!.removeView(ballList[index].imageView)
        ballList.remove(mBall)
    }
    companion object {
        @kotlin.jvm.JvmField
        var MOVE_IMAGE: Int = 1
    }
}
