public class Camera extends Vector3D
{
  private double fovV;
  private double near;
  private double far;
  private double pitch;
  private double yaw;
  public Camera()
  {
    super();
    fovV = 68;
    near = 0.1;
    far = 1000.0;
    pitch = 0;
    yaw = 180;
  }
  public Camera(double x, double y, double z)
  {
    super(x, y, z);
    fovV = 68;
    near = 0.1;
    far = 1000.0;
    pitch = 0;
    yaw = 180;
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
  public double getPitch()
  {
    return pitch;
  }
  public void setPitch(double p)
  {
    pitch = p;
  }
  public double getYaw()
  {
    return yaw;
  }
  public void setYaw(double y)
  {
    yaw = y;
  }
}
