public class RotationMatrix4x4 extends Matrix4x4
{
  private double degree;
  private String axis;
  public RotationMatrix4x4(double degree, String axis)
  {
    super();
    this.axis = axis;
    this.degree = degree;
    updateMatrix();
  }
  public void updateMatrix()
  {
    switch (axis)
    {
      case "z":
        super.setValue(0, 0, Math.cos(Math.toRadians(degree)));
        super.setValue(0, 1, Math.sin(Math.toRadians(degree)));
        super.setValue(1, 0, -1 * Math.sin(Math.toRadians(degree)));
        super.setValue(1, 1, Math.cos(Math.toRadians(degree)));
        super.setValue(2, 2, 1);
        super.setValue(3, 3, 1);
        break;
      case "x":
        super.setValue(0, 0, 1);
        super.setValue(1, 1, Math.cos(Math.toRadians(degree)));
        super.setValue(1, 2, Math.sin(Math.toRadians(degree)));
        super.setValue(2, 1, -1 * Math.sin(Math.toRadians(degree)));
        super.setValue(2, 2, Math.cos(Math.toRadians(degree)));
        super.setValue(3, 3, 1);
        break;
      case "y":
        super.setValue(0, 0, Math.cos(Math.toRadians(degree)));
        super.setValue(0, 2, Math.sin(Math.toRadians(degree)));
        super.setValue(2, 0, -1 * Math.sin(Math.toRadians(degree)));
        super.setValue(1, 1, 1);
        super.setValue(2, 2, Math.cos(Math.toRadians(degree)));
        super.setValue(3, 3, 1);
        break;
    }
  }
  public void setDegree(double d)
  {
    degree = d % 360;
    updateMatrix();
  }
  public double getDegree()
  {
    return degree;
  }
}

