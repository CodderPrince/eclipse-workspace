package application;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TetrisGame extends Application {
  // Tetrominos in their initial state (spawn state)
  private static final char[][] TETROMINOS = {
    {
      ' ', ' ', ' ', ' ',
      'I', 'I', 'I', 'I',
      ' ', ' ', ' ', ' ',
      ' ', ' ', ' ', ' '
    },
    {
      'O', 'O',
      'O', 'O'
    },
    {
      'J', ' ', ' ',
      'J', 'J', 'J',
      ' ', ' ', ' '
    },
    {
      ' ', ' ', 'L',
      'L', 'L', 'L',
      ' ', ' ', ' '
    },
    {
      ' ', 'S', 'S',
      'S', 'S', ' ',
      ' ', ' ', ' '
    },
    {
      ' ', 'T', ' ',
      'T', 'T', 'T',
      ' ', ' ', ' '
    },
    {
      'Z', 'Z', ' ',
      ' ', 'Z', 'Z',
      ' ', ' ', ' '
    }
  };

  // ================================================

  /** Simple class to represent state for a key, e.g. pressed or released */
  static class KeyState {
    final boolean isPressed;
    final boolean isReleased;

    KeyState(boolean isPressed, boolean isReleased) {
      this.isPressed = isPressed;
      this.isReleased = isReleased;
    }
  }

  /**
   * Simple class to handle key input.
   * This can be invoked in a different thread so use concurrent hashmap to guarantee consistency.
   */
  static class KeyInputHandler implements EventHandler<KeyEvent> {
    private final ConcurrentHashMap<KeyCode, Boolean> keys;

    KeyInputHandler() {
      this.keys = new ConcurrentHashMap<KeyCode, Boolean>();
    }

    public void reset() {
      keys.clear();
    }

    public KeyState get(KeyCode code) {
      Boolean isPressed = keys.get(code);
      if (isPressed == null) return new KeyState(false, false);
      return new KeyState(isPressed, !isPressed);
    }

    @Override
    public void handle(KeyEvent event) {
      if ("KEY_RELEASED".equals(event.getEventType().toString())) {
        keys.put(event.getCode(), false);
      } else if ("KEY_PRESSED".equals(event.getEventType().toString())) {
        keys.put(event.getCode(), true);
      }
    }
  }

  // ================================================

  private static final int WIDTH = 12; // the board is 12 cells wide
  private static final int HEIGHT = 18; // ... and 18 cells high
  private static final int BLOCK_SIZE = 20; // block size to render on screen

  private Random rand;
  private AnimationTimer loop;
  private KeyInputHandler input;
  private char[][] board;

  @Override
  public void init() throws Exception {
    input = new KeyInputHandler();
    rand = new Random();
  }

  @Override
  public void start(Stage stage) {
    // Some basic setup for the game
    stage.setTitle("Tetris");
    Group root = new Group();
    Scene scene = new Scene(root);
    scene.setOnKeyPressed(input);
    scene.setOnKeyReleased(input);
    stage.setScene(scene);

    final Canvas canvas = new Canvas(2 * WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    // Game loop
    loop = new AnimationTimer() {
      long prevTime = 0; // used to track game time (tick)

      int tetromino, x, y, rotation;
      int nextTetromino, nextRotation;

      boolean isGameOver;
      int score = 0;
      int bestScore = 0;

      /** Restarts the game completely, resets the board and score */
      private void restartTheGame() {
        // Reset the board
        board = new char[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
          for (int j = 0; j < HEIGHT; j++) {
            board[i][j] = ' ';
          }
        };

        nextTetromino = rand.nextInt(TETROMINOS.length);
        nextRotation = rand.nextInt(4);

        selectNewPiece();

        isGameOver = false;
        bestScore = Math.max(score, bestScore);
        score = 0;
      }

      /** Select the next tetromino to show to the player */
      private void selectNewPiece() {
        tetromino = nextTetromino;
        x = WIDTH / 2 - tetrominoSize(tetromino) / 2;
        y = 0;
        rotation = nextRotation;
        nextTetromino = rand.nextInt(TETROMINOS.length);
        nextRotation = rand.nextInt(4);
      }

      // Initial start of the game
      {
        restartTheGame();
      }

      @Override
      public void handle(long elapsedTime) {
        // ============== TIMING ==============

        boolean tick = (elapsedTime - prevTime) / 1e6 >= 1000;
        if (tick) {
          prevTime = elapsedTime;
        }

        // ============== INPUT ==============

        // Process input keys, note that we only process one key per game loop iteration.
        // In the game, you usually can either rotate or move, you cannot do both at the same time.
        if (input.get(KeyCode.SPACE).isReleased) {
          // To make the game more interesting, we need to accommodate wall kicks:
          // https://tetris.wiki/Super_Rotation_System#Wall_Kicks
          int[][] wallKicks = tetrominoWallKicks(tetromino, rotation, rotation + 1);
          for (int[] pair : wallKicks) {
            if (canMove(tetromino, x + pair[0], y + pair[1], rotation + 1)) {
              x += pair[0];
              y += pair[1];
              rotation++;
              break;
            }
          }
        } else if (input.get(KeyCode.LEFT).isPressed && canMove(tetromino, x - 1, y, rotation)) {
          x--;
        } else if (input.get(KeyCode.RIGHT).isPressed && canMove(tetromino, x + 1, y, rotation)) {
          x++;
        } else if (input.get(KeyCode.DOWN).isPressed && canMove(tetromino, x, y + 1, rotation)) {
          y++;
        }
        // Special handling of Enter as restart key
        if (input.get(KeyCode.ENTER).isReleased && isGameOver) {
          restartTheGame();
        }

        input.reset();

        // ============== GAME LOGIC ==============

        // Once we have compute the coordinates, we need to do collision check
        // This will determine if we can move tetromino or not.

        // Check if the game is over, we cannot move any piece at all
        isGameOver = !canMove(tetromino, x, y, rotation);

        if (!isGameOver && tick) {
          // Check if we can move tetromino down, if not, freeze the piece and start a new one.
          if (!canMove(tetromino, x, y + 1, rotation)) {
            // Freeze the tetromino
            for (int i = 0; i < tetrominoSize(tetromino); i++) {
              for (int j = 0; j < tetrominoSize(tetromino); j++) {
                char value = TETROMINOS[tetromino][tetrominoIndex(tetromino, i, j, rotation)];
                if (value != ' ') {
                  board[x + i][y + j] = value;
                }
              }
            }

            // Once piece has been frozen, we need to compute the completed lines that the player
            // has scored. Note that we also need to remove those lines.

            int[] completedLines = new int[HEIGHT];
            int numCompleteLines = 0;
            for (int line = 0; line < HEIGHT; line++) {
              boolean isFull = true;
              for (int cell = 0; cell < WIDTH; cell++) {
                isFull = isFull && board[cell][line] != ' ';
                if (!isFull) break;
              }
              if (isFull) {
                completedLines[numCompleteLines++] = line;
              }
            }

            // Update score and remove complete lines
            if (numCompleteLines > 0) {
              score += 100 * (1 << (numCompleteLines - 1));

              for (int idx = 0; idx < numCompleteLines; idx++) {
                for (int i = 0; i < WIDTH; i++) {
                  for (int j = completedLines[idx] - 1; j >= 0; j--) {
                    board[i][j + 1] = board[i][j];
                  }
                }
              }
            }

            // Start over again
            selectNewPiece();
          } else {
            y++;
          }
        }

        // ============== RENDERING ==============

        // Render the board
        for (int i = 0; i < WIDTH; i++) {
          for (int j = 0; j < HEIGHT; j++) {
            setColorCell(gc, board[i][j]);
            gc.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
          }
        }

        // Render the active tetromino
        for (int i = 0; i < tetrominoSize(tetromino); i++) {
          for (int j = 0; j < tetrominoSize(tetromino); j++) {
            char value = TETROMINOS[tetromino][tetrominoIndex(tetromino, i, j, rotation)];
            if (value != ' ') {
              setColorCell(gc, value);
              gc.fillRect((x + i) * BLOCK_SIZE, (y + j) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
          }
        }

        // Clear the right panel
        gc.clearRect(WIDTH * BLOCK_SIZE, 0, WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);

        // Render the score
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(20));
        gc.fillText("Best: " + bestScore, (WIDTH + 1) * BLOCK_SIZE, BLOCK_SIZE);
        gc.fillText("Score: " + score, (WIDTH + 1) * BLOCK_SIZE, 2 * BLOCK_SIZE);

        // Render the next tetromino
        gc.fillText("Next:", (WIDTH + 1) * BLOCK_SIZE, 4 * BLOCK_SIZE);
        for (int i = 0; i < tetrominoSize(nextTetromino); i++) {
          for (int j = 0; j < tetrominoSize(nextTetromino); j++) {
            char value = TETROMINOS[nextTetromino][tetrominoIndex(nextTetromino, i, j, nextRotation)];
            if (value != ' ') {
              setColorCell(gc, value);
              gc.fillRect(
                (WIDTH + 1 + i) * BLOCK_SIZE,
                (5 + j) * BLOCK_SIZE,
                BLOCK_SIZE,
                BLOCK_SIZE
              );
            }
          }
        }

        // Render game over
        if (isGameOver) {
          gc.setFill(Color.RED);
          gc.setFont(Font.font(30));
          gc.fillText("GAME OVER", (WIDTH + 1) * BLOCK_SIZE, 12 * BLOCK_SIZE);
          gc.setFont(Font.font(20));
          gc.fillText("Press Enter to restart", (WIDTH + 1) * BLOCK_SIZE, 13 * BLOCK_SIZE);
        }
      }
    };

    loop.start();
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    System.out.println("Stop!");
    if (loop != null) {
      loop.stop();
      loop = null;
    }
  }

  /** Helper method to extract the size of each tetromino matrix since they are different */
  private static int tetrominoSize(int piece) {
    switch (piece) {
      case 0: return 4; // tetromino I
      case 1: return 2; // tetromino O
      default: return 3; // the rest
    }
  }

  /**
   * Helper method to normalise and return rotation as one of [0, 1, 2, 3].
   * 0 - base (spawn) state.
   * 1 - 0->R clockwise rotation.
   * 2 - 0->R->R clockwise rotation.
   * 3 - 0->R->R->R clockwise rotation.
   */
  private static int normRotation(int rotation) {
    return Math.abs(rotation % 4);
  }

  /**
   * This method returns the new coordinates taking into account rotation.
   * Since we represent tetromino data as a one-dimensional array, we convert (ip, jp) pair into
   * linear index.
   */
  private static int tetrominoIndex(int piece, int i, int j, int rotation) {
    int size = tetrominoSize(piece);
    int ip, jp;
    switch (normRotation(rotation)) {
      case 0:
        ip = i; jp = j;
        break;
      case 1:
        ip = j; jp = size - 1 - i;
        break;
      case 2:
        ip = size - 1 - i; jp = size - 1 - j;
        break;
      case 3:
        ip = size - 1 - j; jp = i;
        break;
      default:
        throw new AssertionError("Invalid rotation index");
    }
    return ip + size * jp; // we have tetrominos in column-major order
  }

  /**
   * Extracts wall kick data for tetromino.
   * All of our rotations are clockwise, so we do not bother with the rest of the data.
   */
  private static int[][] tetrominoWallKicks(int piece, int prevRotation, int newRotation) {
    prevRotation = normRotation(prevRotation);
    newRotation = normRotation(newRotation);

    switch (piece) {
      case 0:
        // Tetromino I has its own set of wall kicks since it has size 4
        if (prevRotation == 0 && newRotation == 1) {
          return new int[][] {{0, 0}, {-2, 0}, {+1, 0}, {-2, -1}, {+1, +2}}; // 0->R
        } else if (prevRotation == 1 && newRotation == 2) {
          return new int[][] {{0, 0}, {-1, 0}, {+2, 0}, {-1, +2}, {+2, -1}}; // R->2
        } else if (prevRotation == 2 && newRotation == 3) {
          return new int[][] {{0, 0}, {+2, 0}, {-1, 0}, {+2, +1}, {-1, -2}}; // 2->L
        } else if (prevRotation == 3 && newRotation == 0) {
          return new int[][] {{0, 0}, {+1, 0}, {-2, 0}, {+1, -2}, {-2, +1}}; // L->0
        }
        return new int[][] {{0, 0}};
      case 1:
        // Tetromino O does not have any wall kick data since it is just a square
        return new int[][] {{0, 0}};
      default:
        // The rest of the tetrominos have the same size 3
        if (prevRotation == 0 && newRotation == 1) {
          return new int[][] {{0, 0}, {-1, 0}, {-1, +1}, {0, -2}, {-1, -2}}; // 0->R
        } else if (prevRotation == 1 && newRotation == 2) {
          return new int[][] {{0, 0}, {+1, 0}, {+1, -1}, {0, +2}, {+1, +2}}; // R->2
        } else if (prevRotation == 2 && newRotation == 3) {
          return new int[][] {{0, 0}, {+1, 0}, {+1, +1}, {0, -2}, {+1, -2}}; // 2->L
        } else if (prevRotation == 3 && newRotation == 0) {
          // L->0	( 0, 0)	(-1, 0)	(-1,-1)	( 0,+2)	(-1,+2)
          return new int[][] {{0, 0}, {-1, 0}, {-1, -1}, {0, +2}, {-1, +2}}; // L->0
        }
        return new int[][] {{0, 0}};
    }
  }

  /**
   * Returns true if tetromino can be placed on the board starting at (x, y) coordinates and having
   * this rotation. False indicates that something blocks tetromino, so we cannot move it.
   */
  private boolean canMove(int tetromino, int x, int y, int rotation) {
    int size = tetrominoSize(tetromino);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        char value = TETROMINOS[tetromino][tetrominoIndex(tetromino, i, j, rotation)];
        int currX = x + i;
        int currY = y + j;

        // The move is valid if index is inside the board and the board at that index has an
        // empty cell, so we can place our tetromino there
        boolean isValid =
          value == ' ' /* value is empty, we don't care if it out of bounds */  ||
          currX >= 0 && currX < WIDTH && currY >= 0 && currY < HEIGHT && board[currX][currY] == ' ';

        if (!isValid) return false;
      }
    }

    return true;
  }

  /** Helper method to set the colour for the tetromino */
  private void setColorCell(GraphicsContext gc, char value) {
    switch (value) {
      case 'I': gc.setFill(Color.CYAN); break;
      case 'O': gc.setFill(Color.YELLOW); break;
      case 'J': gc.setFill(Color.BLUE); break;
      case 'L': gc.setFill(Color.ORANGE); break;
      case 'S': gc.setFill(Color.GREEN); break;
      case 'T': gc.setFill(Color.PURPLE); break;
      case 'Z': gc.setFill(Color.RED); break;
      default: gc.setFill(Color.GRAY); break;
    }
  }
}