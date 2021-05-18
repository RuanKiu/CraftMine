import java.util.ArrayList;

public class Mesh
{
  private ArrayList<Triangle> tris;
  public Mesh()
  {
    tris = new ArrayList<Triangle>(); 
  }
  public void addTris(Triangle t)
  {
    tris.add(t);
  }
  public ArrayList<Triangle> getTris()
  {
    return tris;
  }
}
