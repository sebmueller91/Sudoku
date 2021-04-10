import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Convert {

	private static final String SUDOKU_DELIMITER = ";", ROW_DELIMITER = "-", NUMBER_DELIMITER = ",";
	private static final String NEWLINE = "\n", COMMA = ",", DOT = ".";
	
	private static String inputFile = "C:\\repos\\GDS_Software\\src\\java\\SudokuGenerator\\raw_files\\intermediate_unprocessed.csv"; // e.g. "C:\repos\GDS_Software\src\java\SudokuGenerator\raw_files\simple_unprocessed.csv"
	private static String outputFile = "C:\\Users\\sebmu\\Desktop\\sudokus_hard.txt"; // e.g. "C:\Users\sebmu\Dekstop\easy.txt"
	
	public static void main(String[] args) {
		if (args.length == 2) {
			inputFile = args[0];
			outputFile = args[1];
		}
		
		LinkedList<int[][]> sudokuList = FileToSudokuList(inputFile);
		System.out.println(sudokuList.size());
		SudokuListToFile(sudokuList, outputFile);
		
	}
	
	public static boolean SudokuListToFile(LinkedList<int[][]> sudokuList, String filename) {
		try {
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			StringBuilder stringBuilder = new StringBuilder();
			for (int s = 0; s < sudokuList.size(); s++) {
				int[][] sudoku = sudokuList.get(s);
				for (int i = 0; i < sudoku.length; i++) {
					for (int j = 0; j < sudoku[i].length; j++) {
						stringBuilder.append(sudoku[i][j]);
						stringBuilder.append(createDelimiter(NUMBER_DELIMITER, j, sudoku[i].length));
					}
					stringBuilder.append(createDelimiter(ROW_DELIMITER, i, sudoku.length));

				}
				stringBuilder.append(createDelimiter(SUDOKU_DELIMITER, s, sudokuList.size()));
			}

			WriteStringToFile(file, stringBuilder.toString());

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static LinkedList<int[][]> FileToSudokuList(String filename) {
		LinkedList<int[][]> sudokuList = new LinkedList<int[][]>();
		String content = null;
		try {
			content = readStringFromFile(filename);
		} catch(IOException e) {
			System.err.println("IOException when reading file " + filename + " : " + e.getMessage());
			e.printStackTrace();
			return null;
		}		

		return FileContentToSudokuList(content);
	}
	
	public static LinkedList<int[][]> FileContentToSudokuList(String fileContent) {
		LinkedList<int[][]> sudokuList = new LinkedList<int[][]>();
		String[] lines = fileContent.split(NEWLINE);		
		for (int l = 0; l < lines.length; l++) {
			String sudokuString = lines[l].split(COMMA)[0];	
			int[][] sudoku = new int[9][9];
			for (int i = 0; i < 9; i++) {				
				for (int j = 0; j < 9; j++) {	
					int pos = i*9+j;
					char curChar = sudokuString.charAt(pos);
					if (curChar == '.') {
						sudoku[i][j] = 0;
					} else {	
						sudoku[i][j] = Integer.parseInt(String.valueOf(curChar));
					}
				}
			}			
			sudokuList.add(sudoku);
		}
		
		return sudokuList;
	}


	private static String readStringFromFile(String filename) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(filename));
		return new String(encoded);
	}
	
	private static String createDelimiter(String delimiter, int index, int maxIndex) {
		if (index + 1 < maxIndex) { // Only use delimiter if not last element
			return delimiter;
		}
		return "";
	}
	
	private static boolean WriteStringToFile(File file, String string) {
		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(string);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return true;
	}

}
