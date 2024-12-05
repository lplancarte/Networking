/**
Programmer: Lucio Plancarte
Created: 31-OCT-2024
Modified: 01-DEC-2024
ChangeLog: 
		Rework of run();set up variables for indeces and reduced number of for-loops
Description: extends Thread and performs submatrix addition
*/

/**
ThreadOperation constructor takes in two matrices and a string to indicate
quadrant. The result is stored in 2-D array C.
*/

enum Quadrant {
		I,
		II,
		III,
		IV
	};

public class ThreadOperation extends Thread{

	//Class Variables
	private int[][] A;
	private int[][] B;
	private int[][] C;

	private Quadrant q;
	//https://www.w3schools.com/java/java_enums.asp

	//Constructor
	public ThreadOperation(int[][] A, int[][] B, int[][] C, Quadrant q){
		this.A = A;
		this.B = B;
		this.C = C;
		this.q = q;
	}

	@Override
	public void run(){
		//get row and col from A
		int ROW = A.length;
		int COL = A[0].length;
		//Set Start indexes into variables
		int iStart = 0;
		int jStart = 0;
		switch(q){

			case I:{
				ROW = ROW/2;
				COL = COL/2;
			};
			break;
			case II:{
				jStart = COL/2;
				ROW = ROW/2;
			};
			break;
			case III:{
				iStart = ROW/2;
				COL = COL/2;
			};
			break;
			case IV:{
				iStart = ROW/2;
				jStart = COL/2;
			};
			break;

		}
		//reduced to single loop
		for(int i = iStart; i < ROW;i++){
					for(int j = jStart; j < COL;j++){
						C[i][j] = A[i][j] + B[i][j];
					}
				}
		/*switch(q){

			case I:{
				for(int i=0; i<(ROW/2);i++){
					for(int j=0; j<(COL/2);j++){
						C[i][j] = A[i][j] + B[i][j];
					}
				}
			};
			break;
			case II:{
				for(int i=0; i<(ROW/2);i++){
					for(int j=(COL/2); j<COL;j++){
						C[i][j] = A[i][j] + B[i][j];
					}
				}
			};
			break;
			case III:{
				for(int i=(ROW/2); i<ROW;i++){
					for(int j=0; j<(COL/2);j++){
						C[i][j] = A[i][j] + B[i][j];
					}
				}
			};
			break;
			case IV:{
				for(int i=(ROW/2); i<ROW;i++){
					for(int j=(COL/2); j<COL;j++){
						C[i][j] = A[i][j] + B[i][j];
					}
				}
			};
			break;

		}*/



	}

	//Not used.
	public int[][] getResult(){
		return this.C;
	}





}
