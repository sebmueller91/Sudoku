package dgs_software.sudoku.model;

import org.junit.Test;
import java.util.Arrays;

import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

import static org.junit.Assert.assertEquals;


public class Sudoku_UnitTests {
	// region Define TestData
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
	// endregion Define TestData

	// region Tests
	// region getSolution()
	@Test
	public void getSolution_Solvable1() {
	    Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku1,false));
		boolean result = sudoku.getSolution();
		assertEquals(result, true);			
	}			   						
			   						
	@Test
	public void getSolution_Solvable2() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku7,false));
		boolean result = sudoku.getSolution();
		assertEquals(result, true);			
	}	
	
	@Test
	public void getSolution_Solvable3() {
        Sudoku sudoku =new Sudoku(Utils.intToCellArray(sudoku8,false));
        boolean result = sudoku.getSolution();
		assertEquals(result, true);			
	}			
	
	@Test
	public void getSolution_Solvable4() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku9,false));
        boolean result = sudoku.getSolution();
		assertEquals(result, true);			
	}		
	
	@Test
	public void getSolution_Solvable5() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku10,false));
        boolean result = sudoku.getSolution();
		assertEquals(result, true);			
	}		
	
	@Test
	public void getSolution_NonSolvable() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku11,false));
        boolean result = sudoku.getSolution();
        assertEquals(result, false);
	}		
	// endregion

	// region getPossibleNumbers()
	@Test
	public void getPossibleNumbersForCell_EmptySudoku() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku1,false));
		boolean[] result = sudoku.getPossibleNumbers(2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,true,true,true,true,true,true,true}), true);			
	}
	
	@Test
	public void getPossibleNumbersForCell_RegularTest() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku2,false));
		boolean[] result = sudoku.getPossibleNumbers(2,2);
		assertEquals(Arrays.equals(result, new boolean[] {true,true,false,false,true,true,false,true,true}), true);			
	}
	// endregion

	// region isValid()
	@Test
	public void SudokuContainsNoErrors_NoErrorEmpty() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku1,false));
		boolean result = sudoku.isValid();
		assertEquals(result, true);			
	}
	
	@Test
	public void SudokuContainsNoErrors_NoErrorFilled() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku2,false));
		boolean result = sudoku.isValid();
		assertEquals(result, true);			
	}
	
	@Test
	public void SudokuContainsNoErrors_RowError() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku3,false));
		boolean result = sudoku.isValid();
		assertEquals(result, false);			
	}
	
	@Test
	public void SudokuContainsNoErrors_ColumnError() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku4,false));
		boolean result = sudoku.isValid();
		assertEquals(result, false);			
	}
	
	@Test
	public void sudokuContainsNoErrors_BoxError() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku5,false));
		boolean result = sudoku.isValid();
		assertEquals(result, false);			
	}
	
	@Test
	public void sudokuContainsNoErrors_Solved() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku6,false));
		boolean result = sudoku.isValid();
		assertEquals(result, true);			
	}
	// endregion

	// region isCompletelyFilled()
	@Test
	public void sudokuIsCompletelyFilled_False_Empty() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku1,false));
		boolean result = sudoku.isCompletelyFilled();
		assertEquals(result, false);			
	}
	
	@Test
	public void sudokuIsCompletelyFilled_False_PatrtiallyFilled() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku5,false));
		boolean result = sudoku.isCompletelyFilled();
		assertEquals(result, false);			
	}
	
	@Test
	public void sudokuIsCompletelyFilled_False_Solved() {
        Sudoku sudoku = new Sudoku(Utils.intToCellArray(sudoku6,false));
		boolean result = sudoku.isCompletelyFilled();
		assertEquals(result, true);			
	}
	// endregion
	// endregion Tests
}