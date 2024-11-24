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

public class Client extends JFrame{


	private JPanel backgroundPanel;

	private JTextField userInputField;
	private JPanel textPanel;
	private JButton submitBtn;
	private JButton addBtn;

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

	private File file;
	private Scanner inputScanner;

	/*private String dummyData =  "_,_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_,_\n"+
								"_,_,_,_,_,_,_,_";*/
	private String dummyData = "";

	//GUI
	public Client(){
		super("Client: Matrix Addition");
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
					System.out.println("Submit Button Pressed");
					String input = userInputField.getText();
					System.out.println(input);
					output.append("Opening: "+ input);
					output.append("\nFile Processed\n");
					//submitBtn.setVisible(false);
					//addBtn.setVisible(true);

				}
			}
		);
		userInputField = new JTextField(" .txt",25);
		userInputField.addKeyListener(new KeyListener(){
				public void keyTyped(KeyEvent e){}
				public void keyReleased(KeyEvent e){}
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						//TODO: put into method
						System.out.println("Submit Button Pressed");
						String input = userInputField.getText();
						System.out.println(input);
						output.append("Opening: "+ input);
						output.append("\nFile Processed\n");
					}
				}
			}
		);

		addBtn = new JButton("ADD");

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

		outputPanel.setBorder(BorderFactory.createTitledBorder("Terminal"+ 
														"Output"));

		fileInputLabel = new JLabel("Enter File Name: ");
		fileNotFoundLbl = new JLabel("File Not Found");
		fileNotFoundLbl.setForeground(Color.red);
		textPanel = new JPanel();
		textPanel.setPreferredSize(new Dimension(width,height/10));
		textPanel.setBackground(Color.lightGray);
		
		displayPanel1 = new JPanel();
		displayPanel1.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel1.setBackground(Color.lightGray);

		displayPanel2 = new JPanel();
		displayPanel2.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel2.setBackground(Color.cyan);

		displayPanel3 = new JPanel();
		displayPanel3.setPreferredSize(new Dimension(width/3,height/4));
		displayPanel3.setBackground(Color.green);

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
		displayArea1 = new JTextArea(dummyData);
		displayArea1.setFont(new Font("Bookman Old Style", Font.BOLD,19));
		displayArea2 = new JTextArea(dummyData);
		displayArea2.setFont(new Font("Algerian", Font.BOLD,19));
		displayArea3 = new JTextArea(dummyData);
		displayArea3.setFont(new Font("Broadway", Font.BOLD,19));

		textPanel.add(fileInputLabel);
		textPanel.add(userInputField);
		textPanel.add(submitBtn);
		addBtn.setVisible(false); //when file is good then visible
		textPanel.add(addBtn);
		textPanel.add(fileNotFoundLbl,BorderLayout.PAGE_END);
		fileNotFoundLbl.setVisible(false); //if file not found visible

		displayPanel1.add(displayArea1);
		displayPanel1.setBorder(BorderFactory.createTitledBorder("Matrix A"));

		displayPanel2.add(displayArea2);
		displayPanel2.setBorder(BorderFactory.createTitledBorder("Matrix B"));

		displayPanel3.add(displayArea3);
		displayPanel3.setBorder(BorderFactory.createTitledBorder("Matrix Sum"));


		backgroundPanel.add(textPanel);
		backgroundPanel.add(displayPanel1);
		backgroundPanel.add(displayPanel2);
		backgroundPanel.add(displayPanel3);
		backgroundPanel.add(outputPanel);

		add(backgroundPanel);
		setSize(700,600);
		setVisible(true);

	}




}//end CLient Class
