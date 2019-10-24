package com.buntysinghediting.Cb_edit_stocks;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class FontAwesome extends android.support.v7.widget.AppCompatTextView {

    public FontAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesome(Context context) {
        super(context);
        init();
    }

    public void setText(final CharSequence text, final TextView.BufferType type) {
        super.setText(text, type);
        System.out.println("\n\n OUR TEXT HERE =====> "+ Html.toHtml(SpannableString.valueOf(text)));




    }

    public void isBrandIcon(Boolean b){
        if(b){

            //Font name should not contain "/".
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/fa_brands.ttf");
            setTypeface(tf);

        }

    }



    private void init() {

        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/fa_solid.ttf");
        setTypeface(tf);
    }
}
