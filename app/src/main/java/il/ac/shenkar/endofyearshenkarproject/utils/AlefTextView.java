package il.ac.shenkar.endofyearshenkarproject.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Text font for content
 */
public class AlefTextView extends TextView {

    public AlefTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public AlefTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public AlefTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Alef-Regular.ttf", context);
        setTypeface(customFont);
    }
}