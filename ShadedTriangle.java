public class ShadedTriangle extends Triangle
{
  private double lum;
  public ShadedTriangle()
  {
    super();
    lum = 0;
  }
  public ShadedTriangle(double l)
  {
    super();
    lum = l;
  }
  public ShadedTriangle(Vector3D a, Vector3D b, Vector3D c)
  {
    super(a, b, c);
  }
  public void setLuminosity(double l)
  {
    lum = ;
  }
  public double getLuminosity()
  {
    return lum;
  }
}
