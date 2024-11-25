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


import java.util.Date;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**

*/
public class Server extends JFrame{

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
//---------------------------------------------
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private ServerSocket server;
	private Socket connection;


	private String dummyData = "";


	public Server(){
		//GUI
		super("Server: Matrix Addition");	
		buildGUI();
				




	}
	//START NETWORKING

	
	public void runServer(){
		try{
			server = new ServerSocket(12345,10);
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
				//TODO: Write a INSERTMETHODNAMEHERE() 
				
				blue = (int[][])inputStream.readObject();
				if(blue == null){
					closeConnection();
					break;
				}
				displayArea1.setText("");
				print2dArrayToTextArea(blue,displayArea1);
				writeToOuputTerminal("BLUE Transfer Successful");
				
				red = (int[][])inputStream.readObject();
				displayArea2.setText("");
				print2dArrayToTextArea(red,displayArea2);
				writeToOuputTerminal("RED Transfer Successful");
				//return sum of red and blue 
				purple = new int[red.length][red[0].length];
				writeToOuputTerminal(red.length+"x"+red[0].length+" Matrix Created");
				
				//sendData(purple);
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
	
	///--------------START FILE IO METHODS-------->FROM PREVIOUS LAB----------
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
				textArea.append(String.format("%2d",matrix[i][j]));
			}
			if(i < matrix.length -1)
				textArea.append("\n");
		}
	}//end print2dArray



		

	/**		createArrays(int rows, int cols)
	@param int row - number of rows; found in text file
	@param int col - number of cols; found in text file
	*This method is a helper method to processFile. After ROW and COL
	*are read, three arrays are created and filled with 0's with 
	*appropriate number of rows and columns
	*/
	 private void createArrays(int rows, int cols){
		blue = new int[rows][cols];
		red = new int[rows][cols];
		purple = new int[rows][cols];
	}//end createArrays()
	
	
	///-------------END FILE IO METHODS---------------------------------------
	
	
	//HELPER METHODS 
	private void writeToOuputTerminal(String message){
		output.append(message+"\n");
		output.setCaretPosition(output.getDocument().getLength());
	}
	

	/**-----------BUTTON ACTION METHODS----------------------------*/
	/**
	*processUserInput() processess user input to some degree
	for now it will print to terminal and gui, switch buttons around.
	Used in ActionEventListener and KeyEventListener for submitBtn and
	userInputField respectivley.
	*/
	private void processUserInput(){
		System.out.println("Submit Button Pressed");
		String input = "";
		System.out.println(input);
		writeToOuputTerminal("Opening: "+ input);
		//TODO: Validate user input

		System.out.printf("ROW: %d\nCOL: %d\n",ROW, COL);
		System.out.println("Printing array: blue");
		print2dArray(blue);
		print2dArrayToTextArea(blue,displayArea1);

		System.out.println("\nPrinting array: red");
		print2dArray(red);
		print2dArrayToTextArea(red,displayArea2);
		
		
		//END
		writeToOuputTerminal("\nFile Processed");
		

	}//end processUserInput()


	/**
	*resetUserInput() resets GUI by 
		*making the submit button visible
		*disabling the submit button: until input is detected in textfield
		*userInputField is set to editable and ".txt" added
	*/
	private void resetUserInput(){
		System.out.println("Reset Button Pressed");
	

		//reset matrix panels
		displayArea1.setText("");
		displayArea2.setText("");
		
	}

	/**
	*sendPacket is used when add button is pressed, sends object to server
		in TODO stage
	*/
	private void sendPacket(){
		System.out.println("Add Button Pressed");
		writeToOuputTerminal("Sending Matrices to Server.\nAwaiting Response...");
		//TODO:disable reset button and add button until server responds
	}

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
		menu.add(m1);
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
		
		//ADD ABOUT MENU
		addMenu();
		setVisible(true);
		setResizable(false);
	}
	//_______________________END GUI COMPONENTS METHODS________________________
	
	
}//end Server Class
