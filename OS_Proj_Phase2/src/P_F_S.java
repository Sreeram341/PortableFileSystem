/*
 * File Name : P_F_S.java
 * 
 * Author : Sreeram Pulavarthi
 * 
 * Date: 12-01-2017
 * 
 * Compiler Used: Java 1.8
 * 
 * Description of File: Driver program for Portable file system where we can perform all operations on it 
 * 
 * 
 */


import java.io.*;
import java.util.*;

import org.omg.CORBA.Request;



public class P_F_S {

	boolean val = true;
	boolean val2 = true;
	Scanner sc = new Scanner(System.in);
	String inp = "";
	String[] usr_req;

	/*
	 * Constructs the driver class and waits for user input 
	 */
	
	public P_F_S() {
		// TODO Auto-generated constructor stub

		while (val) {

			System.out.println("PFS> ");

			inp = sc.nextLine();

			usr_req = inp.split(" ");

			if (inp.equals("quit")) {
				System.out.println("Exiting from PFS");

				val = false;
			} else if (usr_req[0].toUpperCase().equals("OPEN")) {
				System.out.println("\nOpening PFS " + usr_req[1]);

				/*
				 * Creates the object for the main class to perform all operations on file System
				 *  
				 */
				
				F_C_B fcb;

				try {
					fcb = new F_C_B(usr_req[1]);

					while (val2) {

						if (inp.equals("quit")) {
							System.out.println("Exiting from PFS");

							val = false;
							val2 = false;
						} else {

							System.out.println("\nPFS> ");

							inp = sc.nextLine();

							usr_req = inp.split(" ");
							
							/*
							 * 
							 * Splits the user input and passes the input file to FCB block
							 *  
							 */

							if (usr_req[0].toUpperCase().equals("PUT"))

								fcb.Put_File(usr_req[1]);
							else if (inp.toUpperCase().equals("DIR")) {
								fcb.DIR();
							}
							else if (usr_req[0].toUpperCase().equals("GET")) {
								fcb.Get_Data(usr_req[1]);
							}
							else if (usr_req[0].toUpperCase().equals("PUTR")) {
								fcb.AddRemarks(usr_req[1],usr_req[2]);
							}
							else if (usr_req[0].toUpperCase().equals("RM")) {
								fcb.Remove(usr_req[1]);
							}
							else if (usr_req[0].toUpperCase().equals("KILL")) {
								fcb.Kill(usr_req[1]);
								val = false;
								val2 = false;
							}
							else if (usr_req[0].toUpperCase().equals("RUN")) {
								fcb.Run(usr_req[1]);
							}
							else {
								System.out.println("\n Invalid Command");
							}
						}

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else {

				System.out.println("\n Operation cant be performed as PFS is neither created nor Opened");
			}

		}

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		P_F_S pf = new P_F_S();

	}

}
