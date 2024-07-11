package com.example.storeapp.utils.views

import com.example.storeapp.R
import com.example.storeapp.utils.extensions.moneySeparatingWithoutToman
import com.example.storeapp.utils.extensions.setTypefaceNormal
import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class CustomToolTip(context: Context, layout: Int, private val dateToDisplay: MutableList<String>) : MarkerView(context, layout) {

    private var dateTxt: TextView? = null
    private var countTxt: TextView? = null

    init {
        dateTxt = findViewById(R.id.dateTxt)
        countTxt = findViewById(R.id.countTxt)
    }

    override fun refreshContent(e: Entry, highlight: Highlight) {
        try {
            val index = e.x.toInt()
            dateTxt?.text = dateToDisplay[index]
            dateTxt?.typeface = setTypefaceNormal(context)

            countTxt?.text = highlight.y.toInt().moneySeparatingWithoutToman()
            countTxt?.typeface = setTypefaceNormal(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}