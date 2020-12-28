import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class SudokuGenerator {

	private static final String FILENAME = ""; // e.g. "C:\\Users\\<user>\\Desktop\\sudokus_hard.txt";
	private static final String URL = "https://sugoku.herokuapp.com/board?difficulty=";
	private static final String DIFFICULTY = "hard";
			
	public static void main(String[] args) {
		LinkedList<int[][]> sudokuList = RetrieveSudokus(1000);
		Serializer.SudokuListToFile(sudokuList, FILENAME);
	}

	private static LinkedList<int[][]> RetrieveSudokus(int numberOfSudokus) {
		LinkedList<int[][]> sudokuList = new LinkedList<int[][]>();
		var client = HttpClient.newHttpClient();

		// create a request
		var request = HttpRequest.newBuilder(URI.create(URL + DIFFICULTY))
				.header("accept", "application/json").build();

		for (int i = 0; i < numberOfSudokus; i++) {
			System.out.println("Requesting sudoku " + (i+1) + "/" + numberOfSudokus);
			HttpResponse<String> response = null;
			try {
				response = client.send(request, BodyHandlers.ofString());
			} catch (Exception e) {
				System.out.println("Could not send request: " + e.getMessage());
				e.printStackTrace();
			}

			JSONObject jObject = null;
			try {
				jObject = new JSONObject(response.body());
			} catch (Exception e) {
				System.out.println("Error parsing JSON " + e.getMessage());
				e.printStackTrace();
			}
			Object jsonValue = getJsonValue(jObject, "board");
			int[][] sudoku = getSudokuArrayFromJSON(jsonValue.toString());
			sudokuList.add(sudoku);
		}
		return sudokuList;
	}

	private static int[][] getSudokuArrayFromJSON(String jsonString) {
		try {
			JSONArray array = new JSONArray(jsonString);
			int[][] intArray = new int[9][9];
			for (int i = 0; i < 9; i++) {
				JSONArray innerArray = array.getJSONArray(i);
				for (int j = 0; j < 9; j++) {
					intArray[i][j] = innerArray.getInt(j);
				}
			}
			return intArray;
		} catch (Exception e) {
			System.out.println("Could not get array from JSON" + e.getMessage());
			e.printStackTrace();			
			return null;
		}
	}

	private static Object getJsonValue(JSONObject jObj, String key) {
		try {
			return jObj.get(key);
		} catch (Exception e) {
			System.out.println("Could not get key " + key + "from JSONObject:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
