import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;

public class PFS_DIR {
	

	int File_Size, NUM_of_Blocks, File_Offset_Start, File_Offset_End, StartBlock, EndBlock, BlockClassID;
	String PFS_Number,File_Name,Created_Date_Time, AddLine,Remarks;
	
	FileOutputStream os;
	
	File CRT_PFS_FIL;
	
	public PFS_DIR(String FNM,int FIL_SIZ, String PFS_NUM, int Start_blk, int End_blk, int NUM_OF_BLKS, int FIL_OFF_STA, int FIL_OFF_END, String CDT, String Remrks, int BLKID)
	{
		this.File_Name =FNM;
		this.File_Size = FIL_SIZ;
		this.PFS_Number = PFS_NUM;
		this.StartBlock =Start_blk;
		this.EndBlock = End_blk;
		this.NUM_of_Blocks = NUM_OF_BLKS;
		this.File_Offset_Start =FIL_OFF_STA;
		this.File_Offset_End = FIL_OFF_END;
		this.Created_Date_Time = CDT;
		this.Remarks = Remrks;
		this.BlockClassID = BLKID;
	}
	
	public void PFS_ADD_DIR(File PFS_FIL,String FNM,int FIL_SIZ, String PFS_NUM, int NUM_OF_BLKS, int FIL_OFF_STA, int FIL_OFF_END, String CDT) throws IOException {
		// TODO Auto-generated constructor stub
			
	}

}
