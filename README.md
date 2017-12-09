# PortableFileSystem
This java code creates a portable file system with the below mentioned details.

Problem Statement:
Implement a Portable File System (PFS) with Contiguous Allocation Method, which can perform “Allocate a file”, and “Move files from the Windows file system into your file.” You should have your own directory structure, allocation table, etc. inside your file. Move files back out of your file to the Windows file system or Linux/Unix file system.
When your program is running it should somehow accept the following commands:
open PFSfile Allocate a new 10 KByte "PFS" file if it does not
already exist. If it does exist, begin using it for further
commands.
put myfile Copy the Windows (or Unix/Linux) file "myfile" into
your PFS file.
get myfile Extract file "myfile" from your PFS file and copy it to
the current Windows (or Unix/Linux) directory.
rm myfile Delete "myfile" from your PFS file.
dir List the files in your PFS file.
putr myfile "Remarks" Append remarks to the directory entry for myfile in
your PFS file.
kill PFSfile Delete the PFSfile from the Windows file system.
quit Exit PFS.
You can provide those commands through command line interface ONLY (No GUI will be accepted).
Limits:
1. PFS is NOT a memory based file system, but based on the existing file system on your OS, such as Windows or Linux/Unix.
2. Command-Line Interface (CLI) Only, i.e., cmd.exe (in Windows) or shell interface (in Unix/Linux). When PFS is executed, it will show the prompt as:
C:\> pfs.exe
PFS> open pfs
3. Filenames are a maximum of 20 bytes. And, file extension is optional like Unix/Linux.
4. The directory need handle only Name, Size, Time and Date. For example,
PFS> dir
Test1.txt 128 bytes 12:30 PM September 2
Test2.txt 512 bytes 11:00 AM November 11
Lee.exe 1k bytes 08:52 PM September 1
. . .
5. If the original PFS file fills up then you should create a new PFS "volume" with the same name but a different suffix - e.g., pfs.1, pfs.2, etc., each the same size as the first "volume".
6. Your file system should use Contiguous Allocation for an allocation scheme where “disk block” size is 256 bytes.
7. In your files system, each file has one File Control Block (FCB) that includes file name, file size, create time, create date, starting block ID, ending block ID and more (if needed).
8. Your file system should consist of two main parts, i) Directory Structure and ii) Data Blocks. Therefore, you need to define the directory data structure that includes File Control Block (FCB) for each file.
9. In addition, your files system should be able to manage free blocks. You can use any techniques that you learned in the classroom, such as bit map (vector) or linked free space management. Free block management should be part of directory structure.
10. You should handle unusual conditions such as trying to put a file into the PFS when a file with that name is already there, file too large to fit into one "volume", etc.
