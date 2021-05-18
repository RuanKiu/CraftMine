public class Camera
{
  private double x;
  private double y;
  private double z;
  private double fovV;
  private double near;
  private double far;
  public Camera()
  {
    x = 0.0;
    y = 0.0;
    z = 0.0;
    fovV = 68;
    near = 0.1;
    far = 1000.0;
  }
  public double getVerticalFOV()
  {
    return fovV;
  }
  public double getNear()
  {
    return near;
  }
  public double getFar()
  {
    return far;
  }
  public void setVerticalFOV(double f)
  {
    fovV = f;
  }
}
