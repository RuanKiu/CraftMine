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
    worldLightDirection = new WorldLight(-1, -2, -1);

    // Projection matrix
    projectionMatrix.setValue(0, 0, fovRadiansHorizontal);
    projectionMatrix.setValue(1, 1, -1 * fovRadiansVertical);
    projectionMatrix.setValue(2, 2, camera.getFar() / (camera.getFar() - camera.getNear()));
    projectionMatrix.setValue(3, 2, (-camera.getFar() * camera.getNear()) / (camera.getFar() - camera.getNear()));
    projectionMatrix.setValue(2, 3, 1.0);
    projectionMatrix.setValue(3, 3, 0.0); 
    
    // Rotation matrix

    rotationXMatrix = new RotationMatrix4x4(1, "x");
    rotationZMatrix = new RotationMatrix4x4(2, "z");

    // Adding a cube
    Mesh meshCube = Mesh.createCube(); 
    meshes.add(meshCube);

    // Adding a chunk
    Chunk c = new Chunk();
    Mesh meshChunk = Mesh.createChunk(c);
    //meshes.add(meshChunk);
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
    int offsetZ = 10;
    int offsetY = -3;
    int offsetX = 4;
    for (Mesh m : meshes)
    {
      for (Triangle triangle : m.getTris())
      {
        ShadedTriangle projectedTriangle;
        Vector3D transformed0 = new Vector3D(triangle.getVectors()[0]);
        Vector3D transformed1 = new Vector3D(triangle.getVectors()[1]);
        Vector3D transformed2 = new Vector3D(triangle.getVectors()[2]);
        transformed0.setZ(transformed0.z() + offsetZ);
        transformed0.setY(transformed0.y() + offsetY);
        transformed0.setX(transformed0.x() + offsetX);
        transformed1.setZ(transformed1.z() + offsetZ);
        transformed1.setY(transformed1.y() + offsetY);
        transformed1.setX(transformed1.x() + offsetX);
        transformed2.setZ(transformed2.z() + offsetZ);
        transformed2.setY(transformed2.y() + offsetY);
        transformed2.setX(transformed2.x() + offsetX);
        
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
          //rotationXMatrix.setDegree(rotationXMatrix.getDegree() + 0.001);
          //rotationZMatrix.setDegree(rotationZMatrix.getDegree() + 0.002);
        }
      }
    }
    return projectedTriangles;
  }
}
