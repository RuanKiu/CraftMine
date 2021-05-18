public class RotationMatrix4x4 extends Matrix4x4
{
  private double degree;
  public RotationMatrix4x4(double degree, String axis)
  {
    super();
    this.degree = degree;
    switch (axis)
    {
      case "z":
        updateMatrixZ();
        break;
      case "x":
        updateMatrixX();
        break;
      case "y":
        updateMatrixY();
        break;
    }

  }
  public void updateMatrixZ()
  {
    super.setValue(0, 0, Math.cos(Math.toRadians(degree)));
    super.setValue(0, 1, Math.sin(Math.toRadians(degree)));
    super.setValue(1, 0, -1 * Math.sin(Math.toRadians(degree)));
    super.setValue(1, 1, Math.cos(Math.toRadians(degree)));
    super.setValue(2, 2, 1);
    super.setValue(3, 3, 1);
  }
  public void updateMatrixX()
  {
    super.setValue(0, 0, 1);
    super.setValue(1, 1, Math.cos(Math.toRadians(degree)));
    super.setValue(1, 2, Math.sin(Math.toRadians(degree)));
    super.setValue(2, 1, -1 * Math.sin(Math.toRadians(degree)));
    super.setValue(2, 2, Math.cos(Math.toRadians(degree)));
    super.setValue(3, 3, 1);
  }
  public void updateMatrixY()
  {
    super.setValue(0, 0, Math.cos(Math.toRadians(degree)));
    super.setValue(0, 2, Math.sin(Math.toRadians(degree)));
    super.setValue(2, 0, -1 * Math.sin(Math.toRadians(degree)));
    super.setValue(1, 1, 1);
    super.setValue(2, 2, Math.cos(Math.toRadians(degree)));
    super.setValue(3, 3, 1);
  }
  public void setDegree(double d)
  {
    degree = d;
  }
  public double getDegree()
  {
    return degree;
  }
}

