import java.util.ArrayList;
public class Engine
{
  private Camera camera;
  private double aspectRatio;
  private double fovRadiansHorizontal, fovRadiansVertical;
  private Matrix4x4 projectionMatrix;
  private RotationMatrix4x4 rotationXMatrix, rotationZMatrix;
  private ArrayList<Mesh> meshes;
  public Engine()
  {
    camera = new Camera();
    projectionMatrix = new Matrix4x4();
    fovRadiansVertical = 1.0 / Math.tan(camera.getVerticalFOV() * 0.5 / 180.0 * 3.14159);
    calculateFOV(1920, 1080); 
    meshes = new ArrayList<Mesh>();

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
  public ArrayList<Triangle> createProjections(double w, double h)
  {
    calculateFOV(w, h);
    ArrayList<Triangle> projectedTriangles = new ArrayList<Triangle>();
    for (Mesh m : meshes)
    {
      for (Triangle triangle : m.getTris())
      {
        Triangle projectedTriangle;
        Vector3D point1 = multiplyMatrixVector(triangle.getVectors()[0], projectionMatrix);    
        Vector3D point2 = multiplyMatrixVector(triangle.getVectors()[1], projectionMatrix);     
        Vector3D point3 = multiplyMatrixVector(triangle.getVectors()[2], projectionMatrix); 
        Vector3D rotated1 = multiplyMatrixVector(point1, rotationXMatrix);
        Vector3D rotated2 = multiplyMatrixVector(point2, rotationXMatrix);
        Vector3D rotated3 = multiplyMatrixVector(point3, rotationXMatrix);
        projectedTriangle = new Triangle(rotated1, rotated2, rotated3);
        projectedTriangles.add(projectedTriangle);
        rotationXMatrix.setDegree(rotationXMatrix.getDegree() + 10);
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

    for (Triangle t : cube.getTris())
    {
      for (Vector3D v : t.getVectors())
      {
        v.setZ(v.z() + 3);
        v.setX(v.x());
      }
    }
    return cube;
  }
}
