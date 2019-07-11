/**
Package Name   : SPL
Program Name  : Formated TextField for date
Program Desc. : Allows only num
				max length 10,
				Inserts '/',
				adds '20' to year digit
				Validates month & day values
				
Author        : AAP
Date          : 20/03/2003
Version       : SPL 1.0

Modificaitons 

Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : SPL 2.0
*/

//package spl;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.JOptionPane;
import java.awt.Color;

public class TxtDate extends TxtLimit implements KeyListener,FocusListener
{
	boolean LM_FLLCL=false;
	boolean LM_FLERR;
	public TxtDate()
	{
		super(10);  // Gives TextField for numeric data upto length 10.
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setDisabledTextColor(Color.gray);
	}
	
	public void keyTyped(KeyEvent L_KE){}
	//public void processKeyEvent(KeyEvent L_KE)
	//{
		
	//}
	
	public void keyPressed(KeyEvent L_KE)
	{
		if(L_KE.isAltDown())
			LM_FLERR=true;
		else
			LM_FLERR=false;
		if(((L_KE.isControlDown()) && (L_KE.getKeyCode()==L_KE.VK_V))||((L_KE.isShiftDown())||(!((L_KE.getKeyCode()>=96&&L_KE.getKeyCode()<=105)||(L_KE.getKeyCode()==110)||(L_KE.getKeyCode()>=48&&L_KE.getKeyCode()<=57)||(L_KE.getKeyCode()==46)))))
		{
			if((L_KE.getKeyCode()!=16)
			   &&(!L_KE.isActionKey())
			   &&(L_KE.getKeyCode()!=17)
			   &&(L_KE.getKeyCode()!=18)
			   &&(L_KE.getKeyCode()!=27)
			   &&(L_KE.getKeyCode()!=8)
			   &&(L_KE.getKeyCode()!=10)
			   &&(L_KE.getKeyCode()!=127))
			{	
				if(this.isEditable())
				{	
					this.setEditable(false);
					this.LM_FLLCL=true;
				}
			}
		}
	}

	public void keyReleased(KeyEvent L_KE)
	{
		if(this.LM_FLLCL)
		{
			this.setEditable(true);
			this.LM_FLLCL=false;
		}
		String s=this.getText().trim();
		if(!(L_KE.getKeyCode()==8||L_KE.getKeyCode()==27))
		{
			if(s.length()==2) //SEPARATOR AFTER DATE
				this.setText(s+"/");
			else if(s.length()==5)
				this.setText(s+"/20"); // SEPARATOR WITH FIRST TWO DIGITS OF YEAR
		}
		if((L_KE.getKeyCode()==46||L_KE.getKeyCode()==110))
		{
			if(s.length()>1)
				this.setText(s.substring(0,s.length()-1));
			}
	}

	public void focusGained(FocusEvent L_FE)
	{LM_FLERR=false;}
	public void focusLost(FocusEvent L_FE)
	{
		if(getText().length()>0)
		{
			String L_STRMSG=vldDATE();
			if(L_STRMSG!=null&&!LM_FLERR)
			{
				LM_FLERR=true;
				JOptionPane.showMessageDialog(null,L_STRMSG,"Invalid Date ..",JOptionPane.ERROR_MESSAGE);
				requestFocus();
			}
		}
	}
	
	public String vldDATE() /** Validates all fields. 
								Can called in validation, by the user also. By default called on FocusLost(). Returns error in String.
	                         */
	{
		String s=this.getText();
		int[] mondays=new int[] {31,28,31,30,31,30,31,31,30,31,30,31};
		try
		{
			if (s.length()<10)
				return("Invalid Date ..");
			else if(s.length()>10)
				setText(getText().substring(0,10));
			else if(s.charAt(2)!='/'||s.charAt(5)!='/')
				return("Invalid Date ..");
			int date=Integer.parseInt(s.substring(0,2));
			int month=Integer.parseInt(s.substring(3,5));
			int year=Integer.parseInt(s.substring(6));
			if(year%4==0)
				mondays[1]=29;
			if(year<1000)
				return("Year should be Four Digit ..");
			else if(month==0||date==0)
				return("Date or Month cannot be zero ..");
			else if(month>12)
				return("Month cannot be greater than 12 ..");
			else if(date>mondays[month-1])
				return("Days for specified month cannot be greater than "+mondays[month-1]+" ..");
		}catch(Exception e)
		{
			return(e.getMessage());
		}
		return null;
	}
}
