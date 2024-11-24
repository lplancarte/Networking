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
import javax.swing.UIManager;


public class Client extends JFrame{


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

	//GUI
	public Client(){
		super("Client: Matrix Addition");

		menuBar = new JMenuBar();
		menu = new JMenu("File");
		m1 = new JMenuItem("About",
			UIManager.getDefaults().getIcon("OptionPane.informationIcon")
		);
		menu.add(m1);
		menuBar.add(menu);
		super.setJMenuBar(menuBar);

		int height = 800;
		int width = 600;
		Dimension frameD = new Dimension(width,height);
		backgroundPanel = new JPanel();
		backgroundPanel.setPreferredSize(frameD);


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

		userInputField = new JTextField(" .txt",25);
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

		addBtn = new JButton("ADD");
		addBtn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sendPacket();
				}
			}
		);

		output = new JTextArea("Matrix Addition Client Interface\n"+
								"Version 1.0.0\n"+
								"Ready...\n",
								8,55);
		output.setWrapStyleWord(true);
		output.setLineWrap(true);
		output.setForeground(Color.green);
		output.setBackground(Color.black);
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
		output.setEditable(false);

		fileInputLabel = new JLabel("Enter File Name: ");
		fileNotFoundLbl = new JLabel("File Not Found");
		fileNotFoundLbl.setForeground(Color.red);
		textPanel = new JPanel();
		textPanel.setPreferredSize(new Dimension(width,height/10));
		textPanel.setBackground(Color.lightGray);
		
		displayPanel1 = new JPanel();
		displayPanel1.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel1.setBackground(Color.gray);

		displayPanel2 = new JPanel();
		displayPanel2.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel2.setBackground(Color.gray);

		displayPanel3 = new JPanel();
		displayPanel3.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel3.setBackground(Color.darkGray);

		//userInputField = new JTextField(" .txt",25);
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

		submitBtn.addMouseListener(
			new MouseListener(){
				public void mouseClicked(MouseEvent e){}
				public void mouseEntered(MouseEvent e){
					if(!userInputField.getText().equals(".txt")){
						submitBtn.setBackground(Color.green);
						submitBtn.setEnabled(true);
					}
				}
				public void mouseExited(MouseEvent e){
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

		textPanel.add(fileInputLabel);
		textPanel.add(userInputField);
		textPanel.add(submitBtn);
		addBtn.setVisible(false); //when file is good then visible
		submitBtn.setEnabled(false);


		textPanel.add(addBtn);
		textPanel.add(resetBtn);
		textPanel.add(fileNotFoundLbl,BorderLayout.PAGE_END);
		fileNotFoundLbl.setVisible(false); //if file not found visible

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


		backgroundPanel.add(textPanel);
		backgroundPanel.add(displayPanel1);
		backgroundPanel.add(displayPanel2);
		backgroundPanel.add(displayPanel3);
		backgroundPanel.add(outputPanel);

		add(backgroundPanel);
		setSize(700,560);
		setVisible(true);
		setResizable(false);

	}


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
	*resetUserInput() resets GUI
	*/
	private void resetUserInput(){
		System.out.println("Reset Button Pressed");
		userInputField.setText(".txt");
		submitBtn.setVisible(true);
		submitBtn.setEnabled(false);
		addBtn.setVisible(false);
		userInputField.setEditable(true);
		
	}


	private void sendPacket(){
		System.out.println("Add Button Pressed");
		output.append("Sending Matrices to Server.\nAwaiting Response...\n");
	}


}//end CLient Class
