
package backup;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;

public class KGlassPane extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{

public KGlassPane(){
    setBorder(null);
    setOpaque(false);
	addMouseListener(this);
	//addActionListener(this);
	addMouseMotionListener(this);
}

int progress=1;
int x=0;
int y=0;
int h=10;
int w=0;


@Override
protected void paintComponent(Graphics g) {
	Rectangle rect = g.getClipBounds();
	Graphics2D g2 = (Graphics2D) g;
	Color c = new Color(1.0f, 1.0f, 1.0f, 0.7f);
	g2.setColor(c);
	g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height ,(rect.width*5)/100, (rect.height*5)/100);
	
	super.paintComponent(g);
}

public void setProgress(int progress) {

}


    public void mouseExited(MouseEvent e){
	}

	public void mouseEntered(MouseEvent e){	
	}

	public void mouseClicked(MouseEvent e){
	}

	public void mouseReleased(MouseEvent e){}

    public void mousePressed(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}
    
    public void mouseDragged(MouseEvent e) {}

    public void actionPerformed(ActionEvent e){}
	
	public void keyReleased(java.awt.event.KeyEvent e){}
	
	public void keyPressed(java.awt.event.KeyEvent e){}
	
	public void keyTyped(java.awt.event.KeyEvent e){}
}