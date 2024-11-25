/**
Programmer: Lucio Plancarte
Created: 24-NOV-2024
*/

import javax.swing.JFrame;
public class ServerStart{

	public static void main(String[] args){
		Server app = new Server();
		app.runServer();
		app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
}
