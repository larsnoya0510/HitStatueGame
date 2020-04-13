package com.example.hitstatuegame

import android.content.Context
import android.widget.ImageView

class BlockViewSet(imageView: ImageView,level : Int,context :Context) {
    var life =  0
    var mImageView :ImageView
    var mContext :Context
    var mLevel=level
    init{
        mImageView=imageView
        mContext = context
        when(mLevel){
            0->{
                life=9999
            }
            1->{
                life=20
            }
            2 ->{
                life=40
            }
        }
    }
    fun checkDelete(){
        if(mLevel==1) {
            when {
                life <= 0 -> {

                    (mContext as MainActivity).runOnUiThread {
                        mImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.block_final))
                        (mContext as MainActivity).killBlocks(
                            this
                        )
                    }
                }
                life > 0 && life < 10 -> {
                    (mContext as MainActivity).runOnUiThread {
                        mImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.block_destory_notyet))
                    }
                }
            }
        }
        if(mLevel==2){
            when {
                life <= 0 -> {
                    (mContext as MainActivity).runOnUiThread {
                        mImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.boss1fixhit))
                        (mContext as MainActivity).killBlocks(
                            this
                        )
                    }
                }
                life > 0 && life < 10 -> {
                    (mContext as MainActivity).runOnUiThread {
                        mImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.boss1fixhit))
                    }
                }
            }
        }
    }
}