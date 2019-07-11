//package spl;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent ;
import javax.swing.JTextField;
import java.awt.Color;

public class TxtLimit extends JTextField implements KeyListener, FocusListener
{
	int size;
	boolean LM_FLLCL=false,LM_BKSFL=false;
	public TxtLimit(int s)
	{
		super();
		size=s;
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setDisabledTextColor(Color.gray);
	}

	protected void processKeyEvent(KeyEvent ke)
	{
		try{
		int key=ke.getKeyCode();
		if(key==8)
			LM_BKSFL=!LM_BKSFL;
		if(key!=27)
		{
			if(getText().length()>=size
				&&key!=8&&key!=9&&key!=10&&key!=16&&key!=18&&key!=19&&key!=27&&key!=127
			    &&key!=155)
			{
				if(key>40||key<33)
				{
					if(key==0)
					{
						if(!LM_BKSFL)
						{
							ke.consume();
						}
						//else 
							//LM_BKSFL=false;	
					}
					else
					{
						ke.consume();
					}
				}
			}
		}
		super.processKeyEvent(ke);
	}
		catch(Exception e)
		{System.out.println("TxtLimit processKeyEvent "+e);}
	}

	
	public void keyReleased(KeyEvent ke)
	{
		if( LM_FLLCL)
		{
			 setEditable(true);
			 LM_FLLCL=false;
		}
		if( getText().length()> size)
		{
			 setText( getText().substring(0, size));
		}
	}
	public void keyPressed(KeyEvent ke)
	{
	}
	public void keyTyped(KeyEvent ke)
	{
	}
	public void focusGained(FocusEvent L_FE)
	{
	}
	public void focusLost(FocusEvent L_FE)
	{
		if( getText().length()> size)
		{
			 setText( getText().substring(0, size));
		}
		if(LM_FLLCL)
		{
			setEditable(true);
			LM_FLLCL=false;
		}
	}
}