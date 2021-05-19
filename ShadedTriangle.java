public class ShadedTriangle extends Triangle
{
  private double lum;
  private double r, b, g;
  public ShadedTriangle()
  {
    super();
    lum = 0;
    r = g = b = 100;
  }
  public ShadedTriangle(double l)
  {
    super();
    lum = Math.abs(l);
    r = g = b = 100;
  }
  public ShadedTriangle(Vector3D x, Vector3D y, Vector3D z)
  {
    super(x, y, z);
    r = g = b = 100;
  }
  public ShadedTriangle(Vector3D x, Vector3D y, Vector3D z, double l)
  {
    this(x, y, z);
    lum = Math.abs(l);
  }
  public void setLuminosity(double l)
  {
    lum = Math.abs(l);
  }
  public double getLuminosity()
  {
    return lum;
  }
  public double r()
  {
    return r * lum / 255;
  }
  public double g()
  {
    return g * lum / 255;
  }
  public double b()
  {
    return b * lum / 255;
  }
}
