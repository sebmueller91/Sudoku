import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays; 

import org.junit.jupiter.api.Test;

class SudokuSolver_UnitTests {
	// Empty
	private int[][] sudoku1 = new int[][] {new int[] {0,0,0,0,0,0,0,0,0}, 
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0}};
    // Valid
	private int[][] sudoku2 = new int[][] {new int[] {0,0,0,0,0,0,0,6,0},
			   						       new int[] {0,3,0,0,0,0,0,0,0},
			   						       new int[] {0,0,0,0,0,4,0,7,0},
			   						       new int[] {0,0,0,0,0,0,0,0,0},
			   						       new int[] {0,0,0,0,0,0,0,0,0},
			   						       new int[] {0,0,7,0,0,0,0,8,0},
			   						       new int[] {0,0,0,0,0,0,0,0,0},
			   						       new int[] {0,0,0,0,0,5,0,0,0},
			   						       new int[] {0,0,0,0,0,0,0,0,0}};
    // Invalid (Row conflict) 
	private int[][] sudoku3 = new int[][] {new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,4,0,0,0,0,0,4},
		   							       new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,0,0,0,0,0,0,0},
		   							       new int[] {0,0,0,0,0,0,0,0,0}};			
	// Invalid (Column conflict) 
	private int[][] sudoku4 = new int[][] {new int[] {0,0,0,0,0,8,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,8,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0}};	
	// Invalid (Box conflict) 
	private int[][] sudoku5 = new int[][] {new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {6,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,0,0,0,0,0,0,0},
		   								   new int[] {0,0,6,0,0,0,0,0,0}};	
	// Solved
	private int[][] sudoku6 = new int[][] {new int[] {8,4,5,9,2,6,7,1,3},
		   								   new int[] {3,1,9,8,4,7,6,5,2},
		   								   new int[] {7,6,2,3,5,1,8,9,4},
		   								   new int[] {6,5,7,4,3,2,1,8,9},
		   								   new int[] {9,3,1,7,8,5,4,2,6},
		   								   new int[] {2,8,4,1,6,9,3,7,5},
		   								   new int[] {1,7,3,5,9,4,2,6,8},
		   								   new int[] {5,2,8,6,1,3,9,4,7},
		   								   new int[] {4,9,6,2,7,8,5,3,1}};			   								   
		   									   							       
		   									   							       
	//*********************************************************************************************
	// GetPossibleNumbersForCell		   						      
	//*********************************************************************************************			   						       
	@Test
	void GetPossibleNumbersForCell_EmptySudoku() {		
		boolean[] result = SudokuSolver.GetPossibleNumbersForCell(sudoku1,2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,true,true,true,true,true,true,true}), true);			
	}
	
	@Test
	void GetPossibleNumbersForCell_RegularTest() {		
		boolean[] result = SudokuSolver.GetPossibleNumbersForCell(sudoku2,2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,false,false,true,true,false,true,true}), true);			
	}
	//*********************************************************************************************

	
	//*********************************************************************************************
	// SudokuContainsNoErrors		   						      
	//*********************************************************************************************		
	@Test
	void SudokuContainsNoErrors_NoErrorEmpty() {		
		boolean result = SudokuSolver.SudokuIsValid(sudoku1);
		assertEquals(result, true);			
	}
	
	@Test
	void SudokuContainsNoErrors_NoErrorFilled() {		
		boolean result = SudokuSolver.SudokuIsValid(sudoku2);
		assertEquals(result, true);			
	}
	
	@Test
	void SudokuContainsNoErrors_RowError() {		
		boolean result = SudokuSolver.SudokuIsValid(sudoku3);
		assertEquals(result, false);			
	}
	
	@Test
	void SudokuContainsNoErrors_ColumnError() {		
		boolean result = SudokuSolver.SudokuIsValid(sudoku4);
		assertEquals(result, false);			
	}
	
	@Test
	void SudokuContainsNoErrors_BoxError() {		
		boolean result = SudokuSolver.SudokuIsValid(sudoku5);
		assertEquals(result, false);			
	}
	
	@Test
	void SudokuContainsNoErrors_Solved() {		
		boolean result = SudokuSolver.SudokuIsValid(sudoku6);
		assertEquals(result, true);			
	}
	//*********************************************************************************************
	
	//*********************************************************************************************
	// SudokuIsCompletelyFilled 						      
	//*********************************************************************************************		
	@Test
	void SudokuIsCompletelyFilled_False_Empty() {		
		boolean result = SudokuSolver.SudokuIsCompletelyFilled(sudoku1);
		assertEquals(result, false);			
	}
	
	@Test
	void SudokuIsCompletelyFilled_False_PatrtiallyFilled() {		
		boolean result = SudokuSolver.SudokuIsCompletelyFilled(sudoku5);
		assertEquals(result, false);			
	}
	
	@Test
	void SudokuIsCompletelyFilled_False_Solved() {		
		boolean result = SudokuSolver.SudokuIsCompletelyFilled(sudoku6);
		assertEquals(result, true);			
	}
	//*********************************************************************************************
}
