package Utilities;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

/**
 * Created by Adnan on 11/12/2017.
 */

public class TextColorBuilder {

    public SpannableStringBuilder comaFormattedData(String data)
    {
        String formatted="►";
        String[] splitData=data.split("।");
        int size=splitData.length;

        for (int i=0;i<size;i++)
        {
            formatted+=splitData[i].trim();
            if(i<size-1)
            {
                formatted+="।\n►";
            }
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String[] splitNumber=formatted.split("#");
        size=splitNumber.length;

        for (int i=0;i<size;i++)
        {
            if(i%2>0)
            {
                String red = splitNumber[i];
                SpannableString redSpannable= new SpannableString(red);
                redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#f16e52")), 0, red.length(), 0);
                builder.append(redSpannable);
            }
            else
            {
                String black = splitNumber[i];
                SpannableString whiteSpannable= new SpannableString(black);
                whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#585656")), 0, black.length(), 0);
                builder.append(whiteSpannable);
            }
        }

        builder.append("।");
        return builder ;
    }

    public SpannableStringBuilder formattedData(String data)
    {


        SpannableStringBuilder builder = new SpannableStringBuilder();
        String[] splitNumber=data.split("#");
        int size=splitNumber.length;

        for (int i=0;i<size;i++)
        {
            if(i%2>0)
            {
                String red = splitNumber[i];
                SpannableString redSpannable= new SpannableString(red);
                redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#f16e52")), 0, red.length(), 0);
                builder.append(redSpannable);
            }
            else
            {
                String black = splitNumber[i];
                SpannableString whiteSpannable= new SpannableString(black);
                whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#585656")), 0, black.length(), 0);
                builder.append(whiteSpannable);
            }
        }

        return builder;
    }

    public SpannableStringBuilder merchantFormattedData(String data)
    {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String[] splitNumber=data.split("#(\\$)");
        int size=splitNumber.length;
        String merchant="";
        for (int i=0;i<size;i++)
        {
            if(i%2==0)
            {
                if(splitNumber[i].length()>1)
                {
                    Log.d("data", "formattedData: "+splitNumber[i]);
                    String[] black = splitNumber[i].split("\\$(\\#)");
                    Log.d("data", "formattedData: "+black.length);
                    for (int j=0;j<size;j++)
                    {
                        if(j%2>0)
                        {
                            SpannableStringBuilder str = new SpannableStringBuilder(black[j]);
                            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            builder.append(str);
                        }
                        else
                        {
                            SpannableStringBuilder str = new SpannableStringBuilder(black[j]);
                            builder.append(str);
                        }
                    }
                }
            }
        }

        return builder;
    }
}
