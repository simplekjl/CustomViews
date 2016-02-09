package company.example.customv;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 *http://code.tutsplus.com/tutorials/creating-compound-views-on-android--cms-22889
 * Creating a View in code
 *
 * This kind of custom Views are very useful when you need to develop something expecific and also, has to
 * to something, for example rotate text or something
 * Created by admin on 2/9/2016.
 */
public class MyCustomTV extends LinearLayout {

    private Button mNext;
    private Button mPrev;
    private CharSequence[] mSpinnerValues = null;
    private int mSelectedIndex = -1;
    /**
     * Identifier for the state to save the selected index of
     * the side spinner.
     */
    private static String STATE_SELECTED_INDEX = "SelectedIndex";

    /**
     * Identifier for the state of the super class.
     */
    private static String STATE_SUPER_CLASS = "SuperClass";

    public MyCustomTV(Context context) {
       super(context);
        init(context);
    }

    public MyCustomTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**We then call the getter method of the TypedArray object that has the right type for the
         * attribute you want, passing the identifier of the attribute as a parameter
         */
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomTV);
        mSpinnerValues = typedArray.getTextArray(R.styleable.MyCustomTV_values);
        typedArray.recycle();

        init(context);
    }

    public MyCustomTV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomTV);
        mSpinnerValues = typedArray.getTextArray(R.styleable.MyCustomTV_values);
        typedArray.recycle();
        init(context);
    }

    /**
     * Managing the view
     * @param context
     */

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_spinner, this);
    }

    /**
     * The onFinishInflate method of the compound view is called when all the views in the layout
     * are inflated and ready to use. This is the place to add your code if you need to modify views in the compound view.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        // When the previous button is pressed, select the previous value
        // in the list.
        mPrev = (Button) this.findViewById(R.id.sidespinner_view_previous);
        mPrev.setBackgroundResource(android.R.drawable.ic_media_previous);
        mPrev.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mSelectedIndex > 0) {
                    int newSelectedIndex = mSelectedIndex - 1;
                    setSelectedIndex(newSelectedIndex);
                }
            }
        });

        // When the next button is pressed, select the next item in the
        // list.
        mNext = (Button)this.findViewById(R.id.sidespinner_view_next);
        mNext.setBackgroundResource(android.R.drawable.ic_media_next);
        mNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mSpinnerValues != null
                        && mSelectedIndex < mSpinnerValues.length - 1) {
                    int newSelectedIndex = mSelectedIndex + 1;
                    setSelectedIndex(newSelectedIndex);
                }
            }
        });

        // Select the first value by default.
        setSelectedIndex(0);
    }


    /**
     * Sets the list of value in the spinner, selecting the first value
     * by default.
     *
     * @param values
     *           the values to set in the spinner.
     */
    public void setValues(CharSequence[] values) {
        mSpinnerValues = values;

        // Select the first item of the string array by default since
        // the list of value has changed.
        setSelectedIndex(0);
    }

    /**
     * Sets the selected index of the spinner.
     *
     * @param index
     *           the index of the value to select.
     */
    public void setSelectedIndex(int index) {
        // If no values are set for the spinner, do nothing.
        if (mSpinnerValues == null || mSpinnerValues.length == 0)
            return;

        // If the index value is invalid, do nothing.
        if (index < 0 || index >= mSpinnerValues.length)
            return;

        // Set the current index and display the value.
        mSelectedIndex = index;
        TextView currentValue;
        currentValue = (TextView)this
                .findViewById(R.id.sidespinner_view_current_value);
        currentValue.setText(mSpinnerValues[index]);

        // If the first value is shown, hide the previous button.
        if (mSelectedIndex == 0)
            mPrev.setVisibility(INVISIBLE);
        else
            mPrev.setVisibility(VISIBLE);

        // If the last value is shown, hide the next button.
        if (mSelectedIndex == mSpinnerValues.length - 1)
            mNext.setVisibility(INVISIBLE);
        else
            mNext.setVisibility(VISIBLE);
    }

    /**
     * Gets the selected value of the spinner, or null if no valid
     * selected index is set yet.
     *
     * @return the selected value of the spinner.
     */
    public CharSequence getSelectedValue() {
        // If no values are set for the spinner, return an empty string.
        if (mSpinnerValues == null || mSpinnerValues.length == 0)
            return "";

        // If the current index is invalid, return an empty string.
        if (mSelectedIndex < 0 || mSelectedIndex >= mSpinnerValues.length)
            return "";

        return mSpinnerValues[mSelectedIndex];
    }

    /**
     * Gets the selected index of the spinner.
     *
     * @return the selected index of the spinner.
     */
    public int getSelectedIndex() {
        return mSelectedIndex;
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(STATE_SUPER_CLASS,
                super.onSaveInstanceState());
        bundle.putInt(STATE_SELECTED_INDEX, mSelectedIndex);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle)state;

            super.onRestoreInstanceState(bundle
                    .getParcelable(STATE_SUPER_CLASS));
            setSelectedIndex(bundle.getInt(STATE_SELECTED_INDEX));
        }
        else
            super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views in the side
        // spinner are not saved since we handle the state in the
        // onSaveInstanceState.
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views in the side
        // spinner are not restored since we handle the state in the
        // onSaveInstanceState.
        super.dispatchThawSelfOnly(container);
    }
}
