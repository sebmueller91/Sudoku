import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Serializer {
	private static final String SUDOKU_DELIMITER = ";", ROW_DELIMITER = "-", NUMBER_DELIMITER = ",";
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
		String[] sudokus = fileContent.split(SUDOKU_DELIMITER);
		for (int s = 0; s < sudokus.length; s++) {
			int[][] sudoku = new int[9][9];
			String[] rows = sudokus[s].split(ROW_DELIMITER);
			for (int i = 0; i < rows.length; i++) {
				String[] entries = rows[i].split(NUMBER_DELIMITER);
				for (int j = 0; j < entries.length; j++) {					
					sudoku[i][j] = Integer.parseInt(entries[j]);
				}
			}
			sudokuList.add(sudoku);
		}
		return sudokuList;
	}

	private static String createDelimiter(String delimiter, int index, int maxIndex) {
		if (index + 1 < maxIndex) { // Only use delimiter if not last element
			return delimiter;
		}
		return "";
	}

	private static String readStringFromFile(String filename) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(filename));
		return new String(encoded);
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
