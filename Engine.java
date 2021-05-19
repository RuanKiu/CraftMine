import java.util.ArrayList;
public class Engine
{
  private Camera camera;
  private double aspectRatio;
  private double fovRadiansHorizontal, fovRadiansVertical;
  private Matrix4x4 projectionMatrix;
  private RotationMatrix4x4 rotationXMatrix, rotationZMatrix;
  private ArrayList<Mesh> meshes;
  private WorldLight worldLightDirection;
  public Engine()
  {
    camera = new Camera();
    projectionMatrix = new Matrix4x4();
    fovRadiansVertical = 1.0 / Math.tan(camera.getVerticalFOV() * 0.5 / 180.0 * 3.14159);
    calculateFOV(1920, 1080); 
    meshes = new ArrayList<Mesh>();
    worldLightDirection = new WorldLight(-1, -.05, 1);

    // Projection matrix
    projectionMatrix.setValue(0, 0, fovRadiansHorizontal);
    projectionMatrix.setValue(1, 1, fovRadiansVertical);
    projectionMatrix.setValue(2, 2, camera.getFar() / (camera.getFar() - camera.getNear()));
    projectionMatrix.setValue(3, 2, (-camera.getFar() * camera.getNear()) / (camera.getFar() - camera.getNear()));
    projectionMatrix.setValue(2, 3, 1.0);
    projectionMatrix.setValue(3, 3, 0.0); 
    
    // Rotation matrix

    rotationXMatrix = new RotationMatrix4x4(1, "x");
    rotationZMatrix = new RotationMatrix4x4(2, "z");

    // Adding a cube
    Mesh meshCube = createCube(); 
    meshes.add(meshCube);

    // Adding a chunk
    Chunk c = new Chunk();
    Mesh meshChunk = createChunk(c);
    meshes.add(meshChunk);
  }
  private Vector3D multiplyMatrixVector(Vector3D vector, Matrix4x4 matrix)
  {
    Vector3D output = new Vector3D();
    output.setX(vector.x() * matrix.getValue(0, 0) + vector.y() * matrix.getValue(1, 0) + vector.z() * matrix.getValue(2, 0) + matrix.getValue(3, 0)); 
    output.setY(vector.x() * matrix.getValue(0, 1) + vector.y() * matrix.getValue(1, 1) + vector.z() * matrix.getValue(2, 1) + matrix.getValue(3, 1)); 
    output.setZ(vector.x() * matrix.getValue(0, 2) + vector.y() * matrix.getValue(1, 2) + vector.z() * matrix.getValue(2, 2) + matrix.getValue(3, 2)); 
    double w = vector.x() * matrix.getValue(0, 3) + vector.y() * matrix.getValue(1, 3) + vector.z() * matrix.getValue(2, 3) + matrix.getValue(3, 3);

    if (w != 0) {
      output.setX(output.x()/w);
      output.setY(output.y()/w);
      output.setZ(output.z()/w);
    }
    return output;
  }
  private void calculateFOV(double w, double h)
  {
    aspectRatio = h/w;
    double verticalFOV = Math.atan(Math.tan(Math.toRadians(camera.getVerticalFOV()/2))*(w/h)); 
    fovRadiansHorizontal = 1.0 / Math.tan(verticalFOV);
    projectionMatrix.setValue(0, 0, fovRadiansHorizontal);
  }
  public ArrayList<ShadedTriangle> createProjections(double w, double h)
  {
    calculateFOV(w, h);
    ArrayList<ShadedTriangle> projectedTriangles = new ArrayList<ShadedTriangle>();
    int offset = 0;
    int offsetY = 5;
    for (Mesh m : meshes)
    {
      for (Triangle triangle : m.getTris())
      {
        ShadedTriangle projectedTriangle;
        Vector3D rotated1 = multiplyMatrixVector(triangle.getVectors()[0], rotationXMatrix);
        Vector3D rotated2 = multiplyMatrixVector(triangle.getVectors()[1], rotationXMatrix);
        Vector3D rotated3 = multiplyMatrixVector(triangle.getVectors()[2], rotationXMatrix);
        Vector3D transformed0 = multiplyMatrixVector(rotated1, rotationZMatrix);
        Vector3D transformed1 = multiplyMatrixVector(rotated2, rotationZMatrix);
        Vector3D transformed2 = multiplyMatrixVector(rotated3, rotationZMatrix);
        transformed0.setZ(transformed0.z() + offset);
        transformed0.setY(transformed0.y() + offsetY);
        transformed1.setZ(transformed1.z() + offset);
        transformed1.setY(transformed1.y() + offsetY);
        transformed2.setZ(transformed2.z() + offset);
        transformed2.setY(transformed2.y() + offsetY);
        
        Vector3D normal = new Vector3D();
        Vector3D line1 = new Vector3D();
        Vector3D line2 = new Vector3D();


        line1.setX(transformed1.x() - transformed0.x());
        line1.setY(transformed1.y() - transformed0.y());
        line1.setZ(transformed1.z() - transformed0.z());
        
        line2.setX(transformed2.x() - transformed0.x());
        line2.setY(transformed2.y() - transformed0.y());
        line2.setZ(transformed2.z() - transformed0.z());

        normal.setX(line1.y() * line2.z() - line1.z() * line2.y());
        normal.setY(line1.z() * line2.x() - line1.x() * line2.z());
        normal.setZ(line1.x() * line2.y() - line1.y() * line2.x()); 
        
        double l = Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        normal.setX(normal.x() / l);
        normal.setY(normal.y() / l);
        normal.setZ(normal.z() / l);

        if (normal.x() * (transformed0.x() - camera.x()) +
            normal.y() * (transformed0.y() - camera.y()) +
            normal.z() * (transformed0.z() - camera.z()) < 0
          )
        {
          double l2 = Math.sqrt(worldLightDirection.x() * worldLightDirection.x() + worldLightDirection.y() * worldLightDirection.y() + worldLightDirection.z() * worldLightDirection.z());
          worldLightDirection.setX(worldLightDirection.x() / l2);
          worldLightDirection.setY(worldLightDirection.y() / l2);
          worldLightDirection.setZ(worldLightDirection.z() / l2);

          double dp = normal.x() * worldLightDirection.x() + normal.y() * worldLightDirection.y() + normal.z() * worldLightDirection.z();

          Vector3D point1 = multiplyMatrixVector(transformed0, projectionMatrix);    
          Vector3D point2 = multiplyMatrixVector(transformed1, projectionMatrix);     
          Vector3D point3 = multiplyMatrixVector(transformed2, projectionMatrix); 
          projectedTriangle = new ShadedTriangle(point1, point2, point3, dp);
          projectedTriangles.add(projectedTriangle);
          //rotationXMatrix.setDegree(rotationXMatrix.getDegree() + 0.1);
          //rotationZMatrix.setDegree(rotationZMatrix.getDegree() + 0.05);
        }
      }
    }
    return projectedTriangles;
  }
  public Mesh createCube()
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
  public Mesh createChunk(Chunk chunk)
  {
    Mesh chunkMesh = new Mesh();
    for (int f = 0; f < chunk.getChunk().length - 1; f++) {
      for (int r = 0; r < chunk.getChunk()[f].length; r++) {
        for (int c = 0; c < chunk.getChunk()[f][r].length; c++) {
          if (chunk.getChunk()[f + 1][r][c] == false) {
            Triangle t1 = new Triangle(new Vector3D(c + 1, f + 1, r + 1), new Vector3D(c, f + 1, r + 1), new Vector3D(c, f + 1, r));
            Triangle t2 = new Triangle(new Vector3D(c + 1, f + 1, r), new Vector3D(c + 1, f + 1, r + 1), new Vector3D(c, f + 1, r));  
            chunkMesh.addTris(t1);
            chunkMesh.addTris(t2);
          }
        }
      }
    }
    return chunkMesh;
  }
}
