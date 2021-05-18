public class Matrix4x4
{
  private double[][] m;
  public Matrix4x4()
  {
    m = new double[4][4]; 
  }
  public void setValue(int r, int c, double value)
  {
    m[r][c] = value;
  }
  public double[][] getMatrix()
  {
    return m;
  }
  public double getValue(int r, int c)
  {
    return m[r][c];
  }
}
