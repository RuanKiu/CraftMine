public class Camera
{
  private double x;
  private double y;
  private double z;
  private double fov;
  private double near;
  private double far;
  public Camera()
  {
    x = 0.0;
    y = 0.0;
    z = 0.0;
    fov = 90;
    near = 0.1;
    far = 1000.0;
  }
  public double getFOV()
  {
    return fov;
  }
  public double getNear()
  {
    return near;
  }
  public double getFar()
  {
    return far;
  }
  public void setFOV(double f)
  {
    fov = f;
  }
}
