package com.example.hitstatuegame

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlin.math.absoluteValue
import kotlin.random.Random

class Ball(var context: Context, var ballHandler: Handler,var blockViewList : MutableList<BlockViewSet>,var initPotion : MutableList<Float>) {
    var sucideCount = 20
    var handler: Handler? = null
    var MOVE_IMAGE = 1
    //初始位置
    var orginX = Random.nextInt(0, 50)
    var orginY = Random.nextInt(0, 50)
    // 移動方向和距離
    var decX = Random.nextInt(5, 10)
    var decY = -Random.nextInt(5, 10)
    // 座標
    var moveX: Int = 200
    var moveY: Int = 300
    var isMove: Boolean = false// 是否正在移動
    var relative: RelativeLayout? = null
    var imageView: ImageView? = null
    lateinit var ballRunnable: Runnable
    var moveAdjustvalue =3
    init {
        moveX = initPotion[0].toInt()
        moveY = initPotion[1].toInt()-200
        imageView = ImageView(context!!)
        imageView!!.setImageDrawable(context.resources.getDrawable(R.drawable.fireballverysmall))
        imageView!!.scaleType = ImageView.ScaleType.FIT_XY
        var mLayoutParams = RelativeLayout.LayoutParams(
            30, 30
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
                var iterator = blockViewList.iterator();
                while(iterator.hasNext()){
                    var integer = iterator.next();
                    ItemEdgeDetect(imageView!!,integer)
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
            var getWidth=imageView!!.getWidth()
            var getHeight = imageView!!.getHeight()
            var relativegetHeight = relative!!.getHeight()
            if ((moveX + imageView!!.getWidth()) >= relative!!.getWidth() || moveX < 0) {
                decX = -decX;
            }
            if ( moveY < 0) {
                decY = -decY
            }
            if((moveY + imageView!!.getHeight()) >= relative!!.getHeight()){
                sucide()
            }
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
    fun ItemEdgeDetect(mView: View, BeHitObject : BlockViewSet) {
            var mBeHitObject = BeHitObject.mImageView
            if(mView.bottom <=mBeHitObject.bottom+mView.height && mView.top>=  mBeHitObject.top-mView.height){
//              左右
                if(mView.left >= mBeHitObject.right -decX.absoluteValue   && mView.left<=mBeHitObject.right +decX.absoluteValue){
                    decX = -decX;
                    moveX +=decX.absoluteValue*moveAdjustvalue
                    BeHitObject.life-=1
                    BeHitObject.checkDelete()
                    println(" BeHitObject.life  ${ BeHitObject.life}")
                }
                if (mView.right>=mBeHitObject.left -decX.absoluteValue && mView.right<=mBeHitObject.left +decX.absoluteValue) {
                    decX = -decX;
                    moveX-=decX.absoluteValue*moveAdjustvalue
                    BeHitObject.life-=1
                    BeHitObject.checkDelete()
                    println(" BeHitObject.life  ${ BeHitObject.life}")
                }
            }
            if(mView.left >= mBeHitObject.left - mView.width && mView.right <=mBeHitObject.right + mView.width){
//                上下
                if(mView.top >= mBeHitObject.bottom -decY.absoluteValue   && mView.top<=mBeHitObject.bottom +decY.absoluteValue){
                    decY = -decY;
                    moveY +=decY.absoluteValue*moveAdjustvalue
                    BeHitObject.life-=1
                    BeHitObject.checkDelete()
                    println(" BeHitObject.life  ${ BeHitObject.life}")
                }
                if (mView.bottom>=mBeHitObject.top -decY.absoluteValue && mView.bottom<=mBeHitObject.top +decY.absoluteValue) {
                    decY = -decY;
                    moveY-=decY.absoluteValue*moveAdjustvalue
                    BeHitObject.life-=1
                    BeHitObject.checkDelete()
                    println(" BeHitObject.life  ${ BeHitObject.life}")
                }
            }
    }
}