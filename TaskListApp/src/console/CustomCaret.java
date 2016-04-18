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
	    y = r.y;
	    height = r.height;
		if (width <= 0) width = getComponent().getWidth();
	    repaint();
	    repaint();
	  }

	  public void paint(Graphics g) {
	    JTextComponent comp = getComponent();
	    if (comp == null)
	      return;

	    int dot = getDot();
	    Rectangle r = null;
	    char dotChar;
	    try {
	      r = comp.modelToView(dot);
	      if (r == null) return;
	      dotChar = comp.getText(dot, 1).charAt(0);
	    } catch (BadLocationException e) {
	      return;
	    }
	    if(Character.isWhitespace(dotChar)) dotChar = '_';

		if ((x != r.x) || (y != r.y)) {
			damage(r);
			return;
		}

		g.setColor(comp.getCaretColor());
		g.setXORMode(comp.getBackground());

		width = g.getFontMetrics().charWidth(dotChar);
		if (isVisible()) g.fillRect(r.x, r.y, width, r.height);
		
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