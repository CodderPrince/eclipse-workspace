/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package sample;

import java.util.Random;

/**
 * A 2-dimensional, single-precision, floating-point vector.
 */
public class Vecc2f {
    /**
     * The x coordinate.
     */
    public float x;

    /**
     * The y coordinate.
     */
    public float y;
    /**
     * Random
     */
    Random random = new Random();

    public Vecc2f() {
    }

    public Vecc2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vecc2f(Vecc2f v) {
        this.x = v.x;
        this.y = v.y;
    }


    /**
     * Sets the location of this <code>Vec2f</code> to the same
     * coordinates as the specified <code>Vec2f</code> object.
     *
     * @param v the specified <code>Vec2f</code> to which to set
     *          this <code>Vec2f</code>
     */
    public void set(Vecc2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Sets the location of this <code>Vec2f</code> to the
     * specified <code>float</code> coordinates.
     *
     * @param x the new X coordinate of this {@code Vec2f}
     * @param y the new Y coordinate of this {@code Vec2f}
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(double centerX, double centerY) {
        this.x = (float) centerX;
        this.y = (float) centerY;

    }

    public void set(int centerX, int centerY) {
        this.x = (float) centerX;
        this.y = (float) centerY;

    }

    /**
     * Returns the square of the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the square of the distance between the two
     * sets of specified coordinates.
     */
    public static float distanceSq(float x1, float y1, float x2, float y2) {
        x1 -= x2;
        y1 -= y2;
        return (x1 * x1 + y1 * y1);
    }

    /**
     * Returns the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the distance between the two sets of specified
     * coordinates.
     */
    public static float distance(float x1, float y1, float x2, float y2) {
        x1 -= x2;
        y1 -= y2;
        return (float) Math.sqrt(x1 * x1 + y1 * y1);
    }

    public void limit(float limit) {
        if (limit < magnitude()) {
            float constVar = (float) limit / magnitude();
            this.x = this.x * constVar;
            this.y = this.y * constVar;

        }
    }

    public void add(Vecc2f vec) {
        this.x = this.x + vec.x;
        this.y = this.y + vec.y;
    }

    public void add(float x, float y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    public void sub(Vecc2f vec) {
        this.x = this.x - vec.x;
        this.y = this.y - vec.y;
    }


    public void sub(float x, float y) {
        this.x = this.x - x;
        this.y = this.y - y;
    }

    public Vecc2f sub(Vecc2f v1, Vecc2f v2) {
        return sub(v1, v2, null);
    }

    static public Vecc2f sub(Vecc2f v1, Vecc2f v2, Vecc2f target) {
        if (target == null) {
            target = new Vecc2f(v1.x - v2.x, v1.y - v2.y);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y);
        }
        return target;
    }

    public void mult(float n) {
        this.x = this.x * n;
        this.y = this.y * n;
    }

    public void div(float divideBy) {
        this.x = (this.x / divideBy);
        this.y = (this.y / divideBy);
    }

    public void normalise() {
        float m = magnitude();
        if (m != 0 && m != 1) {
            div(m);
        }
    }

    public void random2D(int modifier) {
        //unit vector with a magnitude of 1
        this.x = (((random.nextFloat() * 2) - 1) / modifier);
        this.y = (((random.nextFloat() * 2) - 1) / modifier);
        // System.out.println(this.x + "  " + this.y + "  " + magnitude());

    }


    public float magnitude() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    public void setMag(float magnitude) {

        float constA = magnitude / magnitude();
        this.x = this.x * constA;
        this.y = this.y * constA;

    }


    /**
     * Returns the square of the distance from this
     * <code>Vec2f</code> to a specified point.
     *
     * @param vx the X coordinate of the specified point to be measured
     *           against this <code>Vec2f</code>
     * @param vy the Y coordinate of the specified point to be measured
     *           against this <code>Vec2f</code>
     * @return the square of the distance between this
     * <code>Vec2f</code> and the specified point.
     */
    public float distanceSq(float vx, float vy) {
        vx -= x;
        vy -= y;
        return (vx * vx + vy * vy);
    }

    /**
     * Returns the square of the distance from this
     * <code>Vec2f</code> to a specified <code>Vec2f</code>.
     *
     * @param v the specified point to be measured
     *          against this <code>Vec2f</code>
     * @return the square of the distance between this
     * <code>Vec2f</code> to a specified <code>Vec2f</code>.
     */
    public float distanceSq(Vecc2f v) {
        float vx = v.x - this.x;
        float vy = v.y - this.y;
        return (vx * vx + vy * vy);
    }

    /**
     * Returns the distance from this <code>Vec2f</code> to
     * a specified point.
     *
     * @param vx the X coordinate of the specified point to be measured
     *           against this <code>Vec2f</code>
     * @param vy the Y coordinate of the specified point to be measured
     *           against this <code>Vec2f</code>
     * @return the distance between this <code>Vec2f</code>
     * and a specified point.
     */
    public float distance(float vx, float vy) {
        vx -= x;
        vy -= y;
        return (float) Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Returns the distance from this <code>Vec2f</code> to a
     * specified <code>Vec2f</code>.
     *
     * @param v the specified point to be measured
     *          against this <code>Vec2f</code>
     * @return the distance between this <code>Vec2f</code> and
     * the specified <code>Vec2f</code>.
     */
    public float distance(Vecc2f v) {
        float vx = v.x - this.x;
        float vy = v.y - this.y;
        return (float) Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Returns the hashcode for this <code>Vec2f</code>.
     *
     * @return a hash code for this <code>Vec2f</code>.
     */
    @Override
    public int hashCode() {
        int bits = 7;
        bits = 31 * bits + Float.floatToIntBits(x);
        bits = 31 * bits + Float.floatToIntBits(y);
        return bits;
    }

    /**
     * Determines whether or not two 2D points or vectors are equal.
     * Two instances of <code>Vec2f</code> are equal if the values of their
     * <code>x</code> and <code>y</code> member fields, representing
     * their position in the coordinate space, are the same.
     *
     * @param obj an object to be compared with this <code>Vec2f</code>
     * @return <code>true</code> if the object to be compared is
     * an instance of <code>Vec2f</code> and has
     * the same values; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Vecc2f) {
            Vecc2f v = (Vecc2f) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    /**
     * Returns a <code>String</code> that represents the value
     * of this <code>Vec2f</code>.
     *
     * @return a string representation of this <code>Vec2f</code>.
     */
    @Override
    public String toString() {
        return "Vec2f[" + x + ", " + y + "]";
    }

    public void fromAngle(double v) {
        this.x = (float) Math.cos(v);
        this.y = (float) Math.sin(v);
    }

    public float toAngle() {

        if (this.x > 0) {
            return (float) (90 + Math.toDegrees(Math.atan(this.y / this.x)));
        } else if (this.x < 0) {
            return (float) (270 + Math.toDegrees(Math.atan(this.y / this.x)));
        } else if (this.x == 0 && this.y < 0) {
            return 0;
        } else if (this.x == 0 && this.y > 0) {
            return 180;
        } else if (this.x < 0 && this.y == 0) {
            return 270;
        } else if (this.x > 0 && this.y == 0) {
            return 45;
        }
        return 0;
    }


}
