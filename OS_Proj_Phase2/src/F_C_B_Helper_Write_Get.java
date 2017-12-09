/*
 * File Name : F_C_B_Helper_Write_Get.java
 * 
 * Author : Sreeram Pulavarthi
 * 
 * Date: 12-01-2017
 * 
 * Compiler Used: Java 1.8
 * 
 * Description of File: Main controller for the Write and Read operations
 * 
 * 						Creates New a New PFS file if the Size of 10KB but with free data.
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class F_C_B_Helper_Write_Get {
	
	private int FILE_OFFSET_START, FILE_OFFSET_END, curr_leng=0,prev_leng=0;
	String Read_File_Name,Write_File_Name, ReadData="", rrd,nul="X";
	File PFSRDFILE,PFSWRTFILE; 
	OutputStream OS,NOS;
	final int EOF = -1; 
	public void F_C_B_Helper_Write(String Read_F_Nam,String Wrt_F_Nam, int FIL_OFF_STA) throws IOException
	{
		this.Read_File_Name = "C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" +Read_F_Nam;
		this.Write_File_Name = "C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" +Wrt_F_Nam;
		//this.FILE_OFFSET_END = FIL_OFF_END; int FIL_OFF_END
		this.FILE_OFFSET_START = FIL_OFF_STA;
		
		PFSRDFILE = new File(Read_File_Name);
		
		PFSWRTFILE = new File(Write_File_Name);
		
		if (PFSWRTFILE.exists()) {
			
			try {
				
				//curr_leng = 0;
				
				FileReader freader = new FileReader(PFSRDFILE);  
				
				BufferedReader br = new BufferedReader(freader);
				
				 try {
					while ( (rrd = br.readLine()) != null)  

					 {
						//System.out.println(rrd);
						ReadData += rrd;
						
//						curr_lng+= this.ReadData.length()
					 }
					
					curr_leng += this.ReadData.length();
					
					br.close();
					
					OS = new FileOutputStream(PFSWRTFILE,true);
					
					OS.write(ReadData.getBytes());
					
					this.ReadData="";
					
					OS.close();	
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}} 
			
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else {
			
			
			PFSWRTFILE.createNewFile();
			
			OS = new FileOutputStream(PFSWRTFILE);
			
			//OS.write(nul.getBytes());
			
			//OS.close();
			try {
			
				FileReader freader = new FileReader(PFSRDFILE);  
				
				BufferedReader br = new BufferedReader(freader);
				
				 try {
					while ( (rrd = br.readLine()) != null)  

					 {
						//System.out.println(rrd);
						this.ReadData += rrd;
					 }
					br.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}} 
			
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			write();
		}
		
		//System.out.println("Read data = "+ReadData);
	}

	public void write() throws IOException
	{
		curr_leng=0;
		
		NOS = new FileOutputStream(PFSWRTFILE,true);
		
		//System.out.format("%s,%d,%d",ReadData, this.FILE_OFFSET_START, this.FILE_OFFSET_END);
		
		NOS.write(this.ReadData.getBytes());
		
		curr_leng = this.ReadData.length();
		
		this.ReadData="";
		
		NOS.close();
	}
	
	public int Return_Stats()
	{
		
		System.out.println("Current updated length= "+this.curr_leng);
	
		return this.curr_leng;
	
	}
	
	public void UPDATEOFF(int FIL_OFF_STA)
	{
		
		this.curr_leng = FIL_OFF_STA;
		
	}
	
}
