import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

public class CustomPanel extends JPanel implements ActionListener
{
  private Timer time;
  private Engine engine;
  private BasicStroke stroke;
  public CustomPanel()
  {
    setVisible(true);
    setSize(1000, 1000);
    time = new Timer(10, this);
    engine = new Engine();
    stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    time.start();
  }

  public void actionPerformed(ActionEvent e)
  {
    repaint();
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(stroke);
    ArrayList<ShadedTriangle> triangles = engine.createProjections(getWidth(), getHeight());
    for (ShadedTriangle triangle : triangles)
    {
      paintTriangle(g2, triangle);
    }
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
    g2.setColor(Color.BLACK);
    g2.drawPolygon(p);
  }
}
