import java.util.ArrayList;
public class Engine
{
  private Camera camera;
  private Matrix4x4 projectionMatrix;
  private Matrix4x4 rotationXMatrix, rotationZMatrix, rotationYMatrix;
  private Matrix4x4 transformationMatrix;
  private Matrix4x4 modelMatrix;
  private Matrix4x4 viewMatrix;
  private ArrayList<Mesh> meshes;
  private WorldLight worldLightDirection;
  private Vector3D look;
  private Vector3D up;
  private Vector3D target; 
  public Engine()
  {
    camera = new Camera();
    projectionMatrix = Matrix4x4.makeProjectionMatrix(camera, 700, 700);
    meshes = new ArrayList<Mesh>();
    worldLightDirection = new WorldLight(-1, -2, -1);
    Vector3D.normalizeVector(worldLightDirection, worldLightDirection);
    look = new Vector3D(0, 0, 1); 
    up = new Vector3D(0, 1, 0);
    target = new Vector3D();
    Vector3D.addVectors(target, look, camera);

    // Rotation matrix
    rotationXMatrix = Matrix4x4.makeRotationMatrix("x" , 0); 
    rotationZMatrix = Matrix4x4.makeRotationMatrix("z", 0); 
    rotationYMatrix = Matrix4x4.makeRotationMatrix("y", 0); 

    // Transformation matrix
    transformationMatrix = Matrix4x4.makeTranslationMatrix(-0.5, -5, 20);

    // Model matrix
    modelMatrix = new Matrix4x4();
    updateWorldMatrix();

    // View matrix
    viewMatrix = new Matrix4x4();
    updateViewMatrix();

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
    modelMatrix = Matrix4x4.multiplyMatrix(rotationXMatrix, rotationYMatrix);
    modelMatrix = Matrix4x4.multiplyMatrix(rotationZMatrix, modelMatrix);
    modelMatrix = Matrix4x4.multiplyMatrix(transformationMatrix, modelMatrix);
  }
  public void updateViewMatrix()
  {
    Vector3D.addVectors(target, look, camera);
    Matrix4x4.createLookAtViewTransform(viewMatrix, camera, target, up);
    Matrix4x4.quickInverse(viewMatrix); 
    //Matrix4x4.createFPSViewTransform(viewMatrix, camera, camera.getPitch(), camera.getYaw());
    System.out.println(viewMatrix);
  }
  public void rotateCamera(double pitchChange, int yawChange)
  {
    if (camera.getYaw() + yawChange > 360) 
      camera.setYaw(360);
    else if (camera.getYaw() + yawChange < 0) 
      camera.setYaw(0);
    else 
      camera.setYaw(camera.getYaw() + yawChange);

    if (camera.getPitch() + pitchChange > 90) 
      camera.setPitch(90);
    else if (camera.getPitch() + pitchChange < -90)
      camera.setPitch(-90);
    else
      camera.setPitch(camera.getPitch() + pitchChange);

    updateViewMatrix();
  }
  public void moveCamera(double x, double y, double z)
  {
    up.setValues(up.x() + x, up.y() + y, up.z() + z);
    look.setValues(look.x() + x, look.y() + y, look.z() + z);
    camera.setValues(camera.x() + x, camera.y() +  y, camera.z() + z);
    updateViewMatrix();
  }
  public Camera getCamera()
  {
    return camera;
  }
  public ArrayList<ShadedTriangle> createProjections()
  {
    updateWorldMatrix();
    updateViewMatrix();
    
    ArrayList<ShadedTriangle> projectedTriangles = new ArrayList<ShadedTriangle>();
    for (Mesh m : meshes)
    {
      for (Triangle triangle : m.getTris())
      {
        ShadedTriangle projectedTriangle;
        Vector3D point0 = Matrix4x4.multiplyMatrixVector(triangle.getVectors()[0], modelMatrix);
        Vector3D point1 = Matrix4x4.multiplyMatrixVector(triangle.getVectors()[1], modelMatrix);
        Vector3D point2 = Matrix4x4.multiplyMatrixVector(triangle.getVectors()[2], modelMatrix);
        
        Matrix4x4.multiplyMatrixVector(point0, point0, viewMatrix);    
        Matrix4x4.multiplyMatrixVector(point1, point1, viewMatrix);     
        Matrix4x4.multiplyMatrixVector(point2, point2, viewMatrix); 

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
          Vector3D.divideVector(point0, point0, point0.w());
          Matrix4x4.multiplyMatrixVector(point1, point1, projectionMatrix);     
          Vector3D.divideVector(point1, point1, point1.w());
          Matrix4x4.multiplyMatrixVector(point2, point2, projectionMatrix); 
          Vector3D.divideVector(point2, point2, point2.w());
          projectedTriangle = new ShadedTriangle(point0, point1, point2, dp);
          projectedTriangles.add(projectedTriangle);
        }
      }
    }
    return projectedTriangles;
  }
}
