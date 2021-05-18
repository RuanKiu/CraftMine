import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.util.ArrayList;

public class CustomPanel extends JPanel implements ActionListener
{
  private Timer time;
  private Engine engine;
  public CustomPanel()
  {
    setVisible(true);
    setSize(1000, 1000);
    time = new Timer(15, this);
    engine = new Engine();
    time.start();
  }

  public void actionPerformed(ActionEvent e)
  {
    repaint();
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    ArrayList<Triangle> triangles = engine.createProjections(getWidth(), getHeight());
    for (Triangle triangle : triangles)
    {
      paintTriangle(g, triangle);
    }
  }
  public void paintTriangle(Graphics g, Triangle triangle)
  {
    double x1 = triangle.getVectors()[0].x();
    double y1 = triangle.getVectors()[0].y();
    double x2 = triangle.getVectors()[1].x();
    double y2 = triangle.getVectors()[1].y();
    double x3 = triangle.getVectors()[2].x();
    double y3 = triangle.getVectors()[2].y();

    x1 += 1.0; y1 += 1.0; x2 += 1.0; y2 += 1.0; x3 += 1.0; y3 += 1.0;
    x1 *= getWidth() * 0.5; y1 *= 0.5 *  getHeight(); x2 *= getWidth() * 0.5; y2 *= 0.5 * getHeight(); x3 *= getWidth() * 0.5; y3 *= 0.5 *  getHeight();

    g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    g.drawLine((int)x2, (int)y2, (int)x3, (int)y3);
    g.drawLine((int)x3, (int)y3, (int)x1, (int)y1);
  }
}
