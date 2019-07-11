//package spl;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent ;
import javax.swing.JTextField;
import java.awt.Color;

public class TxtNonNum extends JTextField implements FocusListener, KeyListener 
{
	int size;
	boolean LM_FLLCL=false;
	int flag;
	boolean f=false;
	public TxtNonNum()
	{
		super();
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setDisabledTextColor(Color.gray);
	}
	public void keyPressed(KeyEvent ke)
	{
		if(((ke.isControlDown()) && (ke.getKeyCode()==ke.VK_V))||!((ke.isShiftDown())||(!((ke.getKeyCode()>=96&&ke.getKeyCode()<=105)||(ke.getKeyCode()==110)||(ke.getKeyCode()>=48&&ke.getKeyCode()<=57)||(ke.getKeyCode()==46)))))
		{
			if((ke.getKeyCode()!=16)
			   &&(!ke.isActionKey())
			   &&(ke.getKeyCode()!=17)
			   &&(ke.getKeyCode()!=18)
			   &&(ke.getKeyCode()!=27)
			   &&(ke.getKeyCode()!=8)
			   &&(ke.getKeyCode()!=10)
			   &&(ke.getKeyCode()!=127))
			{	
				if( isEditable())
				{
					 setEditable(false);
					 LM_FLLCL=true;
				}
			}
		}
	}

	public void keyReleased(KeyEvent k)
	{
		if( LM_FLLCL)
		{
			 setEditable(true);
			 LM_FLLCL=false;
		}
	}
	public void keyTyped(KeyEvent k)
	{	}
	public void focusGained(FocusEvent L_FE)
	{
	}
	public void focusLost(FocusEvent L_FE)
	{
		if(LM_FLLCL)
		{
			setEditable(true);
			LM_FLLCL=false;
		}
	}
}