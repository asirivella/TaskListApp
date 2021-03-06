package console;

/*
 * Built over example at http://www.java2s.com/Code/Java/Swing-JFC/AppendingTextPane.htm
 */

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import console.CustomCaret;

public class JavaConsole extends WindowAdapter implements WindowListener, ActionListener, Runnable
{
	private JFrame frame;
	private JTextArea textArea;
	private Thread reader;
	private Thread reader2;
	private boolean quit;
					
	private final PipedInputStream pin=new PipedInputStream(); 
	private final PipedInputStream pin2=new PipedInputStream();
	private final PipedOutputStream pout3=new PipedOutputStream();
	
	public JavaConsole()
	{
		frame=new JFrame("Java Console");
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize=new Dimension((int)(screenSize.width/2),(int)(screenSize.height/2));
		int x=(int)(frameSize.width/2);
		int y=(int)(frameSize.height/2);
		frame.setBounds(x,y,frameSize.width,frameSize.height);
		
		textArea=new JTextArea();
		textArea.setCaret(new CustomCaret());
		textArea.setBackground(Color.black);
		textArea.setForeground(Color.white);
		textArea.setCaretColor(textArea.getForeground());
		textArea.setFont(new Font("Lucida Sans", Font.BOLD, 14));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(true);
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
		frame.setVisible(true);		
		
		frame.addWindowListener(this);		

		
		try
		{
			PipedOutputStream pout=new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout,true)); 
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+io.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+se.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
	    } 
		
		try 
		{
			PipedOutputStream pout2=new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2,true));
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDERR to this console\n"+io.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDERR to this console\n"+se.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
	    } 		

		try 
		{
			System.setIn(new PipedInputStream(this.pout3));
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDIN to this console\n"+io.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDIN to this console\n"+se.getMessage());
			textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
		
		textArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e)  {
					try { pout3.write(e.getKeyChar()); } catch (IOException ex) {}
			}
		});
		
		quit=false;
				
		reader=new Thread(this);
		reader.setDaemon(true);	
		reader.start();	

		reader2=new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();				
	}
	
	public synchronized void windowClosed(WindowEvent evt)
	{
		quit=true;
		this.notifyAll();
		try { reader.join(1000);pin.close();   } catch (Exception e){}		
		try { reader2.join(1000);pin2.close(); } catch (Exception e){}
		try { pout3.close(); } catch (Exception e){}
		System.exit(0);
	}		
		
	public synchronized void windowClosing(WindowEvent evt)
	{
		frame.setVisible(false);	
		frame.dispose();
	}
	
	public synchronized void actionPerformed(ActionEvent evt)
	{
		this.clear();
	}

	public synchronized void run()
	{
		try
		{			
			while (Thread.currentThread()==reader)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin.available()!=0)
				{
					String input=this.readLine(pin);
					textArea.append(input);
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				if (quit) return;
			}
		
			while (Thread.currentThread()==reader2)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin2.available()!=0)
				{
					String input=this.readLine(pin2);
					textArea.append(input);
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
				if (quit) return;
			}			
		} catch (Exception e)
		{
			textArea.append("\nConsole reports an Internal error.");
			textArea.append("The error is: "+e);
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException
	{
		String input="";
		do
		{
			int available=in.available();
			if (available==0) break;
			byte b[]=new byte[available];
			in.read(b);
			input=input+new String(b,0,b.length);														
		}while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
		return input;
	}	
		
	public static void main(String[] arg)
	{
		new JavaConsole();	
	}
	
	public void clear() {
		textArea.setText("");
	}
	
	public Color getBackground() {
		return textArea.getBackground();
	}

	public void setBackground(Color bg) {
		this.textArea.setBackground(bg);
	}	

	public Color getForeground() {
		return textArea.getForeground();
	}

	public void setForeground(Color fg) {
		this.textArea.setForeground(fg);
		this.textArea.setCaretColor(fg);
	}
	
	public Font getFont() {
		return textArea.getFont();
	}

	public void setFont(Font f) {
		textArea.setFont(f);
	}
	
	public void setIconImage(Image i) {
		frame.setIconImage(i);
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
}