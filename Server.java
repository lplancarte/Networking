/**
Programmer: Lucio Plancarte
Created/Modified: 24-NOV-2024


*/

import javax.swing.JFrame;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.BorderFactory;

import javax.swing.JScrollBar;

import java.awt.Font;

import javax.swing.border.TitledBorder;

/*
https://www.geeksforgeeks.org/java-swing-jmenubar/
*/
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager; //Used for Informational Icon 

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;


import java.util.Date;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**

*/
public class Server extends JFrame{

	private static final int PORT = 12345;
	private static final int HEIGHT = 800;
	private static final int WIDTH = 600;
	
	//FILE IO -> Brought in from Previous Lab 
	private static int [][] blue = null;
	private static int [][] red = null;
	private static int [][] purple = null;
	private static int ROW = 0;
	private static int COL = 0;


	//---------GUI Components---------------------
	private JPanel backgroundPanel;
	

	private JTextArea displayArea1; //matrix 1
	private JTextArea displayArea2; //matrix 2
	private JTextArea displayArea3; //matrix 3

	private JPanel displayPanel1; //matrix 1
	private JPanel displayPanel2; //matrix 2
	private JPanel displayPanel3; //matrix 3


	private JTextArea output;
	private JScrollPane outputPanel; //terminal Output

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem m1;
	private JMenuItem m2;
	private JMenuItem m3;
//---------------------------------------------
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private ServerSocket server;
	private Socket connection;


	private String dummyData = "";

	private ThreadOperation upperRight;
	private ThreadOperation upperLeft;
	private ThreadOperation lowerRight;
	private ThreadOperation lowerLeft;

	public Server (){
		//GUI
		super("Server: Matrix Addition");	
		buildGUI();
				




	}
	//START NETWORKING

	
	public void runServer(){
		try{
			server = new ServerSocket(PORT,10);
			while(true){
				try{
					waitForConnection();
					getStreams();
					processConnection();
				}catch(EOFException e){
					writeToOuputTerminal("Connection to Server Lost");
				}finally{
					closeConnection();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			writeToOuputTerminal(e.toString());
		}
	}
	
	public void waitForConnection() throws IOException{
		writeToOuputTerminal("Waiting for connection...");
		connection = server.accept();
		writeToOuputTerminal("Connection recieved from: "+
							connection.getInetAddress().getHostName());
	}
	
	public void getStreams() throws IOException{
		outputStream = new ObjectOutputStream(connection.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(connection.getInputStream());
		writeToOuputTerminal("Connection Established");
	}
	
	public void processConnection() throws IOException{
		
		
		do{
			try{

				//Read in blue array
				blue = (int[][])inputStream.readObject();
				
				//CLose connection if blue array is null
				if(blue == null){
					closeConnection();
					//displayArea1.setText("");
					//displayArea2.setText("");
					resetMatrixDisplays(4);
					break;
				}
				
				//Blue Matrix; reset display area, print new array to display area, inform user 
				successMessageAnnounce(1, blue, displayArea1, "BLUE");

				//Red Matrix; read in red array, reset display area, print new array to display area, inform user
				red = (int[][])inputStream.readObject();
				successMessageAnnounce(2,red, displayArea2, "RED");

				
				//Start return sum of red and blue => Purple Matrix  
				purple = new int[red.length][red[0].length];
				writeToOuputTerminal(red.length+"x"+red[0].length+" Matrix Created");
				System.out.println(" MATRIX PURPLE ["+red.length+"x"+red[0].length+"] created.");
				
				//Instantiate 4 ThreadOperation objects
				System.out.println("Setting (4) ThreadOperation Objects");
				upperRight = new ThreadOperation
											(blue,red,purple,Quadrant.I);
				upperLeft = new ThreadOperation
											(blue,red,purple,Quadrant.II);
				lowerRight = new ThreadOperation
											(blue,red,purple,Quadrant.III);
				lowerLeft = new ThreadOperation
											(blue,red,purple,Quadrant.IV);
				//Start them
				System.out.println("Starting Threads");
				upperRight.start();
				upperLeft.start();
				lowerRight.start();
				lowerLeft.start();
				//Join them
				System.out.println("Joining Threads");
				try{
					upperRight.join();
					upperLeft.join();
					lowerRight.join();
					lowerLeft.join();

				}catch(InterruptedException e){
					System.out.println("INTERRUPTION HAS OCCURED");
					writeToOuputTerminal("ADDITION Failed.");
				};
				//PURPLE Sent Success 
				successMessageAnnounce(3,purple,displayArea3,"PURPLE");

			}catch(ClassNotFoundException e){
				writeToOuputTerminal("Unknown object type recieved");
			}
		}while(true);
	}
	
	public void closeConnection(){
		writeToOuputTerminal("Terminating Connection");
		try{
			outputStream.close();
			inputStream.close();
			connection.close();
		}catch(IOException e){
			e.printStackTrace();
			writeToOuputTerminal(e.toString());
		}
	}
	
	public void sendData(int[][] matrix){
		try{
			outputStream.writeObject(matrix);
			outputStream.flush();
			writeToOuputTerminal("Matrix Sent");
		}catch(IOException e){
			writeToOuputTerminal("Error writing object");
		}
	}
	
	///--------------PRINT METHODS GUI & TERMINAL------------/
	public  void print2dArray(int[][] matrix){
		//print array given
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				System.out.printf("%4d",matrix[i][j]);
			}
			System.out.println();
		}
	}//end print2dArray


	public  void print2dArrayToTextArea(int[][] matrix, JTextArea textArea){
		//print array given
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				textArea.append(String.format("%3d",matrix[i][j]));	
			}
			//if(i < matrix.length -1)
				textArea.append("\n");
		}
	}//end print2dArray

		
	//HELPER METHODS 
	private void writeToOuputTerminal(String message){
		output.append(message+"\n");
		output.setCaretPosition(output.getDocument().getLength());
	}//end writeToOuputTerminal
	

	/**
	*successMessageAnnounce prints to gui and terminal success Messages
	@param int displayArea - used to in resetMatrtixDisplay() to reset display 
	@param int[][] matrix to print in display 
	@param  JTextArea display - JTextArea displayArea1,2,or 3
	@param String matrixName - name of the Matrix Name to display in messages 
	*/
	private void successMessageAnnounce(int displayArea, int[][] matrix,
									JTextArea display, String matrixName){
		resetMatrixDisplays(displayArea);
		
		//success in sending matrix back to client 
		if(displayArea == 3){
			System.out.println("Thread Operations Successful.");
			System.out.println("MATRIX RED ADDED TO BLUE");
			print2dArray(matrix);
			System.out.println("Sending Result Matrix "+ matrixName+" to Client");
			//Purple Matrix ; send to client 
			writeToOuputTerminal("Matrix ADDED Successfully!");
			displayArea3.setText("");
			print2dArrayToTextArea(matrix,display);
			sendData(matrix);//end Send data purple 
			System.out.println("Matrix sent successfully.");
		}else{
			//GUI terminal output
			print2dArrayToTextArea(matrix, display);
			writeToOuputTerminal(matrixName+ " Transfer Successful");
				
			//System output
			System.out.println("MATRIX "+matrixName+ " Transfer Successful");
			print2dArray(matrix);
		}
	}

	/**
	*resetMatrixDisplays() will reset textareadisplays 1, 2, 3 or all
	@Param int displayArea - # of the display to clear; 1, 2, 3, default: all
	*/
	private void resetMatrixDisplays(int displayArea){
	//private void resetUserInput(){
		
		switch(displayArea){
			case 1: displayArea1.setText("");
			break;
			case 2: displayArea2.setText("");
			break;
			case 3: displayArea3.setText("");
			break;
			default:{
				displayArea1.setText("");
				displayArea2.setText("");
				displayArea3.setText("");
			}
		}

		System.out.println("Matrix Reset");

		
	}

	/**------------END PRINT METHODS --------------------------------------*/

	/**------------START GUI COMPONENTS METHODS------------------------------*/ 

	private void buildGUI(){
			
		preBuild();
		//CREATE Terminal Output 
		buildOutputTerminal();
		//SET UP TEXT AREAS FOR MATRIX DISPLAY
		setUpMatrixDisplays();
		//ADD TEXT AREAS TO DISPLAY PANELS; SET BORDERS W/TITLES
		addMatrixDisplaysToPanels();
		//ADD PANELS TO BACKGROUND PANEL
		buildBackgroundPanel();
		postBuild();
	}
	


	
	
	
	
	//MENU
	private void addMenu(){
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		m1 = new JMenuItem("About",
			UIManager.getDefaults().getIcon("OptionPane.informationIcon")
		);
		m2 = new JMenuItem("Help",
			UIManager.getDefaults().getIcon("OptionPane.questionIcon")
		);
		m3 = new JMenuItem("Exit",
			UIManager.getDefaults().getIcon("OptionPane.warningIcon")
		);
		
		//Add menu item Action Listeners 
		m1.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//show pop up message
					JOptionPane.showMessageDialog(
						outputPanel,
						"About this App.\n"+
						"Adds matricies from a Client using threads\n"+
						"Returns result to Client.\n"+
						"Listening on Port: "+ PORT ,
						"ABOUT",
						JOptionPane.INFORMATION_MESSAGE
					
					);

				}
			}
		);
		
		m2.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//show pop up message
					JOptionPane.showMessageDialog(
						outputPanel,
						"Help me App!\nThere is no help here...\nYet!",
						"HELP",
						JOptionPane.QUESTION_MESSAGE
					
					);

				}
			}
		);
		
		m3.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//show pop up message
					int userOpt = JOptionPane.showConfirmDialog(
						outputPanel,
						"To Exit.\nOr Not to Exit.\nThat is the Question.",
						"EXIT",
						JOptionPane.CANCEL_OPTION
					
					);
					if(userOpt == JOptionPane.OK_OPTION){
						closeConnection();
						System.out.println("Exiting.");
						System.exit(1);
					}

				}
			}
		);		
		
		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		menuBar.add(menu);
		super.setJMenuBar(menuBar);
	}




	
	//BUILD TERMINAL OUTPUT
	private void buildOutputTerminal(){
		Date d = new Date();
		output = new JTextArea("Matrix Addition Server Log\n"+
								d+"\n"+
								"Waiting for connection...\n",
								13,55);
		output.setWrapStyleWord(true);
		output.setLineWrap(true);
		output.setForeground(Color.green);
		output.setBackground(Color.black);
		output.setEditable(false);
		
		//BUILD OUTPUT TERMINAL 
		outputPanel = new JScrollPane(output,
								JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
								JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar vSBar = outputPanel.getVerticalScrollBar();
		
		outputPanel.setBorder(BorderFactory.createTitledBorder(
			null,
			"Server Log",
			TitledBorder.CENTER,
			0,
			new Font("",Font.BOLD,20),
			Color.black
		));
		
	}
	
	//DISPLAY MATRICES 
	private void setUpMatrixDisplays(){
		displayArea1 = new JTextArea(dummyData);
		displayArea1.setEditable(false);
		displayArea1.setFont(new Font("", Font.BOLD,19));
		displayArea1.setBackground(Color.black);
		displayArea1.setForeground(Color.green);

		displayArea2 = new JTextArea(dummyData);
		displayArea2.setEditable(false);
		displayArea2.setFont(new Font("", Font.BOLD,19));
		displayArea2.setBackground(Color.black);
		displayArea2.setForeground(Color.green);

		displayArea3 = new JTextArea(dummyData);
		displayArea3.setEditable(false);
		displayArea3.setFont(new Font("", Font.BOLD,19));
		displayArea3.setBackground(Color.black);
		displayArea3.setForeground(Color.green);
		
	}
	
	private void addMatrixDisplaysToPanels(){
		displayPanel1.add(displayArea1);
		displayPanel1.setBorder(BorderFactory.createTitledBorder(
			null,
			"Matrix A",
			0,
			0,
			new Font("",Font.ITALIC,19),
			Color.white
		));

		displayPanel2.add(displayArea2);
		displayPanel2.setBorder(BorderFactory.createTitledBorder(
			null,
			"Matrix B",
			0,
			0,
			new Font("",Font.ITALIC,19),
			Color.white
		));

		displayPanel3.add(displayArea3);
		displayPanel3.setBorder(BorderFactory.createTitledBorder(
			null,
			"Matrix Sum",
			TitledBorder.CENTER,
			0,
			new Font("",Font.BOLD,15),
			Color.white
		));
	}
	
	
	private void preBuild(){
		//Sizing
		int height = HEIGHT;
		int width = WIDTH;
		Dimension frameD = new Dimension(width,height);
		//Initialize Background Panel & set preferred size 
		backgroundPanel = new JPanel();
		backgroundPanel.setPreferredSize(frameD);


		
		//MATRIX A DISPLAY PANEL
		displayPanel1 = new JPanel();
		displayPanel1.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel1.setBackground(Color.gray);

		//MATRIX B DISPLAY PANEL
		displayPanel2 = new JPanel();
		displayPanel2.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel2.setBackground(Color.gray);

		//MATRIX SUM DISPLAY PANEL
		displayPanel3 = new JPanel();
		displayPanel3.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel3.setBackground(Color.darkGray);
	}
	
	private void buildBackgroundPanel(){
		backgroundPanel.add(outputPanel);
		backgroundPanel.add(displayPanel1);
		backgroundPanel.add(displayPanel2);
		backgroundPanel.add(displayPanel3);
		
	}
	
	private void postBuild(){
		//ADD BACKGROUND PANEL TO JFRAME; set size,visibility, & restrict resize
		add(backgroundPanel);
		setSize(700,560);
		//setSize(900,600);

		
		//ADD ABOUT MENU
		addMenu();
		setVisible(true);
		setResizable(false);
	}
	//_______________________END GUI COMPONENTS METHODS________________________
	
	
}//end Server Class
