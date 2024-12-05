/**
Programmer: Lucio Plancarte
Created: 23-NOV-2024

*/

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
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
import javax.swing.JButton;
import javax.swing.JScrollBar;

import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.border.TitledBorder;

/*
https://www.geeksforgeeks.org/java-swing-jmenubar/
*/
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager; //Used for Informational Icon 

import java.nio.file.Path;
import java.nio.file.Paths;

import java.net.InetAddress;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;

import javax.swing.JOptionPane;

/**

*/
public class Client extends JFrame{

	private static final int HEIGHT = 800;
	private static final int WIDTH = 600;
	private final String clientIP = "127.0.0.1";
	private final int port = 12345;
	
	//FILE IO -> Brought in from Previous Lab 
	private static int [][] blue = null;
	private static int [][] red = null;
	private static int [][] purple = null;
	private static int ROW = 0;
	private static int COL = 0;
	private static String inputFileDefault = "matrix1.txt";

	//---------GUI Components---------------------
	private JPanel backgroundPanel;
	
	private JTextField userInputField;
	private JPanel textPanel;
	private JButton submitBtn;
	private JButton addBtn;
	private JButton resetBtn;

	private JTextArea displayArea1; //matrix 1
	private JTextArea displayArea2; //matrix 2
	private JTextArea displayArea3; //matrix 3

	private JPanel displayPanel1; //matrix 1
	private JPanel displayPanel2; //matrix 2
	private JPanel displayPanel3; //matrix 3

	private JLabel fileInputLabel;
	private JLabel fileNotFoundLbl;

	private JTextArea output;
	private JScrollPane outputPanel; //terminal Output

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem m1;
	private JMenuItem m2;
	private JMenuItem m3;
//---------------------------------------------

	//NETWORKING
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket client;


	private String dummyData = "";


	public Client(){
		//GUI
		super("Client: Matrix Addition");	
		buildGUI();
				




	}
	//START NETWORKING METHODS 
	public void runClient(){
		try{
			connectToServer();
			processConnection();
		}catch(EOFException e){
			System.out.println("\nClient Has Terminated Connection");
		}catch(IOException e){
			e.printStackTrace();
			writeToOuputTerminal(e.toString());
		}finally{
			closeConnection();
		}
	}
	
	public void connectToServer() throws IOException{
		writeToOuputTerminal("Attempting to connect...");
		client = new Socket(InetAddress.getByName(clientIP),port);
		writeToOuputTerminal("Connected to: "+ client.getInetAddress().getHostName());
	
		outputStream = new ObjectOutputStream(client.getOutputStream());
		outputStream.flush(); //always flush
		inputStream = new ObjectInputStream(client.getInputStream());
		writeToOuputTerminal("Connection Established");
	}
	
	public void processConnection() throws IOException{
		do{
			try{
				
				purple = (int[][])inputStream.readObject();
				writeToOuputTerminal("Matrix Returned from Server");
				print2dArrayToTextArea(purple,displayArea3);
			}catch(ClassNotFoundException e){
				writeToOuputTerminal("Unknown object recieved");
			}
	
		}while(!userInputField.getText().equals("TERMINATE.txt"));
	}
	
	public void closeConnection(){
		writeToOuputTerminal("Terminating Connection");
		try{
			outputStream.close();
			inputStream.close();
			client.close();
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
			writeToOuputTerminal("Error Sending Packet");
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
				textArea.append(String.format("%3d",matrix[i][j]));
			}
			if(i < matrix.length -1)
				textArea.append("\n");
		}
	}//end print2dArray

	/**		checkFile(String[] args)
	LLP: not checking arguments from terminal; modify to take in userinputField text
		TODO: change description @params 
	*Checks that the file to analyze exists. Default: 'matrix1.txt'
	*Has the capability of accepting user input for different txt files
	*if argument is detected in the terminal.
	*These custom txt files must have the same format as 'matrix1.txt'.
	*Header line with 2 numbers for row and columns
	*Number of lines = 2x rows with as many integers as columns
	@param args - Array of strings from the terminal used to search for file
				  can be empty
	@return inputFile - input file to be processed. Returns null for error.
	*/
	 private File checkFile(String fileName){
		File inputFile = null;
		Path inputFilePath =  null;
		try{
			if(fileName.length() > 0){
				inputFilePath = Paths.get(fileName);
				inputFile = new File(inputFilePath.toString());
				System.out.println("Checking "+inputFilePath.toString());
			}else{
				inputFile = new File(inputFileDefault); //default file
				System.out.println("No Arguments detected."+
 				"Analyzing default file: " + inputFile.getName());
			}
			//Check if the file exists
			if(!inputFile.canRead()){
				//display to outputTerminal
				writeToOuputTerminal("\n404: File not found."+		
				"The file you are looking for, isn't here.");
				fileNotFoundLbl.setVisible(true);
				
				throw new FileNotFoundException("404: File not found."+
				"The file you are looking for, isn't here.\n");
			}
		}catch(Exception e){
			System.out.print("Something went wrong. Error "+
			e.getMessage()+"\nExiting.\n");
			return null;
		}
		return inputFile;
	}//end checkFile()

		
	/** 	processFile(File inputFile)
	@param inputFile -File to be processed; returned by checkFile()
	*It is assumed that this file has both:
	*A header line with two integers representing rows and columns
	*Dual matrices one on top of the other (2xrow)xcol
	*/
	 private void processFile(File inputFile){

		try{
			Scanner reader = new Scanner(inputFile);

			//Feedback implementation
			ROW = reader.nextInt();
			COL = reader.nextInt();

			//Create Arrays red, blue, and purple,fill with 0's
			createArrays(ROW,COL);
			//Fill arrays blue and red with data from file
			fillArray(ROW,COL,blue,reader);
			fillArray(ROW,COL,red,reader);


		}catch(Exception e){};

	}//end processFile()

	/**
	*fillArray() fills an array with values from a file using a Scanner.
	*@param - int row - Number of Rows to fill
	*@param - int col - Number of Columns to fill
	*@param - int[][] array - The double integer array to fill
	*@param - Scanner rdr - used to read in data from file
	*@VOID RETURN
	*/
	public  void fillArray(int row,int col,int[][] array,Scanner rdr){
		for(int i=0; i<row; i++){
			for(int j=0; j<col; j++){
				array[i][j] = rdr.nextInt();
			}
		}


	}//end fillArray()


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
		String input = userInputField.getText();
		System.out.println(input);
		if(input.equals("TERMINATE.txt")){
			sendData(null);
			return;
		}
		writeToOuputTerminal("Opening: "+ input);
		//TODO: Validate user input
				//File Handling ->ADAPTED FROM PREVIOUS LAB
		System.out.println("File Handling Start");
		File inputFile = checkFile(input);
		if(inputFile == null)
			return;
		else
			processFile(inputFile);

		System.out.println("File Handling End");

		System.out.printf("ROW: %d\nCOL: %d\n",ROW, COL);
		System.out.println("Printing array: blue");
		print2dArray(blue);
		print2dArrayToTextArea(blue,displayArea1);

		System.out.println("\nPrinting array: red");
		print2dArray(red);
		print2dArrayToTextArea(red,displayArea2);
		
		
		//END
		writeToOuputTerminal("\nFile Processed");
		submitBtn.setVisible(false);
		addBtn.setVisible(true);
		userInputField.setEditable(false);
		
	}//end processUserInput()


	/**
	*resetUserInput() resets GUI by 
		*making the submit button visible
		*disabling the submit button: until input is detected in textfield
		*userInputField is set to editable and ".txt" added
	*/
	private void resetUserInput(){
		System.out.println("Reset Button Pressed");
		userInputField.setText(".txt");
		submitBtn.setVisible(true);
		submitBtn.setEnabled(false);
		addBtn.setVisible(false);
		userInputField.setEditable(true);
		fileNotFoundLbl.setVisible(false);
		//reset matrix panels
		displayArea1.setText("");
		displayArea2.setText("");
		displayArea3.setText("");
		
	}

	/**
	*sendPacket is used when add button is pressed, sends object to server
		in TODO stage
	*/
	private void sendPacket(){
		System.out.println("Add Button Pressed");
		writeToOuputTerminal("Sending Matrices to Server.");
		//TODO:disable reset button and add button until server responds
		displayArea3.setText("");
		sendData(blue);
		sendData(red);
		writeToOuputTerminal("\nAwaiting Response...");
	}

	/**------------START GUI COMPONENTS METHODS------------------------------*/ 

	private void buildGUI(){
			
		preBuild();
		//Initialize Buttons and add listeners
		createButtons();
		//CREATE User Input Text Field 
		setUpUserInputField();
		//CREATE Terminal Output 
		buildOutputTerminal();
		//SET UP TEXT AREAS FOR MATRIX DISPLAY
		setUpMatrixDisplays();
		//ADD TO TEXT PANEL 
		buildTextPanel();
		//ADD TEXT AREAS TO DISPLAY PANELS; SET BORDERS W/TITLES
		addMatrixDisplaysToPanels();
		//ADD PANELS TO BACKGROUND PANEL
		buildBackgroundPanel();
		postBuild();
	}
	
	//BUTTONS
	private void createButtons(){
		submitBtn = new JButton("SUBMIT");
		submitBtn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//display the input in terminal & GUI
					processUserInput();

				}
			}
		);

		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//reset the userinputfield.
					resetUserInput();
				}
			}
		);

		
		addBtn = new JButton("ADD");
		addBtn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sendPacket();
				}
			}
		);
		
		submitBtn.addMouseListener(
			new MouseListener(){
				public void mouseClicked(MouseEvent e){}
				public void mouseEntered(MouseEvent e){
					if(!(userInputField.getText().equals(".txt"))){
						submitBtn.setBackground(Color.green);
						submitBtn.setEnabled(true);
					}
				}
				public void mouseExited(MouseEvent e){
					if(!userInputField.getText().equals(".txt"))
					submitBtn.setBackground(new JButton().getBackground());
				}
				public void mouseReleased(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
			}
		);

		resetBtn.addMouseListener(
			new MouseListener(){
				public void mouseClicked(MouseEvent e){}
				public void mouseEntered(MouseEvent e){
					resetBtn.setBackground(Color.cyan);
				}
				public void mouseExited(MouseEvent e){
					resetBtn.setBackground(new JButton().getBackground());
				}
				public void mouseReleased(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
			}
		);

		addBtn.addMouseListener(
			new MouseListener(){
				public void mouseClicked(MouseEvent e){}
				public void mouseEntered(MouseEvent e){
					addBtn.setBackground(Color.green);
				}
				public void mouseExited(MouseEvent e){
					addBtn.setBackground(new JButton().getBackground());
				}
				public void mouseReleased(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
			}
		);


	}//end addButtons()
	
	//MENU
	/*private void addMenu(){
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		m1 = new JMenuItem("About",
			UIManager.getDefaults().getIcon("OptionPane.informationIcon")
		);
		menu.add(m1);
		menuBar.add(menu);
		super.setJMenuBar(menuBar);
	}*/
	
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
						displayArea2,
						"About this App.\nAdd Matricies on a Server!\nOh Yeah!!",
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
						displayArea2,
						"Help me App!\nEnter a valid text file in text field\n"+
						"Press Submit Button or hit Enter Key to process text file.\n"+
						"Press Add Button or hit Enter key to send matricies to Server.\n"+
						"Press Reset Button to reset text field and/or erase matricies\n",
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
						displayArea2,
						"To Exit.\nOr Not to Exit.\nThat is the Question.",
						"EXIT",
						JOptionPane.CANCEL_OPTION
					
					);
					if(userOpt == JOptionPane.OK_OPTION){
						System.out.println("Goodbye!");	
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


	//TEXT PANEL
	private void buildTextPanel(){
		textPanel.add(fileInputLabel);
		textPanel.add(userInputField);
		textPanel.add(submitBtn);
		textPanel.add(addBtn);
		textPanel.add(resetBtn);
		textPanel.add(fileNotFoundLbl,BorderLayout.PAGE_END);
	}


	//setUpUserInputField()
	private void setUpUserInputField(){
		userInputField = new JTextField(".txt",25);
		userInputField.addKeyListener(new KeyListener(){
				//once user types into textfield enable submit button
				public void keyTyped(KeyEvent e){
					if(userInputField.getText().length() > 3)
						submitBtn.setEnabled(true);
					else
						submitBtn.setEnabled(false);
				}
				public void keyReleased(KeyEvent e){
						if(e.getKeyCode() == 8||e.getKeyCode() == 127){
						//System.out.println("DELETEING");
						//System.out.println(userInputField.getText().length());
						if(userInputField.getText().length() < 5)
							submitBtn.setEnabled(false);
						else
							submitBtn.setEnabled(true);
					} // backspace/delete
					

					
				}
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						//processUserInput();
						if(submitBtn.isVisible()){
							processUserInput();
						}
						else{
							sendPacket();
						}
					}
					
				}
			}
		);
		
		userInputField.addMouseListener(
			new MouseListener(){
				public void mouseClicked(MouseEvent e){
					//set cursor in front of .txt
					//client can only "submit" .txt files
					int c = userInputField.getText().length();
					if(userInputField.getCaretPosition() > (c-4))
						userInputField.setCaretPosition(c-4);
					//reset file not found if file was not found 
					if(fileNotFoundLbl.isVisible())
						fileNotFoundLbl.setVisible(false);

				}
				public void mouseEntered(MouseEvent e){}
				public void mouseExited(MouseEvent e){}
				public void mouseReleased(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
			}
		);
	}//end setUpUserInputField()
	
	//BUILD TERMINAL OUTPUT
	private void buildOutputTerminal(){
		output = new JTextArea("Matrix Addition Client Interface\n"+
								"Version 1.0.0\n"+
								"Ready...\n",
								8,55);
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
			"Terminal Output",
			TitledBorder.CENTER,
			0,
			new Font("",Font.BOLD,18),
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

				//FIle Input Label
		fileInputLabel = new JLabel("Enter File Name: ");
		fileNotFoundLbl = new JLabel("File Not Found");
		fileNotFoundLbl.setForeground(Color.red);
		fileNotFoundLbl.setVisible(false); //if file not found visible
		
		//USER INPUT PANEL
		textPanel = new JPanel();
		textPanel.setPreferredSize(new Dimension(width,height/10));
		textPanel.setBackground(Color.lightGray);
		
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
		backgroundPanel.add(textPanel);
		backgroundPanel.add(displayPanel1);
		backgroundPanel.add(displayPanel2);
		backgroundPanel.add(displayPanel3);
		backgroundPanel.add(outputPanel);
	}
	
	private void postBuild(){
		//ADD BACKGROUND PANEL TO JFRAME; set size,visibility, & restrict resize
		add(backgroundPanel);
		setSize(700,560);
		
		//ADD ABOUT MENU
		addMenu();
		addBtn.setVisible(false); //when file is good then visible
		submitBtn.setEnabled(false);
		setVisible(true);
		setResizable(false);
	}
	//_______________________END GUI COMPONENTS METHODS________________________
	
	
}//end CLient Class
