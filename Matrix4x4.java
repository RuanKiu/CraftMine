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
  public static  Matrix4x4 makeIdenityMatrix()
  {
    Matrix4x4 m = new Matrix4x4();
    m.setValue(0, 0, 1); 
    m.setValue(1, 1, 1); 
    m.setValue(2, 2, 1); 
    m.setValue(3, 3, 1); 
    return m;
  }
  public static void multiplyMatrixVector(Vector3D output, Vector3D vector, Matrix4x4 matrix)
  {
    output.setX(vector.x() * matrix.getValue(0, 0) + vector.y() * matrix.getValue(1, 0) + vector.z() * matrix.getValue(2, 0) + vector.w() * matrix.getValue(3, 0)); 
    output.setY(vector.x() * matrix.getValue(0, 1) + vector.y() * matrix.getValue(1, 1) + vector.z() * matrix.getValue(2, 1) + vector.w() * matrix.getValue(3, 1)); 
    output.setZ(vector.x() * matrix.getValue(0, 2) + vector.y() * matrix.getValue(1, 2) + vector.z() * matrix.getValue(2, 2) + vector.w() * matrix.getValue(3, 2)); 
    output.setW(vector.x() * matrix.getValue(0, 3) + vector.y() * matrix.getValue(1, 3) + vector.z() * matrix.getValue(2, 3) + vector.w() * matrix.getValue(3, 3)); 
    if (output.w() != 0) {
      output.setX(output.x() / output.w());
      output.setY(output.y() / output.w());
      output.setZ(output.z() / output.w());
    }

  }  
  public static Vector3D multiplyMatrixVector(Vector3D vector, Matrix4x4 matrix)
  {
    Vector3D output = new Vector3D();
    output.setX(vector.x() * matrix.getValue(0, 0) + vector.y() * matrix.getValue(1, 0) + vector.z() * matrix.getValue(2, 0) + vector.w() * matrix.getValue(3, 0)); 
    output.setY(vector.x() * matrix.getValue(0, 1) + vector.y() * matrix.getValue(1, 1) + vector.z() * matrix.getValue(2, 1) + vector.w() * matrix.getValue(3, 1)); 
    output.setZ(vector.x() * matrix.getValue(0, 2) + vector.y() * matrix.getValue(1, 2) + vector.z() * matrix.getValue(2, 2) + vector.w() * matrix.getValue(3, 2)); 
    output.setW(vector.x() * matrix.getValue(0, 3) + vector.y() * matrix.getValue(1, 3) + vector.z() * matrix.getValue(2, 3) + vector.w() * matrix.getValue(3, 3)); 
    if (output.w() != 0) {
      output.setX(output.x() / output.w());
      output.setY(output.y() / output.w());
      output.setZ(output.z() / output.w());
    }

    return output;
  }

  public static Matrix4x4 multiplyMatrix(Matrix4x4 m1, Matrix4x4 m2)
  {
    Matrix4x4 m = new Matrix4x4();
    for (int r = 0; r < 4; r++) {
      for (int c = 0; c < 4; c++) {
        m.setValue(r, c, m1.getValue(r, 0) * m2.getValue(0, c) + m1.getValue(r, 1) * m2.getValue(1, c) + m1.getValue(r, 2) * m2.getValue(2, c) + m1.getValue(r, 3) * m2.getValue(3, c));
      }
    }
    return m;
  }
  public static Matrix4x4 makeRotationMatrix(String axis, double degree)
  {
    degree %= 360;
    Matrix4x4 m = new Matrix4x4();
    switch (axis)
    {
      case "z":
        m.setValue(0, 0, Math.cos(Math.toRadians(degree)));
        m.setValue(0, 1, Math.sin(Math.toRadians(degree)));
        m.setValue(1, 0, -1 * Math.sin(Math.toRadians(degree)));
        m.setValue(1, 1, Math.cos(Math.toRadians(degree)));
        m.setValue(2, 2, 1);
        m.setValue(3, 3, 1);
        break;
      case "x":
        m.setValue(0, 0, 1);
        m.setValue(1, 1, Math.cos(Math.toRadians(degree)));
        m.setValue(1, 2, Math.sin(Math.toRadians(degree)));
        m.setValue(2, 1, -1 * Math.sin(Math.toRadians(degree)));
        m.setValue(2, 2, Math.cos(Math.toRadians(degree)));
        m.setValue(3, 3, 1);
        break;
      case "y":
        m.setValue(0, 0, Math.cos(Math.toRadians(degree)));
        m.setValue(0, 2, Math.sin(Math.toRadians(degree)));
        m.setValue(2, 0, -1 * Math.sin(Math.toRadians(degree)));
        m.setValue(1, 1, 1);
        m.setValue(2, 2, Math.cos(Math.toRadians(degree)));
        m.setValue(3, 3, 1);
        break;

    }
    return m;
  }
  public static Matrix4x4 makeTranslationMatrix(double x, double y, double z)
  {
    Matrix4x4 m = new Matrix4x4();
    m.setValue(0, 0, 1);
    m.setValue(1, 1, 1);
    m.setValue(2, 2, 1);
    m.setValue(3, 0, x);
    m.setValue(3, 1, y);
    m.setValue(3, 2, z);
    return m;
  }
  public static Matrix4x4 makeProjectionMatrix(Camera camera, double w, double h)
  {
    Matrix4x4 m = new Matrix4x4();
    double verticalFOV = 1 / Math.tan(Math.atan(Math.tan(Math.toRadians(camera.getVerticalFOV()/2)) * w/h)); 
    double horizontalFOV = 1 / Math.tan(verticalFOV);
    m.setValue(0, 0, horizontalFOV);
    m.setValue(1, 1, -1 * verticalFOV);
    m.setValue(2, 2, camera.getFar() / (camera.getFar() - camera.getNear()));
    m.setValue(3, 2, (-camera.getFar() * camera.getNear()) / (camera.getFar() - camera.getNear()));
    m.setValue(2, 3, 1);
    m.setValue(3, 3, 0);
    return m;
  }
  public String toString()
  {
    String output = "";
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        output+=" " + this.getValue(i, j);
      }
      output+="\n";
    }
    return output;
  }
  public Matrix4x4 createLookAtViewTransform(Vector3D v, Vector3D t, Vector3D u)
  {
    Vector3D newForward = new Vector3D();
    Vector3D.subtractVectors(newForward, t, v);
    Vector3D.normalizeVector(newForward, newForward);

    Vector3D a = new Vector3D();
    Vector3D.multiplyVectors(a, newForward, Vector3D.vectorDotProduct(u, newForward));
    Vector3D newUp = new Vector3D();
    Vector3D.subtractVectors(newUp, u, a);
    Vector3D.normalizeVector(newUp, newUp);

    Vector3D newRight = new Vector3D();
    Vector3D.crossProduct(newRight, newUp, newForward);

    Matrix4x4 matrix = new Matrix4x4();
    matrix.setValue(0, 0, newRight.x()); matrix.setValue(0, 1, newRight.y()); matrix.setValue(0, 2, newRight.z());
    matrix.setValue(1, 0, newUp.x()); matrix.setValue(1, 1, newUp.y()); matrix.setValue(1, 2, newUp.z());
    matrix.setValue(2, 0, newForward.x()); matrix.setValue(2, 1, newForward.y()); matrix.setValue(2, 2, newForward.z());
    matrix.setValue(3, 0, v.x()); matrix.setValue(3, 1, v.y()); matrix.setValue(3, 2, v.z()); matrix.setValue(3, 3, 1);
    return matrix;
  }
  public Matrix4x4 createFPSViewTransform(Vector3D pos, double pitch, double yaw)
  {
    double cosPitch = Math.cos(Math.toRadians(pitch));
    double sinPitch = Math.sin(Math.toRadians(pitch));
    double cosYaw = Math.cos(Math.toRadians(yaw));
    double sinYaw = Math.cos(Math.toRadians(yaw));

    Matrix4x4 matrix = new Matrix4x4();
    matrix.setValue(0, 0, cosYaw); matrix.setValue(0, 1, sinYaw * sinPitch); matrix.setValue(0, 2, sinYaw * cosPitch);
    matrix.setValue(1, 0, 0); matrix.setValue(1, 1, cosPitch); matrix.setValue(1, 2, -sinPitch);
    matrix.setValue(2, 0, -sinYaw); matrix.setValue(2, 1, sinPitch * cosYaw); matrix.setValue(2, 2, cosPitch * cosYaw);
    matrix.setValue(3, 0, -1 * Vector3D.vectorDotProduct(new Vector3D(cosYaw, 0, -sinYaw), pos));
    matrix.setValue(3, 1, -1 * Vector3D.vectorDotProduct(new Vector3D(sinYaw * sinPitch, cosPitch, cosYaw*cosPitch), pos)); 
    matrix.setValue(3, 2, -1 * Vector3D.vectorDotProduct(new Vector3D(sinYaw*cosPitch, -sinPitch, cosPitch * cosYaw), pos));
    return matrix;
  }
}
