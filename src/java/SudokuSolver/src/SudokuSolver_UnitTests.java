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
		   								   
	// Solvable
	int[][] sudoku7 = new int[][] {new int[] {6,0,0,8,9,3,0,0,0},
								   new int[] {2,0,0,0,0,0,0,8,5},
								   new int[] {3,0,0,0,0,0,0,0,9},
								   new int[] {0,0,9,7,5,1,0,3,6},
								   new int[] {0,0,8,0,6,0,0,1,0},
								   new int[] {0,0,1,0,2,0,0,7,0},
								   new int[] {0,5,0,0,0,0,7,0,0},
								   new int[] {0,7,0,0,0,9,4,2,3},
								   new int[] {0,4,0,1,7,2,0,5,0},};
								   
	int[][] sudoku8 = new int[][] {new int[] {3,0,0,0,0,0,4,7,0},
				   				   new int[] {8,1,0,5,0,0,0,0,0},
				   				   new int[] {7,2,0,8,0,9,0,5,0},
				   				   new int[] {0,8,3,7,0,0,0,0,0},
				   				   new int[] {0,0,0,0,0,1,3,9,0},
				   				   new int[] {0,0,0,0,9,4,5,0,0},
				   				   new int[] {0,0,0,4,7,5,9,0,2},
				   				   new int[] {0,0,0,0,2,0,8,0,1},
				   				   new int[] {0,6,2,0,0,0,0,0,0}};
		   								       
	int[][] sudoku9 =new int[][] {new int[] {8,4,5,9,2,6,7,1,3},
				   				  new int[] {3,1,9,8,4,0,6,5,2},
				   				  new int[] {7,6,2,3,5,0,8,9,4},
				   			      new int[] {6,5,7,4,3,0,1,8,9},
				   				  new int[] {0,0,0,0,0,0,0,0,0},
				   				  new int[] {2,8,4,1,6,0,3,7,5},
				   			      new int[] {1,7,3,5,9,0,2,6,8},
				   				  new int[] {5,2,8,6,1,0,9,4,7},
				   				  new int[] {0,0,0,0,0,0,0,0,0}};		
				   								   
	int[][] sudoku10 = {new int[] { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
						new int[] { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
						new int[] { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
						new int[] { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
						new int[] { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
						new int[] { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
						new int[] { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
						new int[] { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
						new int[] { 0, 9, 0, 0, 0, 0, 4, 0, 0 }};	
	
	// Non_Solvable
	int[][] sudoku11 = new int[][] {new int[] {8,4,5,9,2,0,7,1,3},
			   						new int[] {0,1,9,8,4,7,0,5,0},
			   						new int[] {7,0,0,3,5,1,8,9,4},
			   						new int[] {6,5,7,4,0,2,1,8,9},
			   						new int[] {9,3,0,7,8,5,0,2,6},
			   						new int[] {2,8,4,1,0,9,3,7,5},
			   						new int[] {1,7,3,5,5,4,2,6,8},
			   						new int[] {5,2,0,0,0,3,0,4,7},
			   						new int[] {4,9,6,2,7,8,5,3,1}};		
								   
	//*********************************************************************************************
	// GetPossibleNumbersForCell		   						      
	//*********************************************************************************************
	@Test
	void GetSolution_Solvable1() {		
		boolean result = SudokuSolver.GetSolution(sudoku1);
		assertEquals(result, true);			
	}			   						
			   						
	@Test
	void GetSolution_Solvable2() {		
		boolean result = SudokuSolver.GetSolution(sudoku7);
		assertEquals(result, true);			
	}	
	
	@Test
	void GetSolution_Solvable3() {		
		boolean result = SudokuSolver.GetSolution(sudoku8);
		assertEquals(result, true);			
	}			
	
	@Test
	void GetSolution_Solvable4() {		
		boolean result = SudokuSolver.GetSolution(sudoku9);
		assertEquals(result, true);			
	}		
	
	@Test
	void GetSolution_Solvable5() {		
		boolean result = SudokuSolver.GetSolution(sudoku10);
		assertEquals(result, true);			
	}		
	
	@Test
	void GetSolution_NonSolvable() {		
		boolean result = SudokuSolver.GetSolution(sudoku11);
		assertEquals(result, false);			
	}		
	//*********************************************************************************************
		   									   							       
	//*********************************************************************************************
	// GetPossibleNumbersForCell		   						      
	//*********************************************************************************************			   						       
	@Test
	void GetPossibleNumbersForCell_EmptySudoku() {		
		boolean[] result = SudokuSolver.GetPossibleNumbers(sudoku1,2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,true,true,true,true,true,true,true}), true);			
	}
	
	@Test
	void GetPossibleNumbersForCell_RegularTest() {		
		boolean[] result = SudokuSolver.GetPossibleNumbers(sudoku2,2,2);
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
