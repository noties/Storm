package ru.noties.performance_test;

import android.graphics.Color;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class ColorEvaluator {

    private final ColorHolder mGoodColor;
    private final ColorHolder mBadColor;
    private final ColorHolder mDiff;

    public ColorEvaluator(int goodColor, int badColor) {
        this.mGoodColor = ColorHolder.fromARGB(goodColor);
        this.mBadColor  = ColorHolder.fromARGB(badColor);
        this.mDiff      = getDiff(mGoodColor, mBadColor);
    }

    public ColorHolder getDiff(ColorHolder start, ColorHolder end) {
        return new ColorHolder(
                end.getAlpha()  - start.getAlpha(),
                end.getRed()    - start.getRed(),
                end.getGreen()  - start.getGreen(),
                end.getBlue()   - start.getBlue()
        );
    }

    public int getColor(long min, long max, long value) {

        if (value == min) {
            return mGoodColor.toColor();
        }

        if (value == max) {
            return mBadColor.toColor();
        }

        // ratio to min/max
        // apply

        final float ratio = (float) value / max;
        final ColorHolder holder = new ColorHolder(
                mGoodColor.getAlpha()   + (int) (mDiff.getAlpha()    * ratio),
                mGoodColor.getRed()     + (int) (mDiff.getRed()      * ratio),
                mGoodColor.getGreen()   + (int) (mDiff.getGreen()    * ratio),
                mGoodColor.getBlue()    + (int) (mDiff.getBlue()     * ratio)
        );
        return holder.toColor();
    }

    public static class ColorHolder {

        public static ColorHolder fromARGB(int color) {
            return new ColorHolder(
                    Color.alpha(color),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
            );
        }

        private final int alpha;
        private final int red;
        private final int green;
        private final int blue;

        public ColorHolder(int alpha, int red, int green, int blue) {
            this.alpha = alpha;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getAlpha() {
            return alpha;
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }

        public int toColor() {
            return Color.argb(alpha, red, green, blue);
        }
    }
}
