public class Vector3D
{
  private double x, y, z;
  public Vector3D()
  {
    x = y = z = 0;
  }
  public Vector3D(Vector3D v)
  {
    x = v.x();
    y = v.y();
    z = v.z();
  }
  public Vector3D(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
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

  public void setValue(double x, double y, double z)
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
}
