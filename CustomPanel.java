import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

public class CustomPanel extends JPanel implements ActionListener, KeyListener, ComponentListener, MouseMotionListener
{
  private Timer time;
  private Engine engine;
  private BasicStroke stroke;
  private boolean wireframe, info;
  private double elapsed, current;
  private int lastX, lastY;
  private boolean forward, backward, left, right, up, down;
  public CustomPanel() 
  {
    setVisible(true);
    setSize(1000, 1000);
    setFocusable(true);
    requestFocus();
    requestFocusInWindow();
    
    addKeyListener(this);
    addComponentListener(this);
    addMouseMotionListener(this);

    time = new Timer(5, this);
    engine = new Engine();
    stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    elapsed = 0;
    current = System.nanoTime();
    forward = backward = left = right = false;
    wireframe = false;
    info = false;
    time.start();
  }

  // Action event handling
  public void actionPerformed(ActionEvent e)
  {
    repaint();
    elapsed = System.nanoTime() - current;
    current = System.nanoTime();
    
    //engine.moveCamera(0, 0.001, 0);
    if (left)
      engine.moveCamera(-0.01, 0, 0);
    if (right)
      engine.moveCamera(0.01, 0, 0); 
    if (down)
      engine.moveCamera(0, -0.01, 0);
    if (up)
      engine.moveCamera(0, 0.01, 0);
  }

  // Key event handling
  public void keyPressed(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_W)
      engine.moveCamera(0, 0, 0.01); 
    else if (e.getKeyCode() == KeyEvent.VK_D)
      right = true;
    else if (e.getKeyCode() == KeyEvent.VK_A)
      left = true;
    else if (e.getKeyCode() == KeyEvent.VK_UP)
      up = true;
    else if (e.getKeyCode() == KeyEvent.VK_DOWN)
      down = true;
    
    switch (e.getKeyCode())
    {
      case KeyEvent.VK_F: wireframe = !wireframe; forward = true; break;
      case KeyEvent.VK_I: info = !info; break;
    }
  }
  public void keyReleased(KeyEvent e) 
  {
    left = false;
    right = false;
    up = false;
    down = false;
  }
  public void keyTyped(KeyEvent e) {}

  // Component event handling
  public void componentResized(ComponentEvent e)
  {
    engine.updateFOV(getWidth(), getHeight());
  }
  public void componentHidden(ComponentEvent e) {}
  public void componentMoved(ComponentEvent e) {}
  public void componentShown(ComponentEvent e) {}

  // Mouse event handling
  public void mouseMoved(MouseEvent e) {}
  public void mouseDragged(MouseEvent e) {}

  // Graphics
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(stroke);
    ArrayList<ShadedTriangle> triangles = engine.createProjections();
    for (ShadedTriangle triangle : triangles)
    {
      paintTriangle(g2, triangle);
    }
    if (info)
      g2.drawString("" + (1 / (elapsed / 1_000_000_000)), 20, 20);
  }
  public void paintTriangle(Graphics2D g2, ShadedTriangle triangle)
  {
    double x1 = triangle.getVectors()[0].x();
    double y1 = triangle.getVectors()[0].y();
    double x2 = triangle.getVectors()[1].x();
    double y2 = triangle.getVectors()[1].y();
    double x3 = triangle.getVectors()[2].x();
    double y3 = triangle.getVectors()[2].y();

    x1 += 1.0; y1 += 1.0; x2 += 1.0; y2 += 1.0; x3 += 1.0; y3 += 1.0;
    x1 *= getWidth() * 0.5; y1 *= 0.5 *  getHeight(); x2 *= getWidth() * 0.5; y2 *= 0.5 * getHeight(); x3 *= getWidth() * 0.5; y3 *= 0.5 *  getHeight();

    Polygon p = new Polygon();
    p.addPoint((int) x1, (int) y1);
    p.addPoint((int) x2, (int) y2);
    p.addPoint((int) x3, (int) y3);
    g2.setColor(new Color((float) triangle.r(), (float) triangle.g(), (float) triangle.b()));
    g2.fillPolygon(p);
    if (wireframe)
      g2.setColor(Color.BLACK);
    g2.drawPolygon(p);
  }
}
