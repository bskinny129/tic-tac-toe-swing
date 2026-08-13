# Tic Tac Toe (Java Swing)

A playable Tic Tac Toe game written in Java with a Swing GUI. You play X, the
computer plays O — and the computer is smart enough to take a win when it sees
one and block you when you're about to win.

**No installing anything.** This runs entirely in your web browser using GitHub
Codespaces.

---

## How to run it

1. Make sure you're signed in to GitHub (a free account is fine).
2. On this page, click the green **Code** button → **Codespaces** tab →
   **Create codespace on main**.
3. Wait a minute or two while it builds. You'll end up looking at VS Code in
   your browser.
4. In the terminal at the bottom, type:

   ```bash
   ./run.sh
   ```

5. A notification will pop up about port **6080** — click **Open in Browser**.
   (If you miss it, go to the **PORTS** tab at the bottom and click the globe
   icon next to port 6080.)
6. A Linux desktop opens in a new tab, and the Tic Tac Toe window is right
   there. If it asks for a password, it's `vscode`.
7. Click a square to make your move!

To stop the game, close the window or press `Ctrl+C` in the terminal.

---

## The files

| File | What's in it |
|------|--------------|
| `TicTacToeFinished.java` | The main class. Sets up the window, handles your mouse clicks, decides the computer's move, and checks for a winner. |
| `GameBoard.java` | A `JPanel` that does all the drawing — the grid lines, the red X's, the blue O's, and the status label. |
| `run.sh` | Compiles both files and launches the game. |
| `.devcontainer.json` | Tells Codespaces to install Java and a desktop so the window can appear. You shouldn't need to touch this. |

---

## Make it your own

Here are some ideas, roughly easiest to hardest:

**Change how it looks**
- Change the X and O colors in `GameBoard.java` (look for `Color.RED` and
  `Color.BLUE`).
- Make the board bigger or smaller: change `pixels_per_side` in
  `TicTacToeFinished.java`.
- Draw thicker lines by casting to `Graphics2D` and calling
  `g2.setStroke(new BasicStroke(5))`.

**Change how it plays**
- Add a **Reset** button so you can play again without restarting.
- Keep **score** across games and show it in the label.
- Let the computer go first sometimes.
- Make an **easy mode** where the computer always moves randomly (hint: the
  logic is in `determineMove`).

**Bigger challenges**
- Change `squares_per_side` to 4 and make it 4-in-a-row. You'll need to rewrite
  `doesPlayerWin` — right now it checks the 3x3 lines one by one.
- Add a two-player mode so a friend can play as O.
- Make the winning three squares highlight in a different color.

---

## How the code works (the short version)

The board is stored as a 3x3 grid of numbers in `boardPieces`:
`0` = empty, `1` = X, `2` = O.

When you click, `mouseClicked` divides your click position by the size of a
square to figure out which square you hit, puts a `1` there, and then asks the
computer to move.

The computer's logic in `determineMove` is three simple rules, in order:

1. Can I win right now? Then do that.
2. Can X win next turn? Then block that square.
3. Otherwise, pick a random open square.

Everything you see on screen is drawn in `paintComponent` in `GameBoard.java`.
Calling `repaint()` is what asks Java to redraw the board after a move.
