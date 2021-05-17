import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.util.ArrayList;

public class Runner 
{
  public static void main(String[] args)
  {
    JFrame window = new JFrame();
    CustomPanel container = new CustomPanel();
    window.add(container);
    window.setSize(container.getSize());
    window.setVisible(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

class CustomPanel extends JPanel implements ActionListener
{
  private Timer time;
  public CustomPanel()
  {
    setVisible(true);
    setSize(1000, 1000);
    time = new Timer(15, this);
  }

  public void actionPerformed(ActionEvent e)
  {
    repaint();
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
  }
}
class Matrix4x4
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
class Engine
{
  private Camera camera;
  private double aspectRatio;
  private double fovRadians;
  private Matrix4x4 projectionMatrix;
  private ArrayList<Mesh> meshes;
  public Engine()
  {
    Mesh meshCube = createCube(); 
    camera = new Camera();
    projectionMatrix = new Matrix4x4();
    aspectRatio = 1080.0/1920.0;
    fovRadians = 1.0 / Math.tan(camera.getFOV() * 0.5);
    projectionMatrix.setValue(0, 0, aspectRatio * fovRadians);
    projectionMatrix.setValue(1, 1, fovRadians);
    projectionMatrix.setValue(2, 2, camera.getFar() / (camera.getFar() - camera.getNear()));
    projectionMatrix.setValue(3, 2, (-camera.getFar() * camera.getNear()) / (camera.getFar() - camera.getNear()));
    projectionMatrix.setValue(2, 3, 1.0);
    projectionMatrix.setValue(3, 3, 0.0); 
    meshes = new ArrayList<Mesh>();
    meshes.add(meshCube);
  }
  private Vector3D MultiplyMatrixVector(Vector3D vector, Matrix4x4 matrix)
  {
    Vector3D output = new Vector3D();
    output.setX(vector.x() * matrix.getValue(0, 0) + vector.y() * matrix.getValue(1, 0) + vector.z() * matrix.getValue(2, 0) + matrix.getValue(3, 0)); 
    output.setY(vector.x() * matrix.getValue(0, 1) + vector.y() * matrix.getValue(1, 1) + vector.z() * matrix.getValue(2, 1) + matrix.getValue(3, 1)); 
    output.setY(vector.x() * matrix.getValue(0, 2) + vector.y() * matrix.getValue(1, 2) + vector.z() * matrix.getValue(2, 2) + matrix.getValue(3, 2)); 
    double w = vector.x() * matrix.getValue(0, 3) + vector.y() * matrix.getValue(1, 3) + vector.z() * matrix.getValue(2, 3) + matrix.getValue(3, 3);

    if (w != 0) {
      output.setX(output.x()/w);
      output.setY(output.y()/w);
      output.setZ(output.z()/w);
    }
    return output;
  }
  public void render(double w, double h, Graphics g)
  {
    aspectRatio = h/w;
    for (Mesh m : meshes)
    {
      for (Triangle triangle : m.getTris())
      {
        Triangle projectedTriangle;
        Vector3D point1 = MultiplyMatrixVector(triangle.getVectors()[0], projectionMatrix);     
        Vector3D point2 = MultiplyMatrixVector(triangle.getVectors()[1], projectionMatrix);     
        Vector3D point3 = MultiplyMatrixVector(triangle.getVectors()[2], projectionMatrix);     
        projectedTriangle = new Triangle(point1, point2, point3);
      }
    }
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
}
class Camera
{
  private double x;
  private double y;
  private double z;
  private double fov;
  private double near;
  private double far;
  public Camera()
  {
    x = 0.0;
    y = 0.0;
    z = 0.0;
    fov = 90;
    near = 0.1;
    far = 1000.0;
  }
  public double getFOV()
  {
    return fov;
  }
  public double getNear()
  {
    return near;
  }
  public double getFar()
  {
    return far;
  }
}
class Triangle
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
class Mesh
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
class Vector3D
{
  private double x, y, z;
  public Vector3D()
  {
    x = y = z = 0;
  }
  public Vector3D(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  public double x()
  {
    return x;
  }

  public double y()
  {
    return y;
  }

  public double z()
  {
    return z;
  }

  public void setValue(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setX(double x)
  {
    this.x = x;
  }

  public void setY(double y)
  {
    this.y = y;
  }

  public void setZ(double z)
  {
    this.z = z;
  }
}
