import java.util.ArrayList;
public class Engine
{
  private Camera camera;
  private Matrix4x4 projectionMatrix;
  private Matrix4x4 rotationXMatrix, rotationZMatrix, rotationYMatrix;
  private Matrix4x4 transformationMatrix;
  private Matrix4x4 worldMatrix;
  private ArrayList<Mesh> meshes;
  private WorldLight worldLightDirection;
  public Engine()
  {
    camera = new Camera();
    projectionMatrix = Matrix4x4.makeProjectionMatrix(camera, 700, 700);
    meshes = new ArrayList<Mesh>();
    worldLightDirection = new WorldLight(-1, -2, -1);
    Vector3D.normalizeVector(worldLightDirection, worldLightDirection);

    // Rotation matrix
    rotationXMatrix = Matrix4x4.makeRotationMatrix("x" , 0); 
    rotationZMatrix = Matrix4x4.makeRotationMatrix("z", 0); 
    rotationYMatrix = Matrix4x4.makeRotationMatrix("y", 0); 

    // Transformation matrix
    transformationMatrix = Matrix4x4.makeTranslationMatrix(-0.5, -5, 30);

    // World matrix
    worldMatrix = new Matrix4x4();
    updateWorldMatrix();

    // Adding a cube
    Mesh meshCube = Mesh.createCube(); 
    //meshes.add(meshCube);

    // Adding a chunk
    Chunk c = new Chunk();
    Mesh meshChunk = Mesh.createChunk(c);
    meshes.add(meshChunk);
  }
  public void updateFOV(double w, double h)
  {
    double verticalFOV = Math.atan(Math.tan(Math.toRadians(camera.getVerticalFOV()/2))*(w/h)); 
    double horizontalFOV = 1.0 / Math.tan(verticalFOV);
    projectionMatrix.setValue(0, 0, horizontalFOV); 
  }
  private void updateWorldMatrix()
  {
    worldMatrix = Matrix4x4.multiplyMatrix(rotationXMatrix, rotationYMatrix);
    worldMatrix = Matrix4x4.multiplyMatrix(rotationZMatrix, worldMatrix);
    worldMatrix = Matrix4x4.multiplyMatrix(transformationMatrix, worldMatrix);
  }
  public ArrayList<ShadedTriangle> createProjections()
  {
    ArrayList<ShadedTriangle> projectedTriangles = new ArrayList<ShadedTriangle>();
    for (Mesh m : meshes)
    {
      for (Triangle triangle : m.getTris())
      {
        ShadedTriangle projectedTriangle;
        updateWorldMatrix();
        Vector3D point0 = Matrix4x4.multiplyMatrixVector(triangle.getVectors()[0], worldMatrix);
        Vector3D point1 = Matrix4x4.multiplyMatrixVector(triangle.getVectors()[1], worldMatrix);
        Vector3D point2 = Matrix4x4.multiplyMatrixVector(triangle.getVectors()[2], worldMatrix);

        Vector3D normal = new Vector3D();
        Vector3D line1 = new Vector3D();
        Vector3D line2 = new Vector3D();

        Vector3D.subtractVectors(line1, point1, point0);
        Vector3D.subtractVectors(line2, point2, point0);
        Vector3D.crossProduct(normal, line1, line2);

        double l = Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        normal.setX(normal.x() / l);
        normal.setY(normal.y() / l);
        normal.setZ(normal.z() / l);

        if (normal.x() * (point0.x() - camera.x()) +
            normal.y() * (point0.y() - camera.y()) +
            normal.z() * (point0.z() - camera.z()) < 0
           )
        {
          double dp = Vector3D.vectorDotProduct(normal, worldLightDirection);
          dp -= (Math.abs(100 - (point0.z() + point1.z() + point2.z())/3) / (100 + Math.abs(point0.z() + point1.z() + point2.z()))) / 4;

          Matrix4x4.multiplyMatrixVector(point0, point0, projectionMatrix);    
          Matrix4x4.multiplyMatrixVector(point1, point1, projectionMatrix);     
          Matrix4x4.multiplyMatrixVector(point2, point2, projectionMatrix); 
          projectedTriangle = new ShadedTriangle(point0, point1, point2, dp);
          projectedTriangles.add(projectedTriangle);
        }
      }
    }
    return projectedTriangles;
  }
}
