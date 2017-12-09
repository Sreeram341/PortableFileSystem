/*
 * File Name : F_C_B.java
 * 
 * Author : Sreeram Pulavarthi
 * 
 * Date: 12-01-2017
 * 
 * Compiler Used: Java 1.8
 * 
 * Description of File: Main controller for the Operations of the files in windows environment
 * 
 * 
 */

import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class F_C_B {

	// For storing data block information

	static ArrayList<DataBlocks> dbs = new ArrayList<DataBlocks>();

	// To store information recieved from each

	static ArrayList<Integer> get_curr_blck_stats = new ArrayList<Integer>();
	
	// To maintain the file details for each file added into the PFS

	ArrayList<F_C_B_Helper_Write_Get> FCB_W_R = new ArrayList<F_C_B_Helper_Write_Get>();
	
	// Maintains the header information of all files in the FCB
	
	ArrayList<PFS_DIR> Main_Dir_INFO = new ArrayList<PFS_DIR>();

	int blk_num = 0, file_siz = 0, num_of_blocks, curr_block, curr = 0, free_blocks, loop_val;

	File fl, PFSDirfil;

	public String open_file_name, file_name, opn_fil_name, str = " ", AddLine;

	// Reads the data from the file when the FCB exists and creates a new line for every record information
	
	static ArrayList<String> Read_From_file = new ArrayList<String>();

	OutputStream os = null;

	InputStream is = null;

	BufferedWriter writer;

	public String[] CAG_POOL;

	public F_C_B(String fil_name) throws IOException {
		// TODO Auto-generated constructor stub

		opn_fil_name = fil_name;

		open_file_name = "C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + fil_name + ".txt";

		// System.out.println("Message sent from F_C_B ");

		PFSDirfil = new File(open_file_name);

		if (PFSDirfil.exists()) {
			System.out.println("PFS Already exists fetching the details ------->> ");

			/* 
			 * Constructs entire FCB block with all details required for the contagious file structure  
			 * 
			 * Updated the internal file data and FCB's structure and all details handled carefully
			 * 
			 */

			try {

				// Open a Buffer reader connection to get the data from the file which already created

				BufferedReader br = new BufferedReader(new FileReader(PFSDirfil));

				for (String line; (line = br.readLine()) != null;) {

					// process all the line in the file and get the Re-construct the File Control Block

					int curr = 0, prev = 0;

					for (int i = 0; i < 11; i++) {

						prev = curr + 20;

						Read_From_file.add(i, line.substring(curr, prev).trim());

						curr = curr + 20;

					}

					// Load all the values into the Array List of FCB

					PFS_DIR PDIR = new PFS_DIR(Read_From_file.get(0), Integer.parseInt(Read_From_file.get(1)),
							Read_From_file.get(2), Integer.parseInt(Read_From_file.get(3)),
							Integer.parseInt(Read_From_file.get(4)), Integer.parseInt(Read_From_file.get(5)),
							Integer.parseInt(Read_From_file.get(6)), Integer.parseInt(Read_From_file.get(7)),
							Read_From_file.get(8), Read_From_file.get(9), Integer.parseInt(Read_From_file.get(10)));

					Main_Dir_INFO.add(PDIR);

					// Clear the temporary array list created for the file

					Read_From_file.clear();

				}
				
				br.close();
				
				int max = 0,helper_write_max = 0;
				int MAX[];

				for (int i = 0; i < Main_Dir_INFO.size(); i++) {

					// Find the maximum number of Datablocks created in the prior file

					if (Main_Dir_INFO.get(i).BlockClassID > max)
					{
						max = Main_Dir_INFO.get(i).BlockClassID;
						
					}
					
				}
				
				if (max ==0) {
					max =1 ;
				}
				else 
				{
					max =max+1 ;
				}
				
				for (int i = 0; i < max; i++) {
					
					// Create the number of Data blocks and Offset locations after finding the maximum block-id

					DataBlocks dbss = new DataBlocks();

					dbs.add(dbss);
					
					F_C_B_Helper_Write_Get FCBH = new F_C_B_Helper_Write_Get();
					
					FCB_W_R.add(FCBH);
				}
				
				//Construct all classes and add to ArrayLists....
				
				MAX = new int[FCB_W_R.size()];
				helper_write_max= FCB_W_R.size();
				
				
				for (int kk =0;kk<MAX.length;kk++) MAX[kk]=0;
				
				// Finds the Maximum Offset end index for each file and loaded in that particular data block 
				
				for (int i = 0; i < FCB_W_R.size(); i++) {
					
					System.out.println(Main_Dir_INFO.get(0).BlockClassID);
					
					// Find the maximum number of Data blocks created in the prior file
					
					for(int ll = 0;ll < Main_Dir_INFO.size(); ll++) 
					{
						if (Main_Dir_INFO.get(i).BlockClassID == Main_Dir_INFO.get(ll).BlockClassID)
							
						{
							if (Main_Dir_INFO.get(ll).File_Offset_End>MAX[i]) 
							
							{
								MAX[i] = Main_Dir_INFO.get(ll).File_Offset_End;
							}
						}
					}
				}
				
				
				
				//System.out.println(FCB_W_R.size());

				for (int i = 0;i<FCB_W_R.size() ;i++)
				{
					// Update the Maximum Offset for each File read block created					

					//System.out.println(MAX[i]);
					
					FCB_W_R.get(i).UPDATEOFF(MAX[i]);
					
				}
				
				for (int i = 0; i < Main_Dir_INFO.size(); i++)

				{
					for (int j = Main_Dir_INFO.get(i).StartBlock; j < Main_Dir_INFO.get(i).EndBlock; j++) {

						// Re-construct the Data-block file with exact information from the existing PFS

						dbs.get(Main_Dir_INFO.get(i).BlockClassID).Data_Load_Blocks(j, Main_Dir_INFO.get(i).File_Name);

					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else {
			System.out.println("\n Creating a New PFS!");

			try {
				
				// If PFS file doesnt exist and created for first time 

				PFSDirfil.createNewFile();

				os = new FileOutputStream(PFSDirfil);

				os.close();

			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Generate the Data blocks for each file created ie., 40 blocks  
			
			DataBlocks dbss = new DataBlocks();

			dbs.add(dbss);

			loop_val = dbs.size();

			//System.out.println("Size of DBlock = " + loop_val);

		}

	}

	public void Put_File(String fil_name) {

		// Create a file object for each file which needs to be read
		
		file_name = "C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + fil_name + ".txt";

		// System.out.println("Message sent from Put Operation in F_C_B ");

		fl = new File(file_name);

		// If the file mentioned to be copied is present in the directory or not. If exists do below set of operations

		if (fl.exists()) {
			System.out.println("File exists fetching the information into PFS ------->> ");

			try {

				String rrd, ReadData="" ;
				
				loop_val = dbs.size();
				
				FileReader freader = new FileReader(fl);  
				
				BufferedReader br = new BufferedReader(freader);
			
				while ( (rrd = br.readLine()) != null)  

				 {
					//System.out.println(rrd);
					ReadData += rrd;
				 }

				// Read the data entirely from the file and calculate length in bytes
				
				file_siz = ReadData.length();
				
				br.close();

				byte[] byt = new byte[file_siz];

				// Decide the number of Blocks for length of the file and make them occupied.
				
				num_of_blocks = (int) Math.ceil(file_siz / 256);

				if (num_of_blocks > 0) {
					num_of_blocks = num_of_blocks;
				} else {
					num_of_blocks = 1;
				}

				// Check if PFS Directory is created for first time (or) Check if already file exists in the PFS Directory

				if (Main_Dir_INFO.size() > 0) {
					int exist_cntr = 0;
					outerloop: for (int k = 0; k < Main_Dir_INFO.size(); k++) {

						if (Main_Dir_INFO.get(k).File_Name.equals(fil_name + ".txt")) {
							System.out.println(
									"\n***Alert***\tAlready mentioned file present in directory, So file canot be added again ");
							exist_cntr = 1;
							break outerloop;

						} else {
							exist_cntr = 0;
						}

					}

					// If file not present in PFS then create

					if (exist_cntr == 0) {

						for (int i = 0; i < loop_val; i++) {

							// get the number of Data blocks which are free and not occupied  
							
							get_curr_blck_stats = dbs.get(i).Return_Curr_Block();

							curr_block = get_curr_blck_stats.get(0);

							free_blocks = get_curr_blck_stats.get(1);
							
							// Get the off set length for file and set the off set with latest record
							
							int curr_ofst = FCB_W_R.get(i).Return_Stats();

							// Add number of blocks to current blocks and make in-available.
							
							int add_blocks = curr_block + num_of_blocks;

							if (num_of_blocks < free_blocks) {

								for (int j = curr_block; j < add_blocks; j++) {

									dbs.get(i).Data_Load_Blocks(j, fil_name + ".txt");

								}

								Date CreatedDate = new Date();

								String FormattedDate = new SimpleDateFormat("h:mm a MMMMM dd").format(CreatedDate);

								// Construct the Main data structure and intitalize with all variables
								
								PFS_DIR PDIR = new PFS_DIR(fil_name + ".txt", file_siz, opn_fil_name + "-" + i + ".txt",
										curr_block, add_blocks, num_of_blocks, curr_ofst, (curr_ofst+file_siz),
										FormattedDate, "", i);

								// Write the data into the mentioned file from the mentioned position   
								
								FCB_W_R.get(i).F_C_B_Helper_Write(fil_name+ ".txt",opn_fil_name + "-" + i + ".txt",(curr_ofst+file_siz));
								
								PFS_ADD_DIR(fil_name + ".txt", file_siz, opn_fil_name + "-" + i + ".txt", curr_block,
										add_blocks, num_of_blocks, curr_ofst, (curr_ofst+file_siz), FormattedDate,
										"", i);

								Main_Dir_INFO.add(PDIR);

								// if all blocks are written in any of the block then '0' the blocks

								num_of_blocks = 0;
								add_blocks = 0;
								break;

								// dbs.get(i).Current_Block_Status();
							}
						}

						// Create a new Data block with new all free blocks.....
						// Repeat the same set of operations if it needed to be written into a new block

						if (num_of_blocks > 0)

						{
							DataBlocks dbss = new DataBlocks();
							
							dbs.add(dbss);
							
							F_C_B_Helper_Write_Get FCBWR= new F_C_B_Helper_Write_Get(); 
							
							FCB_W_R.add(FCBWR);
							
							// write(dbs.size()-1,0, num_of_blocks, fil_name);

							for (int j = 0; j < num_of_blocks; j++) {
								dbs.get(dbs.size() - 1).Data_Load_Blocks(j, fil_name + ".txt");
							}

							Date CreatedDate = new Date();

							String FormattedDate = new SimpleDateFormat("h:mm a MMMMM dd").format(CreatedDate);
											
							int i = dbs.size() - 1;

							int curr_ofst = FCB_W_R.get(i).Return_Stats();
							
							FCB_W_R.get(i).F_C_B_Helper_Write(fil_name+ ".txt",opn_fil_name + "-" + i + ".txt",(file_siz));	
							
							PFS_DIR PDIR = new PFS_DIR(fil_name + ".txt", file_siz, opn_fil_name + "-" + i + ".txt", 0,
									num_of_blocks, num_of_blocks, 0, num_of_blocks * 256, FormattedDate, "", i);

							Main_Dir_INFO.add(PDIR);
							
							PFS_ADD_DIR(fil_name + ".txt", file_siz, opn_fil_name + "-" + i + ".txt", 0, num_of_blocks,
									num_of_blocks, 0, file_siz, FormattedDate, "", i);
							
							//Write_Data(fil_name+ ".txt", opn_fil_name + "-" + i, 0, file_siz);

							num_of_blocks = 0;

							// dbs.get(dbs.size() - 1).Current_Block_Status();
						}
					}
				}

				// If file is creating for first time
				
				else {
					System.out.println("\nPFS Directory file not created, Creating a new one!!!");

					for (int i = 0; i < loop_val; i++) {
						
						F_C_B_Helper_Write_Get FCBWR= new F_C_B_Helper_Write_Get(); 
						
						FCB_W_R.add(FCBWR);

						get_curr_blck_stats = dbs.get(i).Return_Curr_Block();

						curr_block = get_curr_blck_stats.get(0);

						free_blocks = get_curr_blck_stats.get(1);

						int add_blocks = curr_block + num_of_blocks;
						
						int curr_ofst = FCB_W_R.get(i).Return_Stats();
						
						if (num_of_blocks < free_blocks) {
							for (int j = curr_block; j < add_blocks; j++) {
								dbs.get(i).Data_Load_Blocks(j, fil_name + ".txt");
							}

							Date CreatedDate = new Date();

							String FormattedDate = new SimpleDateFormat("h:mm a MMMMM dd").format(CreatedDate);

							PFS_DIR PDIR = new PFS_DIR(fil_name + ".txt", file_siz, opn_fil_name + "-" + i + ".txt",
									curr_block, add_blocks, num_of_blocks, 0, file_siz,
									FormattedDate, "", i);
							
							FCB_W_R.get(i).F_C_B_Helper_Write(fil_name+ ".txt",opn_fil_name + "-" + i + ".txt",(file_siz));
							
							//Write_Data(fil_name+ ".txt", opn_fil_name + "-" + i, curr_block * 256, file_siz);

							PFS_ADD_DIR(fil_name + ".txt", file_siz, opn_fil_name + "-" + i + ".txt", curr_block,
									add_blocks, num_of_blocks,  0, file_siz, FormattedDate, "",
									i);

							Main_Dir_INFO.add(PDIR);

							// if all blocks are written in any of the block then '0' the blocks

							num_of_blocks = 0;
							add_blocks = 0;
							break;

							// dbs.get(i).Current_Block_Status();
						}
					}
				}
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else {
			System.out.println("\n Mentioned file not present in the current directory");
		}
	}

	// Maintaining the Directory information
	
	public void DIR() {
		System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "File Name", "PFS HOLDER", "File Size",
				"Created On", "Remarks"));

		for (int i = 0; i < Main_Dir_INFO.size(); i++) {
			System.out.println("\n" + String.format("%-20s%-20s%-20s%-20s%-20s", Main_Dir_INFO.get(i).File_Name,
					Main_Dir_INFO.get(i).PFS_Number, Main_Dir_INFO.get(i).File_Size,Main_Dir_INFO.get(i).Created_Date_Time ,
					Main_Dir_INFO.get(i).Remarks));
		}
	}

	// Maintain the entire PFS information in a header block File
	
	public void PFS_ADD_DIR(String FNM, int FIL_SIZ, String PFS_NUM, int SartBLK, int EndBLK, int NUM_OF_BLKS,
			int FIL_OFF_STA, int FIL_OFF_END, String CDT, String Remarks, int BlckClsNumb) throws IOException {

		try {
			os = new FileOutputStream(this.PFSDirfil, true);

			this.AddLine = (String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s%n", FNM, FIL_SIZ,
					PFS_NUM, SartBLK, EndBLK, NUM_OF_BLKS, FIL_OFF_STA, FIL_OFF_END, CDT, Remarks, BlckClsNumb));

			os.write(this.AddLine.getBytes());

			os.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	// Handles to get the data from the PFS file created, extracts and creates a new file in other directory
	
	public void Get_Data(String Get_Fil_Name) throws IOException {
		int cond_check =0;
		File GetFileName;
		
		
		//System.out.println("\n------\tGet Called\t--------");
		
			if (Main_Dir_INFO.size()>0) {
				
				for (int i=0;i<Main_Dir_INFO.size();i++)
				{
					
					if (Main_Dir_INFO.get(i).File_Name.equals(Get_Fil_Name+".txt"))
					{
						
						System.out.format("%s - %s - %s%n",Main_Dir_INFO.get(i).PFS_Number,Main_Dir_INFO.get(i).File_Offset_Start, Main_Dir_INFO.get(i).File_Size);
						
						byte[] ReadBytes = new byte[Main_Dir_INFO.get(i).File_Size+200];
						
						cond_check =1;
					
						FileInputStream fis = new FileInputStream("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\"+Main_Dir_INFO.get(i).PFS_Number);
						
						try {
							
							
							fis.read(ReadBytes,Main_Dir_INFO.get(i).File_Offset_Start,Main_Dir_INFO.get(i).File_Size);
							
							fis.close();
						}
						
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						
						}
						
						//System.out.println(ReadCBytes.toString());
						
						String read = new String(ReadBytes);
						
						read = read.trim();
						
						//System.out.println(read);
						
						File GetCreateFileName1 = new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\" +Main_Dir_INFO.get(i).File_Name);
						
						FileWriter GCF = new FileWriter(GetCreateFileName1);
						
						try {
							
							GCF.write(read);
							
							GCF.close();
							
						}
						
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						
						}
						
						read="";
						
						break;
					}
				}
			
				if (cond_check ==0)
					System.out.println("\n------\tMentioned file not present in PFS Directory\t--------");
				
			}
			else
				System.out.println("\n------\tYou have not inserted any file into PFS Directory\t--------");
	}
	
	
	// Adds the remarks for any file added into the current PFS system
	// Adds the data into the FCB header and maintains even after re-opening them
	
	public void AddRemarks(String Filename,String Remaarks) throws IOException {
		
		int indx_to_del = -1,rmrk_pos=0;
		
		
		for (int i =0;i<Main_Dir_INFO.size();i++ )
		{
			if (Main_Dir_INFO.get(i).File_Name.equals(Filename+".txt"))
			{
				indx_to_del = i;
				
				Main_Dir_INFO.get(indx_to_del).Remarks=Remaarks;
				
				//rmrk_pos=Integer.parseInt(Main_Dir_INFO.get(indx_to_del).Remarks); 
				
				ReplaceAll(open_file_name, i);
				
				PFS_ADD_DIR(Main_Dir_INFO.get(i).File_Name, Main_Dir_INFO.get(i).File_Size, 
						Main_Dir_INFO.get(i).PFS_Number,Main_Dir_INFO.get(i).StartBlock,
						Main_Dir_INFO.get(i).EndBlock, Main_Dir_INFO.get(i).NUM_of_Blocks,
						Main_Dir_INFO.get(i).File_Offset_Start,
						Main_Dir_INFO.get(i).File_Offset_End, 
						Main_Dir_INFO.get(i).Created_Date_Time,
						Remaarks, 
						Main_Dir_INFO.get(i).BlockClassID);
				
				break;
			}
			
		}
		
		if (indx_to_del==-1) {
			
			System.out.println("\nMentioned file not present in Directory");
			
		}
	}
	
	// Removes the temporary file  which is used for maintaining in between writes and updates 
	
	public void Remove(String RmFilename) throws IOException {
		
		String writer = "~";
		
		String RMFilename = RmFilename+".txt";
	
			for (int i =0;i<Main_Dir_INFO.size();i++ )
			{
				
				if (Main_Dir_INFO.get(i).File_Name.equals(RMFilename))
				{
					RandomAccessFile Dell_Rand = new RandomAccessFile("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\"+Main_Dir_INFO.get(i).PFS_Number,"rw");
					
					for (int iii = Main_Dir_INFO.get(i).File_Offset_Start;iii<Main_Dir_INFO.get(i).File_Offset_End;iii++ )
						{
							
							Dell_Rand.seek(iii);
							
							Dell_Rand.write(writer.getBytes());
						}
					Dell_Rand.close();
					
					String Repl = Main_Dir_INFO.get(i).PFS_Number;
					
					ReplaceAll(open_file_name, i);
					
					TrimEmptySpaces(open_file_name,Main_Dir_INFO.get(i).BlockClassID);
					
					for(int lk=Main_Dir_INFO.get(i).StartBlock;lk<Main_Dir_INFO.get(i).EndBlock;lk++ )
					{
						dbs.get(Main_Dir_INFO.get(i).BlockClassID).MakeEmpty(lk,"A");
					}
					
					Main_Dir_INFO.remove(i);
					
				}
				
			}
	}
	
	// Replaces all the inbetween entered escape characters and removes the spaces and maintains file properly
	
	public void ReplaceAll(String Filereplace, int Linenum) throws IOException
	{
		
		int line_counter=0;
		
		System.out.println("Called");
		
		File MainFile= new File(Filereplace); 
		
		File TempFile=new File(Filereplace+".tmp");
		
		String Reading;
				
		BufferedReader reader = new BufferedReader(new FileReader(MainFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(TempFile));

		while ((Reading = reader.readLine()) != null)
		{
			String trimmedLine = Reading.trim();
			
			if (line_counter == Linenum)
			{	
				line_counter +=1;
				continue;
			}
			else
				//writer.write(Reading.cbuf, off, len);
				writer.write(Reading + System.getProperty("line.separator"));
				line_counter +=1;
		}
		
		reader.close();
		writer.close();
		if (!MainFile.delete()) {
	        System.out.println("Could not delete file");
	        return;
	      }
		
		if (!TempFile.renameTo(MainFile))
	        System.out.println("Could not rename file");
	}
	
	// Trims the data 
	
	public void TrimEmptySpaces(String repl, int blockClassID) throws IOException
	{
		File MainFile= new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + opn_fil_name +"-"+blockClassID+ ".txt"); 
		
		File TempFile=new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + opn_fil_name +"-"+blockClassID+".tmp");
		
		BufferedReader reader = new BufferedReader(new FileReader(MainFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(TempFile));
		
		String ReadNoSpace ="";
		
		String rrd,Allread = "";
		
		while ((rrd = reader.readLine()) != null)
		{
			Allread+=rrd;
		}
		
		Allread = Allread.replaceAll("~", "");
		
		writer.write(Allread);
		
		reader.close();
		
		writer.close();
		
	}
	
	// Kills the current PFS and removes all the intermediate extension files.
	
	public void Kill(String fil_name)
	{
		for (int i=0;i<dbs.size();i++)
		{
			File SubFile= new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + fil_name+"-"+i+".txt");
			//File TempFile= new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + fil_name+"-"+i+".tmp");
			
			if (!SubFile.delete()) {
		        System.out.println("Could not delete file");
		        return;
		      }
			
		}
		
		File MainFile= new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\" + fil_name+".txt");
		
		if (!MainFile.delete()) {
	        System.out.println("Could not delete file");
	        return;
	      }
		
		System.out.println("\nExisting PFS is deleted!!!!!!!");
	}
	
	public void Run(String Get_Fil_Name) throws IOException
	{
		int cond_check =0;
		File GetFileName;
		
		
		if (Main_Dir_INFO.size()>0) {
			
			for (int i=0;i<Main_Dir_INFO.size();i++)
			{
				
				if (Main_Dir_INFO.get(i).File_Name.equals(Get_Fil_Name+".txt"))
				{
					
					System.out.format("%s - %s - %s%n",Main_Dir_INFO.get(i).PFS_Number,Main_Dir_INFO.get(i).File_Offset_Start, Main_Dir_INFO.get(i).File_Size);
					
					byte[] ReadBytes = new byte[Main_Dir_INFO.get(i).File_Size+200];
					
					cond_check =1;
				
					FileInputStream fis = new FileInputStream("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\src\\"+Main_Dir_INFO.get(i).PFS_Number);
					
					try {
						
						
						fis.read(ReadBytes,Main_Dir_INFO.get(i).File_Offset_Start,Main_Dir_INFO.get(i).File_Size);
						
						fis.close();
					}
					
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					}
					
					//System.out.println(ReadCBytes.toString());
					
					String read = new String(ReadBytes);
					
					read = read.trim();
					
					//System.out.println(read);
					
					File GetCreateFileName1 = new File("C:\\Users\\Sreeram\\OS_Project_2\\OS_Proj_Phase2\\" +Main_Dir_INFO.get(i).File_Name);
					
					FileWriter GCF = new FileWriter(GetCreateFileName1);
					
					try {
						
						GCF.write(read);
						
						GCF.close();
						
						java.awt.Desktop.getDesktop().edit(GetCreateFileName1);
						
						
					}
					
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					}
					
					read="";
					
					break;
				}
			}
		
			if (cond_check ==0)
				System.out.println("\n------\tMentioned file not present in PFS Directory\t--------");
			
		}
		else
			System.out.println("\n------\tYou have not inserted any file into PFS Directory\t--------");
	}
}

	
