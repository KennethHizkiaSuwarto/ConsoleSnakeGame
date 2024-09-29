import java.util.Random;
import java.util.Scanner;

public class Main {
	private static final int WIDTH = 20;
	private static final int HEIGHT = 10;
	private static final int EMPTY = 0;
	private static final int SNAKE = 1;
	private static final int FRUIT = 2;

	private int[][] grid;
	private int[] snakeX, snakeY; // arrays to store snake body positions
	private int snakeLength, headX, headY, fruitX, fruitY, score;
	private boolean gameOver, hasEaten;
	private Random random;
	private Thread gameThread;
	private Scanner scan = new Scanner(System.in);


	public Main() {
		grid = new int[HEIGHT][WIDTH];
		random = new Random();
		gameThread = new Thread(this::runGame);

	}

	public void startGame() {
		snakeX = new int[WIDTH * HEIGHT];
		snakeY = new int[WIDTH * HEIGHT];
		snakeLength = 1;
		headX = WIDTH / 2;
		headY = HEIGHT / 2;
		snakeX[0] = headX;
		snakeY[0] = headY;
		grid[headY][headX] = SNAKE;
		placeFruit();
		gameOver = false;
		hasEaten = false;

		gameThread.start();
	}

	private void runGame() {
		
		while (!gameOver) {
			drawGrid();
			moveSnake();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			System.out.println("GameOver");
	}

	private void drawGrid() {
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				char cell = ' ';
				if (grid[i][j] == SNAKE) {
					cell = 'O';
				} else if (grid[i][j] == FRUIT) {
					cell = 'F';
				}
				System.out.print(cell + " ");
			}
			System.out.println();
		}
		System.out.println("Score: " + score);
	}

	private void placeFruit() {
		do {
			fruitX = random.nextInt(WIDTH);
			fruitY = random.nextInt(HEIGHT);

		} while (grid[fruitY][fruitX] != EMPTY);
		grid[fruitY][fruitX] = FRUIT;
	}

	private void moveSnake() {
		char direction = scan.next().charAt(0);

		int newHeadX = headX;
		int newHeadY = headY;

		switch (direction) {
		case 'w':
			newHeadY--;
			break;
		case 's':
			newHeadY++;
			break;
		case 'a':
			newHeadX--;
			break;
		case 'd':
			newHeadX++;
			break;
		}
		
		if (newHeadX < 0 || newHeadX >= WIDTH || newHeadY < 0 || newHeadY  >= HEIGHT) {
			gameOver = true; //hit the wall
		}
		
		if (grid[newHeadY][newHeadX] == SNAKE) {
			gameOver = true; // collided with itself
		}
		
		//move snake's head
		snakeX[snakeLength] = newHeadX;
		snakeY[snakeLength] = newHeadY;
		headX = newHeadX;
		headY = newHeadY;
		grid[headY][headX] = SNAKE;
		
		if (headX == fruitX && headY == fruitY) {
			score++;
			hasEaten = true;
			placeFruit();
		}//check if snake has eaten the fruit
		
		if (!hasEaten) {
			int tailX = snakeX[0];
			int tailY = snakeY[0];
			grid[tailY][tailX] = EMPTY;
			for (int i = 0; i < snakeLength; i++) {
				snakeX[i] = snakeX[i + 1];
				snakeY[i] = snakeY[i + 1];
			}
		} else {
			snakeLength++;
			hasEaten = false;
		}
	}
	

	public static void main(String[] args) {
		Main game = new Main();
		game.startGame();
	}

}