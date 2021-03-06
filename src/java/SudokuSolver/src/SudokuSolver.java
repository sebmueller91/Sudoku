import java.util.LinkedList;
import java.util.List;

public class SudokuSolver {
	private Logger logger;
	
	public SudokuSolver(Logger logger) {
		this.logger = logger;
	}
	
	// Returns true if a solution for the given Field was found, false if no solution for the given Field exists
	// For the true case, the variable "field" will be filled with the found solution 
	public static boolean GetSolution(int[][] field) {		
		// Some error in sudoku -> Can not be solved
		if (!SudokuIsValid(field)) {
			return false;
		}
		
		// No errors + completely filled -> we have found a solution
		if (SudokuIsCompletelyFilled(field)) {
			return true;
		}
		
		// Try every possibility
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
	            if (field[i][j] == 0) {
	            	boolean[] possibleNumbers = GetPossibleNumbers(field,i,j);
	                for (int k = 0; k < possibleNumbers.length; k++) {
	                	if (possibleNumbers[k]) {
	                		field[i][j] = k+1;
	                		if (GetSolution(field)) {
	                			// Solution found -> Reach it back up
	                			return true;
	                		}
	                		field[i][j] = 0;
	                	}
	                }
	                return false;
	            }
	        }
	    }
	    return false;
	}
	
	// Returns an arrary of boolean with length 9 that indicates which numbers could be added into this cell without breaking the Sudoku rules
	protected static boolean[] GetPossibleNumbers(int[][] field, int row, int col) {		
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
}
