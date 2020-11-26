import java.util.LinkedList;
import java.util.List;

public class SudokuSolver {

	private static boolean debug = false;
	private static Logger logger = new Logger(Logger.LogLevel.Error, new ConsoleLogger());
	public static void main(String[] args) {
		int[][] sudoku1 = new int[][] {new int[] {6,0,0,8,9,3,0,0,0},
									   new int[] {2,0,0,0,0,0,0,8,5},
									   new int[] {3,0,0,0,0,0,0,0,9},
									   new int[] {0,0,9,7,5,1,0,3,6},
									   new int[] {0,0,8,0,6,0,0,1,0},
									   new int[] {0,0,1,0,2,0,0,7,0},
									   new int[] {0,5,0,0,0,0,7,0,0},
									   new int[] {0,7,0,0,0,9,4,2,3},
									   new int[] {0,4,0,1,7,2,0,5,0},};
									   
        int[][] sudoku2 = new int[][] {new int[] {8,4,5,9,2,0,7,1,3},
			   						   new int[] {0,1,9,8,4,7,0,5,0},
			   						   new int[] {7,0,0,3,5,1,8,9,4},
			   						   new int[] {6,5,7,4,0,2,1,8,9},
			   						   new int[] {9,3,0,7,8,5,0,2,6},
			   						   new int[] {2,8,4,1,0,9,3,7,5},
			   						   new int[] {1,7,3,5,5,4,2,6,8},
			   						   new int[] {5,2,0,0,0,3,0,4,7},
			   						   new int[] {4,9,6,2,7,8,5,3,1}};			
			   						   
		int[][] sudoku3 = new int[][] {new int[] {3,0,0,0,0,0,4,7,0},
				   					   new int[] {8,1,0,5,0,0,0,0,0},
				   					   new int[] {7,2,0,8,0,9,0,5,0},
				   					   new int[] {0,8,3,7,0,0,0,0,0},
				   					   new int[] {0,0,0,0,0,1,3,9,0},
				   					   new int[] {0,0,0,0,9,4,5,0,0},
				   					   new int[] {0,0,0,4,7,5,9,0,2},
				   					   new int[] {0,0,0,0,2,0,8,0,1},
				   					   new int[] {0,6,2,0,0,0,0,0,0}};
				   					   
		int[][] sudoku4 = new int[][] {new int[] {0,0,0,0,0,0,0,0,0}, 
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0},
		   							   new int[] {0,0,0,0,0,0,0,0,0}};
		   								       
		int[][] sudoku5= new int[][] {new int[] {8,4,5,9,2,6,7,1,3},
				   					  new int[] {3,1,9,8,4,0,6,5,2},
				   					  new int[] {7,6,2,3,5,0,8,9,4},
				   					  new int[] {6,5,7,4,3,0,1,8,9},
				   					  new int[] {0,0,0,0,0,0,0,0,0},
				   					  new int[] {2,8,4,1,6,0,3,7,5},
				   					  new int[] {1,7,3,5,9,0,2,6,8},
				   					  new int[] {5,2,8,6,1,0,9,4,7},
				   					  new int[] {0,0,0,0,0,0,0,0,0}};		
				   								   
		int[][] sudoku6 = {new int[] { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
						   new int[] { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
						   new int[] { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
						   new int[] { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
						   new int[] { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
						   new int[] { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
						   new int[] { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
						   new int[] { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
						   new int[] { 0, 9, 0, 0, 0, 0, 4, 0, 0 }};	
		
		LinkedList<int[][]> sudokus = new LinkedList<int[][]>();
		//sudokus.add(sudoku1);
		//sudokus.add(sudoku2);
		//sudokus.add(sudoku3);
		//sudokus.add(sudoku4);
		//sudokus.add(sudoku5);
		sudokus.add(sudoku6);
			   	
		for (int i = 0; i < sudokus.size(); i++) {			
			if (GetSolutionForSudoku(sudokus.get(i))) {
				System.out.println("The solution of sudoku "+ i + " is:");
				PrintSudoku(sudokus.get(i));
			} else {
				System.out.println("The sudoku " + i + " has no solution");
			}			
			System.out.println();
		}
	}
	
	// Returns true if a solution for the given Field was found, false if no solution for the given Field exists
	// For the true case, the variable "field" will be filled with the found solution 
	private static boolean GetSolutionForSudoku(int[][] field) {
		logger.LogDebug("GetSolutionForSudoku"); 
		
		// Some error in sudoku -> Can not be solved
		if (!SudokuIsValid(field)) {
			return false;
		}
		
		// No errors + completely filled -> we have found a solution
		if (SudokuIsCompletelyFilled(field)) {
			return true;
		}
		
		// If there is only one cell that has no possibilities left, we are at a dead end
		boolean[][][] possibleNumbersField = new boolean[9][9][9];
		if (!GetPossibleNumbersForAllCells(field, possibleNumbersField)) {
			return false;
		}		
		
		// Try every possibility
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {		
	            if (field[i][j] == 0) { // for empty cells
	                for (int k = 0; k < possibleNumbersField[i][j].length; k++) { // Iterate over all numbers 1-9
	                	if (possibleNumbersField[i][j][k]) { // Only if the current number (k+1) can be filled in this cell
	                		field[i][j] = k+1; // fill the current cell with the possible number
	                		if (GetSolutionForSudoku(field)) { // check if sub-sudoku is solvable
	                			// Solution found -> Reach it back up
	                			return true;
	                		} else { 
	                			field[i][j] = 0; // no solution for this sub-sudoku exists -> Empty the cell again
	                		}
	                	}
	                }
	                return false;
	            }
	        }
	    }
	    return false;
	}
	
	// Fills the given variable "possibleNumbersField"
	// For each empty cell in "field", a boolean array of length 9 will be created that contains all possible numbers for this cell
	// Returns true if there is at least one possibility left for each empty cell, false otherwise
	private static boolean GetPossibleNumbersForAllCells(int[][] field, boolean[][][] possibleNumbersField) {
		logger.LogDebug("GetPossibleNumbersForAllCells()");
		logger.LogWarning("GetPossibleNumbersForAllCells()");
		logger.LogError("GetPossibleNumbersForAllCells()");
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if (field[i][j] == 0) {
					possibleNumbersField[i][j] = GetPossibleNumbersForCell(field,i,j);
					boolean possibilitiesLeftForCurrentCell = false;
					for (int k = 0; k < possibleNumbersField.length; k++) {
						if (possibleNumbersField[i][j][k]) {
							possibilitiesLeftForCurrentCell = true;
						}
					}
					if (!possibilitiesLeftForCurrentCell) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// Returns an arrary of bool with length 9 that indicates which numbers could be added into this cell without breaking the Sudoku rules
	protected static boolean[] GetPossibleNumbersForCell(int[][] field, int row, int col) {		
		logger.LogDebug("GetPossibleNumbersForCell()");
		
		// Array that contains pre-set of possible numbers
		boolean[] possibleNumbers = new boolean[9];
		for (int i = 0; i < 9; i++) {
			possibleNumbers[i] = true;
		}
		
		for (int i = 0; i < 9; i++) {
			// Remove numbers that are present in column
			if (i != col && field[row][i] != 0) {
				possibleNumbers[field[row][i]-1] = false;
			}
			// Remove numbers that are present in row
			if (i != row && field[i][col] != 0) {
				possibleNumbers[field[i][col]-1] = false;
			}
		}
		
		// Remove numbers that are present in block
		int blockStartRow = (row/3)*3; // Get top left indices of current block
		int blockStartCol = (col/3)*3;
		// Traverse block and remove present numbers		
		for (int i = blockStartRow; i < blockStartRow + 3; i++) {
			for (int j = blockStartCol; j < blockStartCol + 3; j++) {
				if (i != row && j != col && field[i][j] != 0) {
					possibleNumbers[field[i][j]-1] = false;
				}
			}
		}
		
		return possibleNumbers;
	}
	
	// Returns true if all cells of the field are containing a number, false otherwise (if any cell is empty)
	protected static boolean SudokuIsCompletelyFilled(int[][] field) {
		PrintDebug("SudokuIsCompletelyFilled()");
		
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if (field[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	// Returns true if there are no collisions between numbers in the Sudoku, false if there is any error
	protected static boolean SudokuIsValid(int[][] field) {
		PrintDebug("SudokuIsValid()");
		
		// Check rows for duplicate entries
		for (int i = 0; i < 9; i++) {
			boolean[] numbers = new boolean[9];
			for (int j = 0; j < 9; j++) {
				if (field[i][j] != 0) { // Only check if cell is not empty
					if (numbers[field[i][j]-1] == false) { // Number has not been present in this row before
						numbers[field[i][j]-1] = true; // Mark number as present in this row
					} else {
						return false; // Number is present twice in this row
					}	
				}
			}
		}
	
		// Check columns for duplicate entries
		for (int i = 0; i < 9; i++) {
			boolean[] numbers = new boolean[9];
			for (int j = 0; j < 9; j++) {
				if (field[j][i] != 0) { // Only if cell is not empty
					if (numbers[field[j][i]-1] == false) { // Numbers has not been present in this column
						numbers[field[j][i]-1] = true; // Mark number as present in this column	
					} else {
						return false; // Number is present twice in this column
					}				
				}
			}
		}
		
		// Check blocks for duplicate entries
		for (int i = 0; i < 9; i = i+3) {
			for (int j = 0; j < 9; j=j+3) {
				// i and j will be the indices of the left top of each block at this point
					
				// Traverse the individual 3x3 blocks
				boolean[] numbers = new boolean[9];						
				for (int ii = i; ii < i+3; ii++) {
					for (int jj = j; jj < j+3; jj++) {					
						if (field[ii][jj] != 0) { // Only if cell is not empty			
							if (numbers[field[ii][jj]-1] == false) { // Number has not been present in this block	
								numbers[field[ii][jj]-1] = true; // Mark number as present in this block							
							} else { // Number is present twice in this block
								return false;
							}
						}
						
					}
				}
			}
		}
		
		// No errors found
		return true;
	}

	private static void PrintDebug(String message) {
		if (debug) {
			System.out.println(message);
		}
	}
	
	private static void PrintSudoku(int[][] field) {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				System.out.print(field[i][j] + " ");
			}
			System.out.println();
		}
	}
}
