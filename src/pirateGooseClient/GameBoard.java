package pirateGooseClient;


public class GameBoard {
	
	int[][] grid = new int[10][9];
	
	public void createGrid() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				grid[i][j] = 0;
			}
		}
		// grog
		grid[3][8] = 1;
		grid[2][7] = 1;
		grid[7][5] = 1;
		grid[2][3] = 1;
		grid[6][1] = 1;
		
		// maps
		grid[5][8] = 2;
		grid[9][6] = 2;
		grid[2][4] = 2;
		grid[0][1] = 2;
		
		// mermaids
		grid[8][7] = -1;
		grid[2][1] = -1;
		grid[0][5] = -1;
		grid[6][3] = -1;
		grid[4][6] = -1;
		
		// cannons
		grid[3][2] = -2;
		grid[7][0] = -2;
		grid[4][5] = -2;
		grid[6][8] = -2;
		
		// the big chtulhu
		grid[5][4] = -45;
		
		grid[9][0] = 100;
		
	}
	
	public int getCellValue(int x, int y) {
		return grid[x][y];
	}
}