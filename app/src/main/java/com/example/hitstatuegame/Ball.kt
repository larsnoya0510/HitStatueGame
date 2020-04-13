package com.example.hitstatuegame

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.math.absoluteValue
import kotlin.random.Random

class Ball(var context: Context, var ballHandler: Handler,var blockViewList : MutableList<View>) {
    var sucideCount = 20
    var handler: Handler? = null
    var MOVE_IMAGE = 1
    //初始位置
    var orginX = Random.nextInt(0, 50)
    var orginY = Random.nextInt(0, 50)
    // 移動方向和距離
    var decX = Random.nextInt(5, 10)
    var decY = Random.nextInt(5, 10)
    // 座標
    var moveX: Int = 200
    var moveY: Int = 300
    var isMove: Boolean = false// 是否正在移動
    var isScideble: Boolean = false
    var relative: RelativeLayout? = null
    var imageView: ImageView? = null
    lateinit var ballRunnable: Runnable
    var boundRate: Float = 0F

    init {
//        moveX = orginX
//        moveY = orginY
        imageView = ImageView(context!!)
        imageView!!.setImageDrawable(context.resources.getDrawable(R.drawable.fireballsmall))
        imageView!!.scaleType = ImageView.ScaleType.FIT_XY
        var mLayoutParams = RelativeLayout.LayoutParams(
            50, 50
        )
        mLayoutParams.setMargins(orginX, 10, 0, orginY)
        imageView!!.layoutParams = mLayoutParams
        relative = (context as MainActivity).findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
        relative!!.addView(imageView)

        handler = MyHandler(context as MainActivity, imageView)
        this.ballRunnable = Runnable {
            if (this.isMove) {
                moveX += decX;
                moveY += decY;
                EdgeDetect()
                blockViewList.forEach {
                    ItemEdgeDetect(imageView!!, it)
                }
                if (sucideCount <= 0) {
                    sucide()
                }
                var message = Message();
                message.what = MOVE_IMAGE;
                (this.context as MainActivity).runOnUiThread {
                    val lp = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    // 利用Margin改變小球的位置
                    lp.setMargins(
                        moveX, moveY, 0, 0
                    )
                    this@Ball.imageView!!.setLayoutParams(lp)
                }
                try {
                    ballHandler.removeCallbacks(ballRunnable)
                    ballHandler.postDelayed(this@Ball.ballRunnable, 20)
                } catch (e: InterruptedException) {
                    e.printStackTrace();
                }
            }
        }
    }

    private fun EdgeDetect() {

//        imageView!!.post{
            var getWidth=imageView!!.getWidth()
            var getHeight = imageView!!.getHeight()
            var relativegetHeight = relative!!.getHeight()
            if ((moveX + imageView!!.getWidth()) >= relative!!.getWidth() || moveX < 0) {
                decX = -decX;
            }
//        if ( moveY < 0 || (moveY + imageView!!.getHeight()) >= relative!!.getHeight()) {
//            decY = -decY
//        }
            if ( moveY < 0) {
                decY = -decY
            }
            if((moveY + imageView!!.getHeight()) >= relative!!.getHeight()){
                sucide()
            }
//        }

    }


    fun start() {
        this.isMove = false;
        if (!this.isMove) {
            this.isMove = true;
        } else {
            return;
        }
        if (!ballHandler.hasCallbacks(ballRunnable)) {
            ballHandler.post(ballRunnable)
        }
    }

    fun stop() {
        this.isMove = false
        ballHandler.removeCallbacks(ballRunnable)
    }

    fun sucide() {
        this.stop()
        (this.context as MainActivity).runOnUiThread {
            (this.context as MainActivity).killBalls(this)
        }
    }
    fun ItemEdgeDetect(mView: View, BeHitObject : View) {
            var mBeHitObject = BeHitObject
            if(mView.bottom <=mBeHitObject.bottom+mView.height && mView.top>=  mBeHitObject.top-mView.height){
                var Aleft = mView.left
                var Aright = mView.right
                var Bleft = mBeHitObject.left
                var Bright = mBeHitObject.right
                var Unit = decX.absoluteValue
//              左右
                if(mView.left >= mBeHitObject.right -decX.absoluteValue   && mView.left<=mBeHitObject.right +decX.absoluteValue){
                    decX = -decX;
                    moveX +=decX.absoluteValue*5
                }
                if (mView.right>=mBeHitObject.left -decX.absoluteValue && mView.right<=mBeHitObject.left +decX.absoluteValue) {
                    decX = -decX;
                    moveX-=decX.absoluteValue*5
                }

            }
            if(mView.left >= mBeHitObject.left - mView.width && mView.right <=mBeHitObject.right + mView.width){
//                上下
                if(mView.top >= mBeHitObject.bottom -decY.absoluteValue   && mView.top<=mBeHitObject.bottom +decY.absoluteValue){
                    decY = -decY;
                    moveY +=decY.absoluteValue*5
                }
                if (mView.bottom>=mBeHitObject.top -decY.absoluteValue && mView.bottom<=mBeHitObject.top +decY.absoluteValue) {
                    decY = -decY;
                    moveY-=decY.absoluteValue*5
                }
            }
    }
}