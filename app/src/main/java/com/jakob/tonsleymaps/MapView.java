package com.jakob.tonsleymaps;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/* Written by Jakob Pennington - Based on https://stackoverflow.com/questions/12169905/zoom-and-panning-imageview-android
 *
 * A View to display a map which can be zoomed and panned and behave as the user should expect
 */
public class MapView extends View {
    private  static final int INVALID_POINTER_ID = -1;

    // Map Drawable
    private Drawable mMap;
    private float mMapPosX;
    private float mMapPosY;

    // Place marker drawable
    private Drawable mPlace;
    private int mPlacePosX;
    private int mPlacePosY;
    private int mPlaceWidth;
    private int mPlaceHeight;

    // Touch variables
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId;

    // Scale variables
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor;
    private int mViewWidth;

    public MapView(Context context) {
        this(context, null, 0);
    }

    public MapView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        // Set up map drawable
        mMapPosY = 0;
        mMapPosY = 0;
        mMap = ContextCompat.getDrawable(context, R.drawable.map_ground);
        mMap.setBounds(0, 0, mViewWidth, (int) (mViewWidth / 1.6));

        //Set up placemarker drawable
        mPlacePosX = 0;
        mPlacePosY = 0;
        mPlace = ContextCompat.getDrawable(context, R.drawable.place_marker);
        mPlaceWidth = mPlace.getIntrinsicWidth()/2;
        mPlaceHeight = mPlace.getIntrinsicHeight()/2;

        //Set up scaling variables
        mActivePointerId = INVALID_POINTER_ID;
        mScaleFactor = 1.f;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        // The height of the maps is the height of the map divided by 1.6
        int preferred = (int)(mViewWidth /1.6);
        return getMeasurement(heightMeasureSpec, preferred);
    }

    private int measureWidth(int widthMeasureSpec) {
        // Width of the view is the width of the screen
        int preferred = mViewWidth;
        return getMeasurement(widthMeasureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement = 0;

        switch (MeasureSpec.getMode(measureSpec)){
            case(MeasureSpec.EXACTLY):
                // The width of the view is given
                measurement = specSize;
                break;
            case(MeasureSpec.AT_MOST):
                // Take the minimum of the preferred size and what it is set to be
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);

        final int action = event.getAction();
        switch(action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()){
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mMapPosX += dx;
                    mMapPosY += dy;

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Prevent the map from moving off the screen.
        int maxX, minX, maxY, minY;
        minX = (int) (mViewWidth - (mViewWidth *mScaleFactor));
        maxX = 0;
        minY = (int) ((mViewWidth /1.6)-(mViewWidth /1.6*mScaleFactor));
        maxY = 0;

        if (mMapPosX < minX){
            mMapPosX = minX;
        }
        if (mMapPosX > maxX){
            mMapPosX = maxX;
        }
        if (mMapPosY < minY){
            mMapPosY = minY;
        }
        if (mMapPosY > maxY){
            mMapPosY = maxY;
        }

        // Set up the placemarker to display on the map
        double adjustedPlacePosX = mPlacePosX*mViewWidth/1000 - (mPlaceWidth/2);
        double adjustedPlacePosY = mPlacePosY*mViewWidth/1000 - mPlaceHeight;
        mPlace.setBounds((int) adjustedPlacePosX, (int) adjustedPlacePosY, (int) adjustedPlacePosX + mPlaceWidth, (int) adjustedPlacePosY + mPlaceHeight);
        mPlace.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        canvas.save();
        canvas.translate(mMapPosX, mMapPosY);
        canvas.scale(mScaleFactor, mScaleFactor);

        mMap.draw(canvas);

        //Don't draw placemarker if X and Y = 0 ie. location is not in the database
        if (mPlacePosX != 0 || mPlacePosY != 0){
            mPlace.draw(canvas);
        }

        canvas.restore();
    }

    // Update the map as requested from MainActivity
    public void setmMap(Drawable mapDrawable){
        mMap = mapDrawable;
        mMap.setBounds(0, 0, mViewWidth, (int) (mViewWidth / 1.6));
        mScaleFactor = 1.0f;
        invalidate();
    }

    // Update the map as requested from MainActivity
    public void setPlacemarker(int placePosX, int placePosY){
        mPlacePosX = placePosX;
        mPlacePosY = placePosY;
        invalidate();
    }

    public void clearPlacemarker(){
        setPlacemarker(0, 0);
    }

    // Handle pinch and zoom gestures
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            final int displacement;
            final float scaleDiff;
            final float oldScaleFactor = mScaleFactor;
            mScaleFactor *= detector.getScaleFactor();

            // The map won't zoom smaller than the width of the screen but
            // will zoom to three times the size
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 3.0f));

            // Force the map to zoom in around the point between the fingers.
            scaleDiff = mScaleFactor - oldScaleFactor;
            displacement = (int) (scaleDiff * mViewWidth);

            // Since the sign of scaleDiff changes when you zoom in or out, displacement
            // can be subtracted from the coordinates of the canvas both ways.
            mMapPosX -= displacement/2;
            mMapPosY -= displacement/1.6/2;

            invalidate();
            return true;
        }
    }
}
