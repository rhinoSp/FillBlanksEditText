package com.rhino.fillblanksedittext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoLin
 * @since Create on 2018/8/17.
 */
public class FillBlanksEditText extends AppCompatEditText implements TextWatcher, View.OnKeyListener {

    private final static String TAG = FillBlanksEditText.class.getSimpleName();
    private final static String BLANKS_FORMAT_START = "#_blanks:";
    private final static String BLANKS_FORMAT_END = "_#";
    private final static int BLANKS_FORMAT_DEFAULT_COUNT = 10;

    private List<BlanksRange> mBlanksRange = new ArrayList<>();


    public FillBlanksEditText(Context context) {
        super(context);
        init();

    }

    public FillBlanksEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FillBlanksEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

//        Log.d(TAG, "selStart "+selStart+" selEnd "+selEnd);
        int length = getText().toString().length();
        if (selStart == selEnd) {
            if (!isSupportEdit()) {
                clearFocus();
            } else {
                requestFocus();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBottomLine(canvas);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "s = " + s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "s = " + s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "s = " + s.toString());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }
        return false;
    }

    private void init() {
        addTextChangedListener(this);
        setOnKeyListener(this);
    }

    private void drawBottomLine(Canvas canvas) {
        canvas.save();

//        Log.d(TAG, "drawBottomLine");


        canvas.restore();
    }


    public void initFillBlanksText(String text) {
        StringBuilder newText = new StringBuilder();
        while (true) {
            int start = text.indexOf(BLANKS_FORMAT_START);
            if (start < 0) {
                newText.append(text);
                break;
            }
            int end = text.indexOf(BLANKS_FORMAT_END);
            if (end < 0) {
                throw new RuntimeException("start with \"" + BLANKS_FORMAT_START + "\"," +
                        " must end with \"" + BLANKS_FORMAT_END + "\"");
            }
            int count = -1;
            try {
                count = Integer.valueOf(text.substring(start + BLANKS_FORMAT_START.length(), end));
            } catch (Exception e) {
                e.printStackTrace();
            }
            newText.append(text.substring(0, start));

            text = text.substring(end + BLANKS_FORMAT_END.length(), text.length());

            BlanksRange range = new BlanksRange();
            range.start = newText.length();
            newText.append(getBlanks(count));
            range.end = newText.length();
            range.count = count;
            mBlanksRange.add(range);
        }
        setText(newText.toString());
    }


    /**
     * 获取空白字符串
     *
     * @param count 可输入字符个数
     * @return 空白字符串
     */
    private String getBlanks(int count) {
        if (count <= 0) {
            count = BLANKS_FORMAT_DEFAULT_COUNT;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 是否可以编辑
     *
     * @return true 可以编辑
     */
    public boolean isSupportEdit() {
        int end = getSelectionEnd();
        if (mBlanksRange == null || mBlanksRange.isEmpty()) {
            return false;
        }
        for (BlanksRange range : mBlanksRange) {
            if (range.contains(end)) {
                return true;
            }
        }
        return false;
    }


    private class BlanksRange {
        private int start;
        private int end;
        private int count;

        private boolean contains(int index) {
            return index > start && index < end;
        }

    }

}
