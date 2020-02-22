/*
package com.example.textrecognition;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.hardware.Camera;

import com.google.android.gms.vision.CameraSource;

import java.lang.reflect.AccessibleObject;

public class Graphic {

    */
/**
     * Returns a RectF in which the left and right parameters of the provided Rect are adjusted
     * by translateX, and the top and bottom are adjusted by translateY.
     *//*

    public RectF translateRect(RectF inputRect) {
        RectF returnRect = new RectF();

        returnRect.left = translateX(inputRect.left);
        returnRect.top = translateY(inputRect.top);
        returnRect.right = translateX(inputRect.right);
        returnRect.bottom = translateY(inputRect.bottom);

        return returnRect;
    }

    */
/**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate
     * system.
     *//*

    public float translateX(float x) {

        if (mOverlay.get== CameraSource.CAMERA_FACING_FRONT) {
            return mOverlay.getWidth() - scaleX(x);
        } else {
            return scaleX(x);
        }
    }

    */
/**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate
     * system.
     *//*

    public float translateY(float y) {
        return scaleY(y);
    }

    */
/**
     * Returns a RectF in which the left and right parameters of the provided Rect are adjusted
     * by translateX, and the top and bottom are adjusted by translateY.
     *//*


    public float scaleX(float horizontal) {
        return horizontal * mOverlay.widthScaleFactor;
    }

    */
/**
     * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
     *//*

    public float scaleY(float vertical) {
        return vertical * mOverlay.heightScaleFactor;
    }


}
*/
