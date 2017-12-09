/*
 * File Name : F_C_B_Helper_Write_Get.java
 * 
 * Author : Sreeram Pulavarthi
 * 
 * Date: 12-01-2017
 * 
 * Compiler Used: Java 1.8
 * 
 * Description of File: Maintains the Heart of the PFS
 * 
 * 						Data Structure for holding all the information of file
 * 
 * 
 */

import java.util.ArrayList;

public class DataBlocks {
	
	ArrayList<Integer> ret_curr_blck_stats = new ArrayList<Integer>();
	
	
	
	int posit, freeblocks, file_start_pos,file_end_pos;
	String [] CAG_POOL	= new String[20];
	
	
	public DataBlocks() {
		// TODO Auto-generated method stub
		
		for (int i=0;i<20;i++) 
		
		{
			CAG_POOL[i] ="A";
		}
		
	}
	
	public void Data_Load_Blocks(int Indek,String fname) {
		
		//System.out.println("Called me");
		
		CAG_POOL[Indek] = fname;
		
	}
	
	public ArrayList<Integer> Return_Curr_Block()
	{
		
		if (ret_curr_blck_stats.size()>0)
			ret_curr_blck_stats.clear();
		
		posit=0; freeblocks=0;
		
		for (int i=0;i<20;i++) 
			
		{
		
			// For scanning the current Block's are empty or not
			
			// If empty block's are found return index of CAG POOL
			
			if (CAG_POOL[i] =="A")
			{
				posit = i;
				break;
			}
				
			else // else return emty spaces not available
			{
				posit =-1;
			}
		}
		
		ret_curr_blck_stats.add(posit);
		
		for (int i=0;i<20;i++)
		{
			if (CAG_POOL[i] =="A")
			{
				freeblocks += 1;
			}
		}
		
		ret_curr_blck_stats.add(freeblocks);
		
		//System.out.println(ret_curr_blck_stats.get(0)+" , "+ret_curr_blck_stats.get(1));
		
		return ret_curr_blck_stats;
		
	}
	

	public void Current_Block_Status()
	{
		for (int i=0;i<20;i++) 
			
		{
			//System.out.println("\n Block number :"+ i +" , status of block "+ CAG_POOL[i]);
		}
	}
	
	public void MakeEmpty(int Indek,String fname)
	{
		CAG_POOL[Indek] = fname;
	}
	
	
}
