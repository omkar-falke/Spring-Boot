//package spl;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JTextField;
import java.awt.Color;

public class TxtNum extends JTextField 
{
	int size;
	boolean LM_FLLCL=false;
	public TxtNum()
	{
		super();
		this.addKeyListener(new handlernum(this));
		this.setDisabledTextColor(Color.gray);
	}
}

class handlernum extends KeyAdapter 
{
	TxtNum t;
	boolean f=false;
	
	handlernum(TxtNum t)
	{
		this.t=t;
	}
	public void keyPressed(KeyEvent ke)
	{
		if(((ke.isControlDown()) && (ke.getKeyCode()==ke.VK_V))||((ke.isShiftDown())||(!((ke.getKeyCode()>=96&&ke.getKeyCode()<=105)||(ke.getKeyCode()==110)||(ke.getKeyCode()>=48&&ke.getKeyCode()<=57)||(ke.getKeyCode()==46)))))
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
				if(this.t.isEditable())
				{
					this.t.setEditable(false);
					this.t.LM_FLLCL =true;
				}
			}
		}else if(ke.getKeyCode() ==110||ke.getKeyCode() ==46 ) 
		{
			String s=t.getText();
			if(f)
			{
				f=false;
				for (int i=0;i<s.length();i++)
				{
					if(s.charAt(i)=='.')
						f=true;
				}
				if(f)
				{
					if(this.t.isEditable())
					{
						this.t.setEditable(false);
						this.t.LM_FLLCL =true;
					}
				}
			}
			f=true;
		}
	}

	public void keyReleased(KeyEvent k)
	{
		if(this.t.LM_FLLCL)
		{
			this.t.setEditable(true);
			this.t.LM_FLLCL=false;
		}
	}
}