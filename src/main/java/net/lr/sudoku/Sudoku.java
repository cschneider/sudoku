package net.lr.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Sudoku {
	char[][] board;

	public static void main(String[] args) throws IOException {
		Sudoku sudoku = new Sudoku(read(new File("242.txt")));
		if (!sudoku.iterateTrySolve()) {
			sudoku.tryMulti();
		}
	}

	public Sudoku(char[][] board2) {
		this.board = new char[9][9];
		for (int c = 0; c < 9; c++) {
			this.board[c] = new char[9];
			for (int c2 = 0; c2 < 9; c2++) {
				this.board[c][c2] = board2[c][c2];
			}
		}
	}

	static char[][] read(File file) throws IOException {
		char[][] board = new char[9][9];
		try ( //
				FileReader reader = new FileReader(file); //
				BufferedReader breader = new BufferedReader(reader) //
		) {
			for (int c = 0; c < 9; c++) {
				board[c] = new char[9];
				String line = breader.readLine();
				for (int c2 = 0; c2 < 9; c2++) {
					board[c][c2] = line.charAt(c2);
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return board;
	}

	private boolean iterateTrySolve() {
		print();
		while (trySolve()) {
			//print();
		}
		
		if (isSolved()) {
			System.out.println("SOLVED !!!!!");
			print();
			return true;
		} else {
			return tryMulti();
		}
	}
	
	private boolean tryMulti() {
		for (int yc = 0; yc < 9; yc++) {
			for (int xc = 0; xc < 9; xc++) {
				if (board[yc][xc] == ' ') {
					Set<Character> possibleValues = getPossibleValues(yc, xc);
					if (possibleValues.size() == 2) {
						for (Character posValue : possibleValues) {
							System.out.println("Trying " +  xc + ":" + yc + " = " + posValue);
							try {
								board[yc][xc] = posValue;
								Sudoku sudoku = new Sudoku(board);
								if (sudoku.iterateTrySolve()) {
									return true;
								}
							} catch (RuntimeException e) {
								System.out.println("This did not work " +  xc + ":" + yc + " = " + posValue);
								board[yc][xc] = ' ';
								print();
							}
							board[yc][xc] = ' ';
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isSolved() {
		for (int yc = 0; yc < 9; yc++) {
			for (int xc = 0; xc < 9; xc++) {
				if (board[yc][xc] == ' ') {
					return false;
				}
			}
		}
		return true;
	}

	private boolean trySolve() {
		boolean oneSolved = false;
		for (int yc = 0; yc < 9; yc++) {
			for (int xc = 0; xc < 9; xc++) {
				if (board[yc][xc] == ' ') {
					Set<Character> possibleValues = getPossibleValues(yc, xc);
					if (possibleValues.size() == 1) {
						oneSolved = true;
						char solvedValue = possibleValues.iterator().next();
						board[yc][xc] = solvedValue;
						//System.out.println("Solved " +  xc + ":" + yc + " = " + solvedValue);
						//print();
					}
					if (possibleValues.size() == 0) {
						throw new RuntimeException("Unsolvable at " +  xc + ":" + yc);
					}
				}
			}
		}
		return oneSolved;
	}

	private Set<Character> getPossibleValues(int y, int x) {
		Set<Character> possibleValuesy = getPossibleValuesY(x);
		Set<Character> possibleValuesx = getPossibleValuesX(y);
		Set<Character> possibleValuesq = getPossibleValuesQ(y, x);
		possibleValuesq.retainAll(possibleValuesx);
		possibleValuesq.retainAll(possibleValuesy);
		return possibleValuesq;
	}

	private Set<Character> getPossibleValuesQ(int y, int x) {
		Set<Character> possibleValuesq = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;
		for (int yc = starty; yc < starty +3; yc++) {
			for (int xc = startx; xc < startx +3 ; xc++) {
				possibleValuesq.remove(board[yc][xc]);
			}
		}
		return possibleValuesq;
	}

	private Set<Character> getPossibleValuesX(int y) {
		Set<Character> possibleValuesx = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
		for (int xc = 0; xc<9; xc ++) {
			possibleValuesx.remove(board[y][xc]);
		}
		return possibleValuesx;
	}

	private Set<Character> getPossibleValuesY(int x) {
		Set<Character> possibleValuesy = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
		for (int yc = 0; yc<9; yc ++) {
			possibleValuesy.remove(board[yc][x]);
		}
		return possibleValuesy;
	}

	private void print() {
		for (int c = 0; c < 9; c++) {
			if (c % 3 == 0) {
				printSoldiLine();
			}
			printBoardLine(c);
			//printEmptyLine();
		}
		printSoldiLine();

	}

	private void printSoldiLine() {
		System.out.println("-------------------");
	}

	private void printBoardLine(int y) {
		for (int c2 = 0; c2 < 9; c2++) {
			System.out.print((c2 % 3 == 0 ? "|" : " ") + board[y][c2]);
		}
		System.out.print('|');
		System.out.println();
	}
}
