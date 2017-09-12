package kr.co.citizoomproject.android.citizoom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

/**
 * Created by ccei on 2016-08-07.
 */
public class SearchEditText extends AutoCompleteTextView implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener{

    private Drawable clearDrawable;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;

    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_clear_mtrl_alpha);
        clearDrawable = DrawableCompat.wrap(tempDrawable);
        DrawableCompat.setTintList(clearDrawable,getHintTextColors());
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());

        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }


    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText(null);
            }
            return true;
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(view, motionEvent);
        } else {
            return false;
        }

    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);
        setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
    }
}
