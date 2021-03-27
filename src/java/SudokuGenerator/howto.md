# Purpose
How to generate new Sudokus and convert them to the appropriate data format for the Sudoku App.
# How to
- Visit https://qqwing.com/generate.html
- Choose number of sudokus, format=csv, difficulty and symmetry=none, click generate
- Wait until finished
- Copy text into text file and remove first line (the one with the headers)
- Execute /src/java/SudokuGenerator/src <input_file> <output_file>
- Copy the output file into the raw ressource folder of the app
