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
  public static Mesh createCube()
  {
    Mesh cube = new Mesh();

    // South face
    Triangle south1 = new Triangle(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0), new Vector3D(1, 1, 0));
    Triangle south2 = new Triangle(new Vector3D(0, 0, 0), new Vector3D(1, 1, 0), new Vector3D(1, 0, 0));

    // East face
    Triangle east1 = new Triangle(new Vector3D(1, 0, 0), new Vector3D(1, 1, 0), new Vector3D(1, 1, 1));
    Triangle east2 = new Triangle(new Vector3D(1, 0, 0), new Vector3D(1, 1, 1), new Vector3D(1, 0, 1));

    // North face
    Triangle north1 = new Triangle(new Vector3D(1, 0, 1), new Vector3D(1, 1, 1), new Vector3D(0, 1, 1));
    Triangle north2 = new Triangle(new Vector3D(1, 0, 1), new Vector3D(0, 1, 1), new Vector3D(0, 0, 1));

    // West face
    Triangle west1 = new Triangle(new Vector3D(0, 0, 1), new Vector3D(0, 1, 1), new Vector3D(0, 1, 0));
    Triangle west2 = new Triangle(new Vector3D(0, 0, 1), new Vector3D(0, 1, 0), new Vector3D(0, 0, 0));

    // Top face
    Triangle top1 = new Triangle(new Vector3D(0, 1, 0), new Vector3D(0, 1, 1), new Vector3D(1, 1, 1));
    Triangle top2 = new Triangle(new Vector3D(0, 1, 0), new Vector3D(1, 1, 1), new Vector3D(1, 1, 0));

    // Bottom face
    Triangle bottom1 = new Triangle(new Vector3D(0, 0, 1), new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));
    Triangle bottom2 = new Triangle(new Vector3D(0, 0, 1), new Vector3D(1, 0, 0), new Vector3D(1, 0, 1));

    cube.addTris(south1);
    cube.addTris(south2);
    cube.addTris(east1);
    cube.addTris(east2);
    cube.addTris(north1);
    cube.addTris(north2);
    cube.addTris(west1);
    cube.addTris(west2);
    cube.addTris(top1);
    cube.addTris(top2);
    cube.addTris(bottom1);
    cube.addTris(bottom2);

    return cube;
  }
  public static Mesh createChunk(Chunk chunk)
  {
    Mesh chunkMesh = new Mesh();
    for (int f = 0; f < chunk.getChunk().length; f++) {
      for (int r = 0; r < chunk.getChunk()[f].length; r++) {
        for (int c = 0; c < chunk.getChunk()[f][r].length; c++) {
          if (chunk.getChunk()[f][r][c])
          {
            if (f + 1 == chunk.getChunk().length || !chunk.getChunk()[f+1][r][c]) { 
              Triangle t1 = new Triangle(new Vector3D(c, f + 1, r), new Vector3D(c, f + 1, r + 1), new Vector3D(c + 1, f + 1, r + 1));
              Triangle t2 = new Triangle(new Vector3D(c, f + 1, r), new Vector3D(c + 1, f + 1, r + 1), new Vector3D(c + 1, f + 1, r));  
              chunkMesh.addTris(t1);
              chunkMesh.addTris(t2);
            }
            if (f - 1 < 0 || !chunk.getChunk()[f-1][r][c]) { 
              Triangle t1 = new Triangle(new Vector3D(c, f, r), new Vector3D(c, f, r + 1), new Vector3D(c + 1, f, r + 1));
              Triangle t2 = new Triangle(new Vector3D(c, f, r), new Vector3D(c + 1, f, r + 1), new Vector3D(c + 1, f, r));  
              chunkMesh.addTris(t1);
              chunkMesh.addTris(t2);
            }
            if (r - 1 >= 0 && !chunk.getChunk()[f][r-1][c]) {
              Triangle t1 = new Triangle(new Vector3D(c, f, r), new Vector3D(c, f + 1, r), new Vector3D(c + 1, f + 1, r));
              Triangle t2 = new Triangle(new Vector3D(c, f, r), new Vector3D(c + 1, f + 1, r), new Vector3D(c + 1, f, r));  
              chunkMesh.addTris(t1);
              chunkMesh.addTris(t2);
            }
            if (r + 1 < chunk.getChunk()[f].length && !chunk.getChunk()[f][r+1][c]) {
              Triangle t1 = new Triangle(new Vector3D(c + 1, f, r + 1), new Vector3D(c + 1, f + 1, r + 1), new Vector3D(c, f + 1, r + 1));
              Triangle t2 = new Triangle(new Vector3D(c + 1, f, r + 1), new Vector3D(c, f + 1, r + 1), new Vector3D(c, f, r + 1));  
              chunkMesh.addTris(t1);
              chunkMesh.addTris(t2); 
            }
            if (c - 1 >= 0 && !chunk.getChunk()[f][r][c-1]) {
              Triangle t1 = new Triangle(new Vector3D(c, f, r + 1), new Vector3D(c, f + 1, r + 1), new Vector3D(c, f + 1, r));
              Triangle t2 = new Triangle(new Vector3D(c, f, r + 1), new Vector3D(c, f + 1, r), new Vector3D(c, f, r));  
              chunkMesh.addTris(t1);
              chunkMesh.addTris(t2); 
            }
            if (c + 1 < chunk.getChunk()[f][r].length && !chunk.getChunk()[f][r][c+1]) {
              Triangle t1 = new Triangle(new Vector3D(c + 1, f, r), new Vector3D(c + 1, f + 1, r), new Vector3D(c + 1, f + 1, r + 1));
              Triangle t2 = new Triangle(new Vector3D(c + 1, f, r), new Vector3D(c + 1, f + 1, r + 1), new Vector3D(c + 1, f, r + 1));  
              chunkMesh.addTris(t1);
              chunkMesh.addTris(t2); 
            }
          }
        }
      }
    }
    return chunkMesh;
  }
}
