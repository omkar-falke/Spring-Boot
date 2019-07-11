import java.io.DataOutputStream;import java.io.FileOutputStream;

public class cl_graphs
{
	public static void exeMUL_BAR_GRAPH(String LP_TITLE_X,String LP_TITLE_Y,String LP_XVAL,int LP_TOTVL,String LP_STR1,String LP_VAL1,String LP_STR2,String LP_VAL2,String LP_STR3,String LP_VAL3,String LP_STR4,String LP_VAL4,String LP_STR5,String LP_VAL5)
	{
		FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
		DataOutputStream D_OUT ;
		
		try
		{
			F_OUT=new FileOutputStream("C:\\reports\\graph.html");
			D_OUT=new DataOutputStream(F_OUT); 
		
			D_OUT.writeBytes("<applet code='swiftchart.class' width='500' height='500'>");
			D_OUT.writeBytes("<param name='chart_type' value='bar'>");
			D_OUT.writeBytes("<param name='x_axis_font_orientation' value='HORIZONTAL'>");
			D_OUT.writeBytes("<param name='applet_bg' value='666699'>");
			D_OUT.writeBytes("<param name='chart_bg' value='CC9900'>");
			D_OUT.writeBytes("<param name='title_text' value='Horizontal bar chart with labels'>");
			D_OUT.writeBytes("<param name='title_font_size' value='18'>");
			D_OUT.writeBytes("<param name='x_axis_font_size' value='12'>");
			D_OUT.writeBytes("<param name='y_axis_font_size' value='12'>");
			D_OUT.writeBytes("<param name='legend_position' value='TOP'>");
			D_OUT.writeBytes("<param name='legend_border_color' value='CCDDFF'>");
			D_OUT.writeBytes("<param name='legend_font_size' value='12'>");
			D_OUT.writeBytes("<param name='data_value' value='NONE'>");
			D_OUT.writeBytes("<param name='data_value_font_color' value='000000'>");
			D_OUT.writeBytes("<param name='data_value_font_size' value='12'>");
			D_OUT.writeBytes("<param name='grid_line_hor' value='N'>");
			D_OUT.writeBytes("<param name='grid_line_ver' value='N'>");
			D_OUT.writeBytes("<param name='x_value' value='"+LP_XVAL+"'>");
			int x = 0;
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s1_value' value='"+LP_VAL1+"'>");
				D_OUT.writeBytes("<param name='s1_label' value='"+LP_STR1+"'>");
				D_OUT.writeBytes("<param name='s1_color' value='993300'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s2_value' value='"+LP_VAL2+"'>");
				D_OUT.writeBytes("<param name='s2_label' value='"+LP_STR2+"'>");
				D_OUT.writeBytes("<param name='s2_color' value='009933'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s3_value' value='"+LP_VAL3+"'>");
				D_OUT.writeBytes("<param name='s3_label' value='"+LP_STR3+"'>");
				D_OUT.writeBytes("<param name='s3_color' value='003399'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s4_value' value='"+LP_VAL4+"'>");
				D_OUT.writeBytes("<param name='s4_label' value='"+LP_STR4+"'>");
				D_OUT.writeBytes("<param name='s4_color' value='003300'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s5_value' value='"+LP_VAL5+"'>");
				D_OUT.writeBytes("<param name='s5_label' value='"+LP_STR5+"'>");
				D_OUT.writeBytes("<param name='s5_color' value='330000'>");
				x++;
			}
			x=0;
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s1_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s2_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s3_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s4_bar_fill' value='3'>");
				x++;
			}
			if(x<LP_TOTVL)
			{
				D_OUT.writeBytes("<param name='s5_bar_fill' value='3'>");
				x++;
			}
			D_OUT.writeBytes("<param name='x_axis_title' value='Y'>");
			D_OUT.writeBytes("<param name='x_axis_title_text' value='"+LP_TITLE_X+"'>");
			D_OUT.writeBytes("<param name='x_axis_title_font_style' value='BOLD'>");
			D_OUT.writeBytes("<param name='y_axis_title' value='Y'>");
			D_OUT.writeBytes("<param name='y_axis_title_text' value='"+LP_TITLE_Y+"'>");
			D_OUT.writeBytes("<param name='y_axis_title_font_style' value='BOLD'>");
			D_OUT.writeBytes("<param name='chart_bg_fill' value='5'>");
			D_OUT.writeBytes("<param name='bg_fill' value='4'>");
			D_OUT.writeBytes("</applet>");
				
			Runtime r = Runtime.getRuntime();
			Process p = null;					    
			p  = r.exec("c:\\windows\\iexplore.exe C:\\reports\\graph.html"); 
		}
		catch(Exception E)
		{
			System.out.println(E);
		}
	}
	
	public static void exeBAR_GRAPH(String LP_TITLE_X,String LP_TITLE_Y,String LP_SUB,int LP_WIDTH,int LP_HEIGHT,String LP_XVAL,String LP_YVAL)
	{
		FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
		DataOutputStream D_OUT ;
		
		try
		{
			F_OUT=new FileOutputStream("C:\\reports\\graph.html");
			D_OUT=new DataOutputStream(F_OUT); 
			
			D_OUT.writeBytes("<applet code='swiftchart.class' width='"+LP_WIDTH+"' height='"+LP_HEIGHT+"'>");
			D_OUT.writeBytes("<param name='x_axis_font_orientation' value='RIGHT'>");
			D_OUT.writeBytes("<param name='chart_type' value='bar'>");
			D_OUT.writeBytes("<param name='applet_bg' value='EEEEEE'>");
			D_OUT.writeBytes("<param name='chart_bg' value='66CCFF'>");
			D_OUT.writeBytes("<param name='title_text' value='Bar chart with labels'>");
			D_OUT.writeBytes("<param name='title_font_size' value='18'>");
			D_OUT.writeBytes("<param name='title_sub1_text' value='"+LP_SUB+"'>");
			D_OUT.writeBytes("<param name='legend_position' value='NONE'>");
			D_OUT.writeBytes("<param name='data_value_font_size' value='10'>");
			D_OUT.writeBytes("<param name='data_value_font_orientation' value='right'>");
			D_OUT.writeBytes("<param name='data_value' value='inside'>");
			D_OUT.writeBytes("<param name='data_value_font_color' value='990000'>");
			D_OUT.writeBytes("<param name='grid_line_hor' value='Y'>");
			D_OUT.writeBytes("<param name='grid_line_hor_type' value='0'>");
			D_OUT.writeBytes("<param name='x_value' value='"+LP_XVAL+"'>");
			D_OUT.writeBytes("<param name='s1_value' value='"+LP_YVAL+"'>");
			D_OUT.writeBytes("<param name='s1_label' value='Serie 1'>");
			D_OUT.writeBytes("<param name='s1_color' value='FFCC00'>");
			D_OUT.writeBytes("<param name='x_axis_title' value='Y'>");
			D_OUT.writeBytes("<param name='x_axis_title_text' value='"+LP_TITLE_X+"'>");
			D_OUT.writeBytes("<param name='x_axis_title_font_style' value='BOLD'>");
			D_OUT.writeBytes("<param name='y_axis_title' value='Y'>");
			D_OUT.writeBytes("<param name='y_axis_title_text' value='"+LP_TITLE_Y+"'>");
			D_OUT.writeBytes("<param name='y_axis_title_font_style' value='BOLD'>");
			D_OUT.writeBytes("<param name='y_unit' value='500'>");
			D_OUT.writeBytes("/applet>");

				
			Runtime r = Runtime.getRuntime();
			Process p = null;					    
			p  = r.exec("c:\\windows\\iexplore.exe C:\\reports\\graph.html"); 
		}
		catch(Exception E)
		{
			System.out.println(E);
		}
	}
}