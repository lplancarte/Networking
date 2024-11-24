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

/**

*/
public class Client extends JFrame{

	private static final int HEIGHT = 800;
	private static final int WIDTH = 600;

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
//---------------------------------------------
	private File file;
	private Scanner inputScanner;


	private String dummyData ="_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_";
	//private String dummyData = "";


	public Client(){
		//GUI
		super("Client: Matrix Addition");	
		buildGUI();



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
		output.append("Opening: "+ input);
		//TODO: Validate user input
		output.append("\nFile Processed\n");
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
		
	}

	/**
	*sendPacket is used when add button is pressed, sends object to server
		in TODO stage
	*/
	private void sendPacket(){
		System.out.println("Add Button Pressed");
		output.append("Sending Matrices to Server.\nAwaiting Response...\n");
		//TODO:disable reset button and add button until server responds
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
				public void keyTyped(KeyEvent e){
					if(userInputField.getText().length() > 4)
						submitBtn.setEnabled(true);
					else
						submitBtn.setEnabled(false);
				}
				public void keyReleased(KeyEvent e){}
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						processUserInput();
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
