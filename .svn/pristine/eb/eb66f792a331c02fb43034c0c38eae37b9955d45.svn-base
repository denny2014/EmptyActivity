package com.zet.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coolsharp WordWrap
 */
public class EnhanceWordWrapTextView extends TextView {
    private int width = 0;

    private int height = 0;

    private Paint paint = null;

    private List<String> lineString = new ArrayList<String>();

    private int intMaxLines = Integer.MAX_VALUE;

    private boolean istrue;

    private int trueWidth;

    public boolean getIsture() {
        return this.istrue;
    }

    public void setIsture(boolean istrue) {
        this.istrue = istrue;
    }

    public int getTrueWidth() {
        return trueWidth;
    }

    public void setTrueWidth(int trueWidth) {
        this.trueWidth = trueWidth;
    }

    /**
     * @param context
     */
    public EnhanceWordWrapTextView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public EnhanceWordWrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int setTextValue(String text, int textWidth, int textHeight) {
        int hValue = textHeight;

        if (textWidth > 0) {
            lineString.clear();
            if (istrue) {
                width = textWidth - this.getPaddingLeft() - this.getPaddingRight() - trueWidth;
            } else {
                width = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            }

            paint = getPaint();
            paint.setColor(getTextColors().getDefaultColor());
            paint.setTextSize(getTextSize());

            int count = 1;
            int end = 0;
            String[] arrText = text.split("\n");
            for (int i = 0; i < arrText.length; i++) {

                if (arrText[i].length() == 0)
                    arrText[i] = " ";
                do {
                    end = paint.breakText(arrText[i], true, width, null);
                    if (end > 0) {
                        lineString.add(arrText[i].substring(0, end));
                        arrText[i] = arrText[i].substring(end);
                        if (textHeight == 0)
                            hValue += getLineHeight();
                    }
                    count++;
                    if (intMaxLines < count)
                        break;
                } while (end > 0);
            }
        }
        hValue += getPaddingTop() + getPaddingBottom() + 10;
        height = hValue;
        return hValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //         int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //         int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //
        //         int width = widthMeasureSpec;
        //         int height = heightMeasureSpec;
        //
        //         switch (widthMode) {
        //         case MeasureSpec.UNSPECIFIED: // unspecified
        //         	break;
        //         case MeasureSpec.AT_MOST: // wrap_content
        //        	 break;
        //         case MeasureSpec.EXACTLY: // match_parent
        //        	 width = MeasureSpec.getSize(widthMeasureSpec);
        //        	 break;
        //         }
        //
        //         switch (heightMode) {
        //         case MeasureSpec.UNSPECIFIED: // unspecified
        //         	break;
        //         case MeasureSpec.AT_MOST: // wrap_content
        //        	height = setTextValue(this.getText().toString(), width,
        //        	this.getHeight());
        //        	break;
        //         case MeasureSpec.EXACTLY: // match_parent
        //        	 height = MeasureSpec.getSize(heightMeasureSpec);
        //        	 break;
        //         }
        //
        //         setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float height = (float) (getPaddingTop() + getLineHeight() * 0.7);
        int i = 0;
        for (String text : lineString) {
            i++;
            canvas.drawText(text, getPaddingLeft(), height, paint);

            height += getLineHeight();
        }

    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        setTextValue(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            setTextValue(this.getText().toString(), w, h);
        } else {
            super.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        intMaxLines = maxlines;
    }
}
