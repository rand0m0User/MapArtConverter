package ch.n1b.vector;

/**
 * Created with IntelliJ IDEA. User: Thomas Date: 29.09.13 Time: 23:43 To change
 * this template use File | Settings | File Templates.
 */
public final class Vec3D {

    public final int X;
    public final int Y;
    public final int Z;

    public Vec3D(int x, int y, int z) {
        X = x;
        Y = y;
        Z = z;
    }

    public final Vec3D add(Vec3D c) {
        return new Vec3D(X + c.X, Y + c.Y, Z + c.Z);
    }

    public final int dot(Vec3D c) {
        return X * c.X + Y * c.Y + Z * c.Z;
    }

    public final Vec3D mult(int factor) {
        return new Vec3D(X * factor, Y * factor, Z * factor);
    }

    public final Vec3D cross(Vec3D c) {
        return new Vec3D(Y * c.Z - Z * c.Y, Z * c.X - X * c.Z, X * c.Y - Y * c.Y);
    }

    public final Vec3D binary() {
        int x = 0, y = 0, z = 0;
        if (X != 0) {
            x = X / Math.abs(X);
        }
        if (Y != 0) {
            y = Y / Math.abs(Y);
        }
        if (Z != 0) {
            z = Z / Math.abs(Z);
        }
        return new Vec3D(x, y, z);
    }

    public final Vec3D norm2() {
        int abs = abs();
        return new Vec3D(X / abs, Y / abs, Z / abs);
    }

    public final int abs() {
        return (int) Math.round(Math.sqrt(X * X + Y * Y + Z * Z));
    }

    @Override
    public String toString() {
        return "Vec3D[" + X + "/" + Y + "/" + Z + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vec3D vec3D = (Vec3D) o;

        if (X != vec3D.X) {
            return false;
        }
        if (Y != vec3D.Y) {
            return false;
        }
        if (Z != vec3D.Z) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = X;
        result = 31 * result + Y;
        result = 31 * result + Z;
        return result;
    }
}
