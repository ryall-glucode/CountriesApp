package com.example.countriesapp.extensions

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onSwipe(
    direction: Int = ItemTouchHelper.RIGHT,
    action: (viewHolder: RecyclerView.ViewHolder) -> Unit
) {
    val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, direction) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (isCurrentlyActive){
                drawButtons(c, viewHolder)
            }else{
                return
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            action(viewHolder)
        }
    }

    val swap = ItemTouchHelper(itemSwipe)
    swap.attachToRecyclerView(this)
}

fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
    val buttonWidth = 300f
    val buttonWidthWithoutPadding = buttonWidth - 20f
    val corners = 16f

    val itemView = viewHolder.itemView;
    val p = Paint()

    val leftButton = RectF(
        itemView.left.toFloat(),
        itemView.top.toFloat(), itemView.left + buttonWidthWithoutPadding,
        itemView.bottom.toFloat()
    )
    p.color = Color.RED
    c.drawRoundRect(leftButton, corners, corners, p)

    drawText("DELETE", c, leftButton, p)
}

fun drawText(text: String, c: Canvas, button: RectF, p: Paint) {
    val textSize = 60f;
    p.setColor(Color.WHITE);
    p.setAntiAlias(true);
    p.setTextSize(textSize);

    val textWidth = p.measureText(text);
    c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
}

