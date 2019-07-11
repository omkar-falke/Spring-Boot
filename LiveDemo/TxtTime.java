/**
Package Name   : SPL
Program Name  : Formated TextField for date
Program Desc. : Allows only num
				max length 5,
				Inserts ':',
				Validates hour & minute values
				
Author        : AAP
Date          : 13/09/2003
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
import java.util.StringTokenizer;
import java.awt.Color;

public class TxtTime extends TxtNumLimit implements KeyListener,FocusListener
{
	boolean LM_FLLCL=false;
	boolean LM_FLERR;
	String text;
	public TxtTime()
	{
		super(5.0);  // Gives TextField for numeric data upto length 5.
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setDisabledTextColor(Color.gray);
	}
	
	public void keyTyped(KeyEvent L_KE)
	{
		super.keyTyped(L_KE);
	}
	public void processKeyEvent(KeyEvent L_KE)
	{
		if(L_KE.getKeyChar()=='-'||L_KE.getKeyChar()=='.')
			L_KE.consume();
		if(getText().length()>5)
			L_KE.consume();
		super.processKeyEvent(L_KE);
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
	}

	public void keyReleased(KeyEvent L_KE)
	{
		super.keyReleased(L_KE);
		
		if(getText().length()>=2 && !L_KE.isActionKey() && L_KE.getKeyCode()!=8 && L_KE.getKeyCode()!=127)
		{
			text=getText();
			for(int i=0;i<text.length();i++)
				if(text.charAt(i)==':')
				   return;
			StringBuffer L_stbTEMP=new StringBuffer(text);
			L_stbTEMP.insert(2,":");
			setText(L_stbTEMP.toString());
		}
	}

	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		LM_FLERR=false;
	}
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
		if(getText().length()>0)
		{
			String L_STRMSG=vldTIME();
			if(L_STRMSG!=null&&!LM_FLERR)
			{
				LM_FLERR=true;
				JOptionPane.showMessageDialog(this,L_STRMSG,"Invalid Time ..",JOptionPane.ERROR_MESSAGE);
				requestFocus();
			}
		}
	}
	/** Validates all fields<br> 
	* Can be called in validation, by the user also<br> By default called on FocusLost()<br> Returns error in String*/
	public String vldTIME() 
	{
		text=this.getText();
		try
		{
			if(text.length()==4)
			{
				try
				{
					int i=Integer.parseInt(text);
					text=text.substring(0,2)+":"+text.substring(2);
					setText(text);
					return null;
					
				}catch(NumberFormatException L_E)
				{
					return ("Invalid Time ..");
				}
			}
			else  if (text.length()!=5)
				return("Invalid Time ..");
			else if(text.charAt(2)!=':')
				return("Invalid Time ..");
			StringTokenizer L_STRTKN=new StringTokenizer(text,":");
			if(L_STRTKN.countTokens()!=2)
				return ("More no. of ':'");
			int hour=Integer.parseInt(text.substring(0,2));
			int minute=Integer.parseInt(text.substring(3,5));
			if(hour==24)
				return("Hours cannot be 24 ..\nFor mid-night, enter 00:01 or 23:59");
			if(hour>23)
				return("Hours cannot be more than 23 ..");
			else if(minute>59)
				return("Minutes cannot be greater than 59 ..");
			if(hour==0&&minute==0)
				return("Time cannot be 00:00 ..\nEnter 23:59 or 00:01 of next date.");
		}catch(Exception e)
		{
			return(e.getMessage());
		}
		return null;
	}
}
