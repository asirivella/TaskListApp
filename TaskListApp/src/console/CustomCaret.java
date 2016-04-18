package console;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class CustomCaret extends DefaultCaret {

	  public CustomCaret() {
	    setBlinkRate(500);
	  }

	  protected synchronized void damage(Rectangle r) {
	    if (r == null) return;
	    x = r.x;
	    y = r.y + (r.height * 4 / 5 - 3);
	    width = 5;
	    height = 5;
	    repaint();
	  }

	  public void paint(Graphics g) {
	    JTextComponent comp = getComponent();
	    if (comp == null)
	      return;

	    int dot = getDot();
	    Rectangle r = null;
	    try {
	      r = comp.modelToView(dot);
	    } catch (BadLocationException e) {
	      return;
	    }
	    if (r == null)
	      return;

	    int dist = r.height * 4 / 5 - 3;

	    if ((x != r.x) || (y != r.y + dist)) {
	      repaint();
	      x = r.x;
	      y = r.y + dist;
	      width = 5;
	      height = 5;
	    }

	    if (isVisible()) {
	      g.setColor(comp.getCaretColor());
	      g.drawLine(r.x, r.y + dist, r.x, r.y + dist + 4);
	      g.drawLine(r.x, r.y + dist + 4, r.x + 4, r.y + dist + 4);
	    }
	  }

	  public static void main(String args[]) {
	    JFrame frame = new JFrame("CornerCaret demo");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JTextArea area = new JTextArea(8, 32);
	    area.setCaret(new CustomCaret());
	    area.setText("This is the test caret\n just for trial to simulate a jframe window.");
	    frame.getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
	    frame.pack();
	    frame.setVisible(true);
	  }
}