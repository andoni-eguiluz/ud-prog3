package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

import static java.lang.Double.NaN;


//
// http://www.java2s.com/Tutorial/Java/Graphics/Shape/Exercise_shape_area_calculation_in_Java.htm
// http://www.acnenomor.com/3150294p1/how-to-calculate-the-area-of-a-javaawtgeomarea
//

public class PruebaDeAreas extends JComponent {
  private Shape mShapeOne, mShapeTwo;

  private JComboBox mOptions;

  public PruebaDeAreas() {
    mShapeOne = new Ellipse2D.Double(40, 20, 80, 80);
    mShapeTwo = new Rectangle2D.Double(60, 40, 80, 80);
    setBackground(Color.white);
    setLayout(new BorderLayout());

    JPanel controls = new JPanel();

    mOptions = new JComboBox(new String[] { "outline", "add", "intersection", "subtract", "exclusive or" });

    mOptions.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        repaint();
      }
    });
    controls.add(mOptions);
    add(controls, BorderLayout.SOUTH);
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    String option = (String) mOptions.getSelectedItem();
    if (option.equals("outline")) {
      // draw the outlines and return.
      g2.draw(mShapeOne);
      g2.draw(mShapeTwo);
      return;
    }

    // Create Areas from the shapes.
    Area areaOne = new Area(mShapeOne);
    Area areaTwo = new Area(mShapeTwo);
    // Combine the Areas according to the selected option.
    if (option.equals("add"))
      areaOne.add(areaTwo);
    else if (option.equals("intersection"))
      areaOne.intersect(areaTwo);
    else if (option.equals("subtract"))
      areaOne.subtract(areaTwo);
    else if (option.equals("exclusive or"))
      areaOne.exclusiveOr(areaTwo);

    // Fill the resulting Area.
    g2.setPaint(Color.orange);
    g2.fill(areaOne);
    // Draw the outline of the resulting Area.
    g2.setPaint(Color.black);
    g2.draw(areaOne);
    
    // Sacar el área calculada
    System.out.println( "Área = " + approxArea( areaOne, 5, 5 ) );
  }
  
  
      public static double approxArea(Area area, double flatness, int limit) {
          PathIterator i = new FlatteningPathIterator(area.getPathIterator(identity), flatness, limit);
          return approxArea(i);
      }

      public static double approxArea(Area area, double flatness) {
          PathIterator i = area.getPathIterator(identity, flatness);
          return approxArea(i);
      }

      public static double approxAreaSinCurvas(Area area) {
          PathIterator i = area.getPathIterator(identity);
          return approxArea(i);
      }

      public static double approxArea(PathIterator i) {
          double a = 0.0;
          double[] coords = new double[6];
          double startX = NaN, startY = NaN;
          Line2D segment = new Line2D.Double(NaN, NaN, NaN, NaN);
          while (! i.isDone()) {
              int segType = i.currentSegment(coords);
              double x = coords[0], y = coords[1];
              switch (segType) {
              case PathIterator.SEG_CLOSE:
                  segment.setLine(segment.getX2(), segment.getY2(), startX, startY);
                  a += hexArea(segment);
                  startX = startY = NaN;
                  segment.setLine(NaN, NaN, NaN, NaN);
                  break;
              case PathIterator.SEG_LINETO:
                  segment.setLine(segment.getX2(), segment.getY2(), x, y);
                  a += hexArea(segment);
                  break;
              case PathIterator.SEG_MOVETO:
                  startX = x;
                  startY = y;
                  segment.setLine(NaN, NaN, x, y);
                  break;
              default:
                  throw new IllegalArgumentException("PathIterator contains curved segments");
              }
              i.next();
          }
          if (Double.isNaN(a)) {
              throw new IllegalArgumentException("PathIterator contains an open path");
          } else {
              return 0.5 * Math.abs(a);
          }
      }

      private static double hexArea(Line2D seg) {
          return seg.getX1() * seg.getY2() - seg.getX2() * seg.getY1();
      }

      private static final AffineTransform identity = AffineTransform.getQuadrantRotateInstance(0);
  
  
  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(new PruebaDeAreas());
    f.setSize(220, 220);
    f.setVisible(true);
  }
 
  
}
