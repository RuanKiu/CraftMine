public class Camera extends Vector3D
{
  private double fovV;
  private double near;
  private double far;
  public Camera()
  {
    super();
    fovV = 68;
    near = 0.1;
    far = 1000.0;
  }
  public Camera(double x, double y, double z)
  {
    super(x, y, z);
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
