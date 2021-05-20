public class Vector3D
{
  private double x, y, z, w;
  public Vector3D()
  {
    x = y = z = w = 0;
  }
  public Vector3D(Vector3D v)
  {
    x = v.x();
    y = v.y();
    z = v.z();
    w = v.w();
  }
  public Vector3D(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    w = 1;
  }
  public Vector3D(double x, double y, double z, double w)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }
  public double x()
  {
    return x;
  }

  public double y()
  {
    return y;
  }

  public double z()
  {
    return z;
  }

  public double w()
  {
    return w;
  }

  public void setValues(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setX(double x)
  {
    this.x = x;
  }

  public void setY(double y)
  {
    this.y = y;
  }

  public void setZ(double z)
  {
    this.z = z;
  }

  public void setW(double w)
  {
    this.w = w;
  }
  public static void addVectors(Vector3D result,Vector3D a, Vector3D b) 
  {
    result.setValues(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
  }
  public static void subtractVectors(Vector3D result, Vector3D a, Vector3D b) {
    result.setValues(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
  }
  public static void multiplyVectors(Vector3D result, Vector3D a, double d) {
    result.setValues(a.x() * d, a.y() * d, a.z() * d);
  }
  public static void divideVector(Vector3D result, Vector3D a, double d) {
    result.setValues(a.x() / d, a.y() / d, a.z() / d);
  }
  public static double vectorDotProduct(Vector3D a, Vector3D b) {
    return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
  }
  public static double vectorLength(Vector3D a) 
  {
    return Math.sqrt(vectorDotProduct(a, a));
  }
  public static void normalizeVector(Vector3D result, Vector3D a)
  {
    double l = vectorLength(a);
    result.setValues(a.x() / l, a.y() / l, a.z() / l);
  }
  public static void crossProduct(Vector3D product, Vector3D a, Vector3D b)
  {
    product.setX(a.y() * b.z() - a.z() * b.y());
    product.setY(a.z() * b.x() - a.x() * b.z());
    product.setZ(a.x() * b.y() - a.y() * b.x());
  }
 
}
