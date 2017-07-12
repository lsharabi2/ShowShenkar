package il.ac.shenkar.endofyearshenkarproject.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BoldAlefTextView extends TextView {

    public BoldAlefTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public BoldAlefTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public BoldAlefTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Alef-Bold.ttf", context);
        setTypeface(customFont);
    }
}