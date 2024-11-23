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

	private JTextArea output;

	private File file;
	private Scanner inputScanner;

	//GUI
	public Client(){
		super("Client: Matrix Addition");

		int height = 800;
		int width = 600;
		Dimension frameD = new Dimension(width,height);
		backgroundPanel = new JPanel();
		backgroundPanel.setPreferredSize(frameD);

		submitBtn = new JButton("SUBMIT");
		addBtn = new JButton("ADD");

		output = new JTextArea();
		output.setPreferredSize(new Dimension(width,height/5));
		output.setBackground(Color.gray);
		output.setBorder(BorderFactory.createTitledBorder("Terminal Output"));
		
		fileInputLabel = new JLabel("Enter File Name: ");
		textPanel = new JPanel();
		textPanel.setPreferredSize(new Dimension(width,height/10));
		textPanel.setBackground(Color.red);
		
		displayPanel1 = new JPanel();
		displayPanel1.setPreferredSize(new Dimension(width/3,height/3));
		displayPanel1.setBackground(Color.yellow);

		displayPanel2 = new JPanel();
		displayPanel2.setPreferredSize(new Dimension(width/3,height/3));
		displayPanel2.setBackground(Color.cyan);

		displayPanel3 = new JPanel();
		displayPanel3.setPreferredSize(new Dimension(width/3,height/3));
		displayPanel3.setBackground(Color.green);

		userInputField = new JTextField(" .txt",25);
		displayArea1 = new JTextArea("1,1,1\n2,2,2\n3,3,3");
		
		//m1.setPreferredSize(new Dimension(50,50));
		
		displayArea2 = new JTextArea("1,1,1\n2,2,2\n3,3,3");
		
		//m2.setPreferredSize(new Dimension(50,50));

		displayArea3 = new JTextArea("1,1,1\n2,2,2\n3,3,3");
		
		//m3.setPreferredSize(new Dimension(50,50));

		textPanel.add(fileInputLabel);
		textPanel.add(userInputField);
		textPanel.add(submitBtn);
		//addBtn.setVisible(false);
		textPanel.add(addBtn);

		//displayPanel1.add(m1);
		displayPanel1.add(displayArea1);
		displayPanel1.setBorder(BorderFactory.createTitledBorder("Matrix A"));
		//displayPanel2.add(m2);
		displayPanel2.add(displayArea2);
		displayPanel2.setBorder(BorderFactory.createTitledBorder("Matrix B"));
		//displayPanel3.add(m3);
		displayPanel3.add(displayArea3);
		displayPanel3.setBorder(BorderFactory.createTitledBorder("Matrix Sum"));


		backgroundPanel.add(textPanel);
		backgroundPanel.add(displayPanel1);
		backgroundPanel.add(displayPanel2);
		backgroundPanel.add(displayPanel3);
		backgroundPanel.add(output);

		add(backgroundPanel);
		setSize(700,600);
		//setSize(frameD);
		setVisible(true);

	}




}//end CLient Class
