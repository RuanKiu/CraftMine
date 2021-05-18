public class Triangle
{
  private Vector3D[] points;
  public Triangle()
  {
    points = new Vector3D[3];
  }
  public Triangle(Vector3D vec1, Vector3D vec2, Vector3D vec3)
  {
    this();
    points[0] = vec1;
    points[1] = vec2;
    points[2] = vec3;
  }
  public Vector3D[] getVectors()
  {
    return points;
  }
}
