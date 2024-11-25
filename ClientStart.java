/**
Programmer: Lucio Plancarte
Created: 23-NOV-2024
*/

import javax.swing.JFrame;
public class ClientStart{

	public static void main(String[] args){
		Client app = new Client();
		app.runClient();
		app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
}
