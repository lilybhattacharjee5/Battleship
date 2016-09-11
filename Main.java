import java.util.*;

public class Main {
	
	static String posGuessed;
	static int guessNumber = 8;
	static int dimValue = 5;
	static Player user;
	static String result;
	static BattleGame newGame;
	
	public static void main(String[] args) {
		newGame = new BattleGame();
		newGame.setXDim(dimValue);
		newGame.setYDim(dimValue);
		newGame.setNumberOfShips();
		newGame.createShips();
		user = new Player();
		for (int i = 0; i < guessNumber; i++) {
			hitResults();
		}
		newGame.endGame(false);
	}
	
	public static void hitResults() {
		posGuessed = user.guessPosition();
		result = newGame.checkGuess(posGuessed);
		if (result == "true") {
			System.out.println("You hit an enemy ship! (+" + newGame.hitPoints + " points)");
		} else if (result == "false") {
			System.out.println("Unfortunately, you missed and hit the ocean. (+0 points)");
		} else if (result == "blown") {
			System.out.println("You've destroyed an enemy ship! (+" + newGame.blownPoints + " points)");
		} else {
			newGame.endGame(true);
		}
		System.out.println("Current Score: " + newGame.getScore());
	}
	
}

class BattleGame {
	
	private static int xDim;
	private static int yDim;
	public static int blownPoints = 3;
	public static int hitPoints = 1;
	private static int maxShips = 3;
	private static int minShips = 1;
	private static String[] allOrientations = {"left","right","up","down"};
	private int numberOfShips;
	private ArrayList<String[]> shipPoints = new ArrayList<String[]>();
	private int hitShip = 0;
	private int score = 0;
	
	public void endGame(boolean gameStatus) {
		System.out.println("");
		if (gameStatus) {
			System.out.println("Congratulations! You won the game by destroying all enemy ships.");
		} else {
			System.out.println("Oh, no! By failing to destroy all of the enemy ships, you have failed in your mission and lost the war.");
			for (String[] pointsList : shipPoints) {
				for (String eachPoint : pointsList) {
					System.out.println(eachPoint);
				}
			}
		}
		System.out.println("\nFinal Score: " + getScore());
		System.exit(0);
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int scoreValue) {
		score = scoreValue;
	}
	
	public static int getXDim() {
		return xDim;
	}
	
	public void setXDim(int xDimValue) {
		xDim = xDimValue;
	}
	
	public static int getYDim() {
		return yDim;
	}
	
	public void setYDim(int yDimValue) {
		yDim = yDimValue;
	}
	
	public int getNumberOfShips() {
		return numberOfShips;
	}
	
	public void setNumberOfShips() {
		numberOfShips = (int) (Math.random() * maxShips) + minShips;
	}
	
	public void createShips() {
		
		for (int index = 0; index < numberOfShips; index++) {
			Battleship newBattleship = new Battleship();
			newBattleship.setXPos((int) (Math.random() * BattleGame.getXDim()));
			newBattleship.setYPos((int) (Math.random() * BattleGame.getYDim()));
			newBattleship.setOrientation(allOrientations[(int) (Math.random() * allOrientations.length)]);
			newBattleship.setPoints();
			shipPoints.add(newBattleship.getPoints().split(" "));
		}
	}
	
	public String checkGuess(String posGuessed) {
		int indexCounter;
		for (String[] pointsArray : shipPoints) {
			indexCounter = 0;
			for (String eachPoint : pointsArray) {
				if ((posGuessed.equals(eachPoint))) {
					pointsArray[indexCounter] = null;
					hitShip += 1;
					if (hitShip == pointsArray.length) {
						hitShip = 0;
						shipPoints.remove(pointsArray);
						setScore(score + blownPoints);
						if (shipPoints.size() != 0) {
							return "blown";
						} else {
							return "gameOver";
						}
					} else {
						setScore(score + hitPoints);
						return "true";
					}
				}
				indexCounter += 1;
			}
		}
		return "false";
	}
}

class Battleship {
	
	private static int maxLength = 3;
	private static int minLength = 1;
	private int xPos;
	private int yPos;
	private String orientation;
	private int length;
	private ArrayList<int[]> allPoints = new ArrayList<int[]>();
	
	public int getXPos() {
		return xPos;
	}
	
	public void setXPos(int xPosValue) {
		xPos = xPosValue;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setYPos(int yPosValue) {
		yPos = yPosValue;
	}
	
	public String getOrientation() {
		return orientation;
	}
	
	public void setOrientation(String orientationValue) {
		orientation = orientationValue;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int lengthValue) {
		length = lengthValue;
	}
	
	public String getPoints() {
		String allPointsString = "";
		if (allPoints.size() > 0) {
			for (int[] point : allPoints) {
				allPointsString += ("(" + point[0] + "," + point[1] + ")" + " ");
			}
		}
		return allPointsString;
	}
	
	public void setPoints() {
		int[] initPoint = new int[2];
		int newX;
		int newY;
		setLength((int) (Math.random() * maxLength + minLength));
		checkLength();
		if (checkLength()) {
			initPoint[0] = xPos;
			initPoint[1] = yPos;
			allPoints.add(initPoint);
			for (int unit = 1; unit < length; unit++) {
				if (orientation == "left") {
					newX = xPos - unit;
					newY = yPos;
				} else if (orientation == "right") {
					newX = xPos + unit;
					newY = yPos;
				} else if (orientation == "down") {
					newX = xPos;
					newY = yPos - unit;
				} else {
					newX = xPos;
					newY = yPos + unit;
				}
				int[] otherPoint = {newX,newY};
				allPoints.add(otherPoint);
				otherPoint = null;
			}
		} else {
			setPoints();
		}
	}
	
	public boolean checkLength() {
		int boardXDim = BattleGame.getXDim();
		int boardYDim = BattleGame.getYDim();
		int x = getXPos();
		int y = getYPos();
		int length = getLength();
		String orient = getOrientation();
		if (((x + length > boardXDim) && (orient == "right")) ||
			((x - length < 0) && (orient == "left")) ||
			((y - length < 0) && (orient == "down")) ||
			((y + length > boardYDim) && (orient == "up"))
			) {
			setXPos((int) (Math.random() * boardXDim));
			setYPos((int) (Math.random() * boardYDim));
			allPoints.clear();
			return false;
		} else {
			return true;
		}
	}
}

class Player {
	
	public static String guessPosition() {
		Scanner guess = new Scanner(System.in);
		System.out.println("\nGuess an enemy ship position [(x,y) form]: ");
		String guessPos = guess.next();
		return guessPos;
	}
