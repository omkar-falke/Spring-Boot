//package spl;
import java.awt.event.*;
import javax.swing.JTextField;
import java.util.StringTokenizer;
import java.math.BigDecimal;
import java.awt.Color;

public class TxtNumLimit extends JTextField implements FocusListener,KeyListener
{
	int size,dcml,frcn,DCML,FRCN,caret;
	boolean LM_FLLCL=false,L_FLNUM=false,KE_FLALT=false;	
	String LM_STRALT,LM_STRSUB,KE_STALT;
	boolean f=false;
	String L_OLDDC="",L_NEWDC="",L_OLDFR="",L_NEWFR="",L_STRTMP="",L_ALTOGN="__~^@�";
	StringTokenizer L_STTKN;
	
	public TxtNumLimit(int s)
	{
		super();
		size=s;
		dcml=size;
		DCML=size;
		frcn=0;
		FRCN=0;
		this.addKeyListener(this);
		this.addFocusListener(this);
		LM_STRALT=L_ALTOGN;
		LM_STRSUB=L_ALTOGN;
		setHorizontalAlignment(JTextField.RIGHT);
		this.setDisabledTextColor(Color.gray);
	}
	
	public TxtNumLimit(double f)
	{
		super();
		frcn=new Double( setFMT(new Double((f%1)*10).toString(),1)).intValue();
		dcml=new Double(f/1).intValue()-frcn;
		size=dcml+frcn+1;
		this.addKeyListener(this);
		this.addFocusListener(this);		
		DCML=dcml;FRCN=frcn;
		LM_STRALT=L_ALTOGN;
		LM_STRSUB=L_ALTOGN;
		setHorizontalAlignment(JTextField.RIGHT);
		this.setDisabledTextColor(Color.black);
	}

	public String setFMT(String LP_STR,int LP_LEN){
		if(LP_STR.trim().length() > 0){
			BigDecimal L_BIGDEC = new BigDecimal(LP_STR);
			try{
				L_BIGDEC = L_BIGDEC.setScale(LP_LEN,BigDecimal.ROUND_HALF_UP);
			}catch(Exception e){
				System.out.println("Error in setFMT : " + e.toString());
			}
			return L_BIGDEC.toString();
		}else
			return " ";
	}
	
	protected void processKeyEvent(KeyEvent ke)
	{
		try{
		int key=ke.getKeyCode();
		L_STRTMP=getText();
		if(L_STRTMP.length()>0&&key!=37&&key!=39&&key!=ke.VK_DELETE)
		{
			if(L_STRTMP.charAt(0)=='-'&&caret==0)
				ke.consume();
		}
		if(ke.getKeyChar()=='-')
		{
			if(getText().length()==0)
			{
				dcml=DCML+1;
				size=dcml+frcn+1;
			}
			else if(getCaretPosition()==0&&getText().charAt(0)!='-')
			{
				dcml=DCML+1;
				size=dcml+frcn+1;
			}
			else 
			{
				ke.consume();
			}
		}
		super.processKeyEvent(ke);
	}
		catch(Exception e)
		{System.out.println("process"+e);}
	}
	
	public void keyReleased(KeyEvent ke)
	{
		try
		{
			caret=getCaretPosition();
			L_STRTMP=getText();
			if(LM_FLLCL&&!ke.isAltDown())
			{
				setEditable(true);
				LM_FLLCL=false;
			}
			else if(L_STRTMP.length()>size)
			{
				setText(L_STRTMP.substring(0,size));
				caret--;
			}
			if(ke.getKeyCode()==ke.VK_BACK_SPACE||ke.getKeyCode()==ke.VK_DELETE)
			{
				if(L_STRTMP.length()==0)
				{
					dcml=DCML;
					frcn=FRCN;
					size=dcml+frcn+1;
				}
				else if(L_STRTMP.charAt(0)!='-')
				{
					dcml=DCML;
					frcn=FRCN;
					size=dcml+frcn+1;
				}
			}
			if(L_STRTMP.length()>0)
			{
				if(L_STRTMP.charAt(0)=='.')
				{
					setText("0"+L_STRTMP);
					//caret++;
				}
				else if(L_STRTMP.length()>1)
				{
					if(L_STRTMP.charAt(0)=='-'&&L_STRTMP.charAt(1)=='.')
					setText("-0"+L_STRTMP.substring(1,L_STRTMP.length()));
					//caret++;
				}
			}
				if(L_FLNUM)
				{
					L_STRTMP=getText();
					L_STTKN=new StringTokenizer(L_STRTMP,".");
					if(L_STTKN.hasMoreTokens())
						L_NEWDC=L_STTKN.nextToken();
					else
						L_NEWDC="";
					if(L_STTKN.hasMoreTokens())
					   L_NEWFR=L_STTKN.nextToken();
					else
						L_NEWFR="";
					L_FLNUM=false;
					boolean fl=false;
					if(L_OLDDC.length()>dcml)
					{
						L_OLDDC=L_OLDDC.substring(0,dcml);
						fl=true;
					}
					if(L_OLDFR.length()>frcn)
					{
						L_OLDFR=L_OLDFR.substring(0,frcn);
						fl=true;
					}
					if(L_NEWDC.length()>dcml)
					{
						L_NEWDC=L_OLDDC;
						fl=true;
					}
					if(L_NEWFR.length()>frcn)
					{
						L_NEWFR=L_OLDFR;
						fl=true;
					}
					if(fl)
					{
						setText(L_NEWFR.length()>0 ? (L_NEWDC+"."+L_NEWFR) : L_NEWDC);
						caret--;
					}
				}
			if(getText().length()>size)
			{
				setText(getText().substring(0,size));
				caret--;
			}
			if(ke.getKeyChar()=='.')
			{
				boolean fl=false;
				L_STRTMP=getText();
				L_STTKN=new StringTokenizer(L_STRTMP,".");
				if(L_STTKN.hasMoreTokens())
					L_OLDDC=L_STTKN.nextToken();
				else
					L_OLDDC="";
				if(L_STTKN.hasMoreTokens())
				   L_OLDFR=L_STTKN.nextToken();
				else
					L_OLDFR="";
				
				if(L_OLDDC.length()>dcml)
				{
					L_OLDDC=L_OLDDC.substring(0,dcml);
					fl=true;
				}
				if(L_OLDFR.length()>frcn)
				{
					L_OLDFR=L_OLDFR.substring(0,frcn);
					fl=true;
				}
				if(fl)
				{
					setText(L_OLDFR.length()>0 ? (L_OLDDC+"."+L_OLDFR) : L_OLDDC);
					caret--;
				}
			}
			if(ke.getKeyChar()!='.')
			{
			L_STRTMP=getText();
			StringTokenizer L_STTKN=new StringTokenizer(L_STRTMP,".");
			String st1="",st2="";
			if(L_STTKN.hasMoreTokens())
				st1=L_STTKN.nextToken();
			if(L_STTKN.hasMoreTokens())
				st2=L_STTKN.nextToken();
			if(st1.length()>dcml)
				st1=st1.substring(0,dcml);
			
			if(st2.length()>frcn)
				st2=st2.substring(0,frcn);
			setText(st2.length()>0 ? (st1+"."+st2) : st1);}
			setCaretPosition(caret);
		}catch(Exception e)
		{System.out.println("rel "+e);}
		}

		public void keyPressed(KeyEvent ke)
		{
			try{
			int key=ke.getKeyCode();
			caret=getCaretPosition();
			if(key==ke.VK_ALT)
			{
				setEditable(false);
				LM_FLLCL=true;
				if(!LM_STRALT.equals(L_ALTOGN))
					setText(LM_STRALT);
				else
					LM_STRALT=getText();
				
			}
			else if(!LM_STRALT.equals(L_ALTOGN)&&!ke.isAltDown())
			{
				if(!LM_STRALT.equals(getText()))
				{
				   setText(LM_STRALT);
				}
				
				LM_STRALT=L_ALTOGN;
			}
			if(((ke.isControlDown()) && (key==ke.VK_V))||((ke.isShiftDown())||(!((key>=96&&key<=105)||(key==110)||(key>=48&&key<=57)||(key==46)))))
			{
				if(((key!=16)
				   &&(!ke.isActionKey())
				   &&(key!=17)
				   &&(key!=18)
				   &&(key!=27)
				   &&(key!=8)
				   &&(key!=ke.VK_SUBTRACT)
				   &&(key!=10)
				   &&(key!=127))||ke.isAltDown())
				{	
					if(isEditable())
					{
						setEditable(false);
						LM_FLLCL=true;
					}
				}
			}
			if(ke.getKeyChar()=='.') 
				{
					String s=getText();
					if(s.length()>0)
					{
						for (int i=0;i<s.length();i++)
						{
							if(s.charAt(i)=='.')
							{
								if(isEditable())
								{
									setEditable(false);
									LM_FLLCL=true;
									break;
								}
							}
						}
					}
					boolean fl=false;
					L_STRTMP=getText();
					L_STTKN=new StringTokenizer(L_STRTMP,".");
					if(L_STTKN.hasMoreTokens())
						L_OLDDC=L_STTKN.nextToken();
					else
						L_OLDDC="";
					if(L_STTKN.hasMoreTokens())
					   L_OLDFR=L_STTKN.nextToken();
					else
						L_OLDFR="";
					if(L_OLDDC.length()>dcml)
					{
						L_OLDDC=L_OLDDC.substring(0,dcml);
						fl=true;
					}
					if(L_OLDFR.length()>frcn)
					{
						L_OLDFR=L_OLDFR.substring(0,frcn);
						fl=true;
					}
					if(fl)
					{
						setText(L_OLDFR.length()>0 ? (L_OLDDC+"."+L_OLDFR) : L_OLDDC);
					}
				}
			L_OLDDC="";
			L_NEWDC="";L_OLDFR="";L_NEWFR="";L_STRTMP="";
			if(((key>=96&&key<=105)||(key==110)||(key>=48&&key<=57)||(key==46))&&((key!=37&&key!=39&&key!=8&&key!=127)))
			{
				L_STRTMP=getText();
				L_STTKN=new StringTokenizer(L_STRTMP,".");
				if(L_STTKN.hasMoreTokens())
					L_OLDDC=L_STTKN.nextToken();
				else
					L_OLDDC="";
				if(L_STTKN.hasMoreTokens())
				   L_OLDFR=L_STTKN.nextToken();
				else
					L_OLDFR="";
				boolean fl=false;
				if(L_OLDDC.length()>dcml)
				{
					L_OLDDC=L_OLDDC.substring(0,dcml);
					fl=true;
				}
				if(L_OLDFR.length()>frcn)
				{
					L_OLDFR=L_OLDFR.substring(0,frcn);
					fl=true;
				}
				if(fl)
				{
					if(hasFocus())
					setText(L_OLDFR.length()>0 ? (L_OLDDC+"."+L_OLDFR) : L_OLDDC);
				}
				L_FLNUM=true;
				if(getText().length()==size)
				{
					setText(getText().substring(0,size));
				}
			}
		}catch(Exception e)
		{System.out.println("TxtNumLimit pressed "+e);}
		}
		
	public void focusLost(FocusEvent L_FE)
	{
		if(!LM_STRALT.equals(L_ALTOGN))
		{
			if(!LM_STRALT.equals(getText()))
			{
			   setText(LM_STRALT);
			}
			LM_STRALT=L_ALTOGN;
		}
		boolean fl=false;
		L_STRTMP=getText();
		L_STTKN=new StringTokenizer(L_STRTMP,".");
		if(L_STTKN.hasMoreTokens())
			L_OLDDC=L_STTKN.nextToken();
		else
			L_OLDDC="";
		if(L_STTKN.hasMoreTokens())
		   L_OLDFR=L_STTKN.nextToken();
		else
			L_OLDFR="";
		if(L_OLDDC.length()>dcml)
		{
			L_OLDDC=L_OLDDC.substring(0,dcml);
			fl=true;
		}
		if(L_OLDFR.length()>frcn)
		{
			L_OLDFR=L_OLDFR.substring(0,frcn);
			fl=true;
		}
		else if(L_OLDFR.length()<frcn&&L_OLDDC.length()>0)
		{
			for(int i=L_OLDFR.length();i<frcn;i++)
			{
				L_OLDFR=L_OLDFR+"0";
				fl=true;
			}
		}
		if(fl)
		{
			setText(L_OLDFR.length()>0 ? (L_OLDDC+"."+L_OLDFR) : L_OLDDC);
		}
		if(LM_FLLCL)
		setEditable(true);
	}
	public void focusGained(FocusEvent L_FE)
	{}
	public void keyTyped(KeyEvent L_FE)
	{
	}
}
