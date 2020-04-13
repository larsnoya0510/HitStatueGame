package com.example.hitstatuegame

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var lastX: Int = 0
    var lastY: Int = 0    //保存手指点下的点的坐标
    lateinit var ballList: MutableList<Ball>
    lateinit var ballHandler : Handler
    var mThread: HandlerThread = HandlerThread("BallThread")
    lateinit var viewList:MutableList<BlockViewSet>
    var BossFightFlag=false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewList = mutableListOf<BlockViewSet>()
        mThread.start()
        ballHandler = Handler(mThread.getLooper())
        ballList = mutableListOf<Ball>()
        multiBallImageView.setOnClickListener { multiBall() }
        weapnImageView!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX.toInt()
//                        lastY = event.rawY.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
                        //计算出需要移动的距离
                        var dx = event.rawX - lastX;
                        var dy = event.rawY - lastY;
                        //将移动距离加上，现在本身距离边框的位置
                        var left = view.getLeft() + dx;
//                        var top = view.getTop() + dy;
                        var right = view.getRight()-dx
                        if(left>=relative.width-view.width) {
                            left=(relative.width-view.width).toFloat()
                        }
                        if(left<0){
                            left =0F
                        }

                            //获取到layoutParams然后改变属性，在设置回去
                            var layoutParams = view.getLayoutParams() as RelativeLayout.LayoutParams
//                        layoutParams.height = 500
//                        layoutParams.width = 2000
                            layoutParams.leftMargin = left.toInt();
//                            layoutParams.topMargin = top.toInt();
                            view.setLayoutParams(layoutParams);
                            //记录最后一次移动的位置
                            lastX = event.rawX.toInt()
//                            lastY = event.rawY.toInt()
                        }
                    }

                //刷新界面
                view.invalidate()
                return true
            }
        })
    }
    fun multiBall(){
        if(ballList.size>0){
            for(i in 0 .. 1) {
                var getX = ballList[0].moveX
                var getY = ballList[0].moveY
                var poitionList = mutableListOf<Float>()
                poitionList.add(getX.toFloat())
                poitionList.add(getY.toFloat())
                var mBall = Ball(this@MainActivity, ballHandler, viewList, poitionList)
                ballList.add(mBall)
            }
        }
        else{
            for(i in 0 .. 2) {
                var getX=weapnImageView.x
                var getY=weapnImageView.y
                var poitionList= mutableListOf<Float>()
                poitionList.add(getX)
                poitionList.add(getY)
                var mBall = Ball(this@MainActivity, ballHandler, viewList,poitionList)
                ballList.add(mBall)
            }
        }
        for (i in 0 until ballList.size) {
            ballList[i].start()
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        var mWeaponSet = BlockViewSet(weapnImageView,0,this)
        viewList.add(mWeaponSet)
        var mBlockSet = BlockViewSet(testBlockImageView,1,this)
        var orginX=20
        var orginY=20
        for(i in 0 .. 2){
            for(j in 0 ..1) {
                var imageView = ImageView(this!!)
                imageView!!.setImageDrawable(this.resources.getDrawable(R.drawable.block))
                var mLayoutParams = RelativeLayout.LayoutParams(
                    180, 100
                )
                mLayoutParams.setMargins(i * 200 + 50, j * 100 + (300-j*200), 0, 0)
                imageView!!.layoutParams = mLayoutParams
                var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
                relative!!.addView(imageView)
                var mBlockSet = BlockViewSet(imageView, 1, this)
                viewList.add(mBlockSet)
            }
        }

//        viewList.add(mBlockSet)
//        var mBlockSet2 = BlockViewSet(testBlockImageView2,1,this)
//        viewList.add(mBlockSet2)
//        var mBlockSet3 = BlockViewSet(testBlockImageView3,1,this)
//        viewList.add(mBlockSet3)
        Thread{
            Thread.sleep(3000)
            runOnUiThread { createBall(viewList) }
        }.start()
    }

    private fun createBall(viewList :MutableList<BlockViewSet>) {
        var getX=weapnImageView.x
        var getY=weapnImageView.y
        var poitionList= mutableListOf<Float>()
        poitionList.add(getX)
        poitionList.add(getY)


        var mBall = Ball(this@MainActivity, ballHandler, viewList,poitionList)
        ballList.add(mBall)
        for (i in 0 until ballList.size) {
            ballList[i].start()
        }
    }

    fun killBalls(mBall: Ball) {
        var iterator = ballList.iterator();
        while(iterator.hasNext()){
            var integer = iterator.next();
            if(integer==mBall) {
                var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
                relative!!.removeView(integer.imageView)
                iterator.remove()
            }
        }

//        ballList.forEach {
//            if(it==mBall){
//                var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
//                relative!!.removeView(it.imageView)
//                ballList.remove(it)
//            }
//        }

//        var index = ballList.indexOfFirst { it == mBall }
//        var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
//        relative!!.removeView(ballList[index].imageView)
//        ballList.remove(mBall)
    }
    fun killBlocks(mBlockViewSet: BlockViewSet){
        var iterator = viewList.iterator();
        while(iterator.hasNext()){
            var integer = iterator.next();
            if(integer==mBlockViewSet) {
                var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
                relative!!.removeView(integer.mImageView)
                iterator.remove()
            }
        }

//        viewList.forEach {
//            if(it==mBlockViewSet){
//                var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
//                relative!!.removeView(it.mImageView)
//                viewList.remove(it)
//            }
//        }

//        var index = viewList.indexOfFirst { it == mBlockViewSet }
//        var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
//        if(index!=-1) {
//            relative!!.removeView(viewList[index].mImageView)
//            viewList.remove(mBlockViewSet)
//        }
        if(viewList.size<=1 && BossFightFlag==false){
            BossFightFlag=true
            bossImageView.visibility = View.VISIBLE
//            var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
//            relative!!.addView(bossImageView)
            var mBlockSet = BlockViewSet(bossImageView,2,this)
            viewList.add(mBlockSet)
        }
        else if(viewList.size<=1 && BossFightFlag==true){
            nextStageImageView.visibility =View.VISIBLE
        }
    }
    companion object {
        @kotlin.jvm.JvmField
        var MOVE_IMAGE: Int = 1
    }
}
