package com.dtp.samplemvp.deal

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.dtp.samplemvp.R
import kotlinx.android.synthetic.main.activity_main.*

class DealActivity : AppCompatActivity(), DealView {

    private lateinit var dealPresenter: DealPresenter

    private var animatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dealPresenter = DealPresenter(this)

        val fromX = 0f
        val fromY = 0f
        val toX = 300f
        val toY = 300f

        val animation = ObjectAnimator.ofFloat(image_view, "scaleX", 1f, 4f)

        animation.duration = 2000

        val hightAnimation = ObjectAnimator.ofFloat(image_view, "scaleY", 1f, 4f)

        hightAnimation.duration = 2000

        val translateXAnimation = ObjectAnimator.ofFloat(image_view, "x", fromX, toX)
        translateXAnimation.duration = 2000

        val translateYAnimation = ObjectAnimator.ofFloat(image_view, "y", fromY, toY)
        translateYAnimation.duration = 2000

        animatorSet = AnimatorSet()

        animatorSet?.play(translateXAnimation)?.with(translateYAnimation)?.with(animation)?.with(hightAnimation)

        dealPresenter.load(null)

        click.setOnClickListener {
            dealPresenter.setNewText("Correct text")

            animatorSet?.start()
        }
    }

    override fun onPause() {
        super.onPause()

        if (isFinishing)
            dealPresenter.destroy()
    }

    override fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayNewText(text: String) {
        text_place.text = text
    }
}
