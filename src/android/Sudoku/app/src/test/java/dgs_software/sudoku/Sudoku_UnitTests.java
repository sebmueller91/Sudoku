package dgs_software.sudoku;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;


public class Sudoku_UnitTests {
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

	private static Sudoku sudoku;
								   
	//*********************************************************************************************
	// GetPossibleNumbersForCell		   						      
	//*********************************************************************************************
	@Test
	public void GetSolution_Solvable1() {
	    sudoku = CreateSudokuFromInts(sudoku1);
		boolean result = sudoku.GetSolution();
		assertEquals(result, true);			
	}			   						
			   						
	@Test
	public void GetSolution_Solvable2() {
        sudoku = CreateSudokuFromInts(sudoku7);
        boolean result = sudoku.GetSolution();
		assertEquals(result, true);			
	}	
	
	@Test
	public void GetSolution_Solvable3() {
        sudoku = CreateSudokuFromInts(sudoku8);
        boolean result = sudoku.GetSolution();
		assertEquals(result, true);			
	}			
	
	@Test
	public void GetSolution_Solvable4() {
        sudoku = CreateSudokuFromInts(sudoku9);
        boolean result = sudoku.GetSolution();
		assertEquals(result, true);			
	}		
	
	@Test
	public void GetSolution_Solvable5() {
        sudoku = CreateSudokuFromInts(sudoku10);
        boolean result = sudoku.GetSolution();
		assertEquals(result, true);			
	}		
	
	@Test
	public void GetSolution_NonSolvable() {
        sudoku = CreateSudokuFromInts(sudoku11);
        boolean result = sudoku.GetSolution();
        assertEquals(result, false);
	}		
	//*********************************************************************************************
		   									   							       
	//*********************************************************************************************
	// GetPossibleNumbersForCell		   						      
	//*********************************************************************************************			   						       
	@Test
	public void GetPossibleNumbersForCell_EmptySudoku() {
        sudoku = CreateSudokuFromInts(sudoku1);
		boolean[] result = sudoku.GetPossibleNumbers(2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,true,true,true,true,true,true,true}), true);			
	}
	
	@Test
	public void GetPossibleNumbersForCell_RegularTest() {
        sudoku = CreateSudokuFromInts(sudoku2);
		boolean[] result = sudoku.GetPossibleNumbers(2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,false,false,true,true,false,true,true}), true);			
	}
	//*********************************************************************************************

	
	//*********************************************************************************************
	// SudokuContainsNoErrors		   						      
	//*********************************************************************************************		
	@Test
	public void SudokuContainsNoErrors_NoErrorEmpty() {
        sudoku = CreateSudokuFromInts(sudoku1);
		boolean result = sudoku.SudokuIsValid();
		assertEquals(result, true);			
	}
	
	@Test
	public void SudokuContainsNoErrors_NoErrorFilled() {
        sudoku = CreateSudokuFromInts(sudoku2);
		boolean result = sudoku.SudokuIsValid();
		assertEquals(result, true);			
	}
	
	@Test
	public void SudokuContainsNoErrors_RowError() {
        sudoku = CreateSudokuFromInts(sudoku3);
		boolean result = sudoku.SudokuIsValid();
		assertEquals(result, false);			
	}
	
	@Test
	public void SudokuContainsNoErrors_ColumnError() {
        sudoku = CreateSudokuFromInts(sudoku4);
		boolean result = sudoku.SudokuIsValid();
		assertEquals(result, false);			
	}
	
	@Test
	public void SudokuContainsNoErrors_BoxError() {
        sudoku = CreateSudokuFromInts(sudoku5);
		boolean result = sudoku.SudokuIsValid();
		assertEquals(result, false);			
	}
	
	@Test
	public void SudokuContainsNoErrors_Solved() {
        sudoku = CreateSudokuFromInts(sudoku6);
		boolean result = sudoku.SudokuIsValid();
		assertEquals(result, true);			
	}
	//*********************************************************************************************
	
	//*********************************************************************************************
	// SudokuIsCompletelyFilled 						      
	//*********************************************************************************************		
	@Test
	public void SudokuIsCompletelyFilled_False_Empty() {
        sudoku = CreateSudokuFromInts(sudoku1);
		boolean result = sudoku.SudokuIsCompletelyFilled();
		assertEquals(result, false);			
	}
	
	@Test
	public void SudokuIsCompletelyFilled_False_PatrtiallyFilled() {
        sudoku = CreateSudokuFromInts(sudoku5);
		boolean result = sudoku.SudokuIsCompletelyFilled();
		assertEquals(result, false);			
	}
	
	@Test
	public void SudokuIsCompletelyFilled_False_Solved() {
        sudoku = CreateSudokuFromInts(sudoku6);
		boolean result = sudoku.SudokuIsCompletelyFilled();
		assertEquals(result, true);			
	}
	//*********************************************************************************************

    private static Sudoku CreateSudokuFromInts(int[][] field) {
	    Cell[][] cellArray = new Cell[field.length][field.length];
	    for (int i = 0; i < field.length; i++) {
	        for (int j = 0; j < field.length; j++) {
	            cellArray[i][j] = new Cell(field[i][j]);
            }
        }
	    return new Sudoku(cellArray);
    }
}