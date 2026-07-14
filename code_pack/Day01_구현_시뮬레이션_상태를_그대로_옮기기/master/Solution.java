import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public int[] solution(int[][] board, int[] start, String commands) {
        int rows = board.length;
        int cols = board[0].length;
        int r = start[0];
        int c = start[1];
        int direction = start[2];

        boolean[][] visited = new boolean[rows][cols];
        visited[r][c] = true;
        int visitedCount = 1;

        for (char command : commands.toCharArray()) {
            if (command == 'L') {
                direction = (direction + 3) % 4;
            } else if (command == 'R') {
                direction = (direction + 1) % 4;
            } else if (command == 'F') {
                int nr = r + DR[direction];
                int nc = c + DC[direction];

                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (board[nr][nc] == 1) {
                    continue;
                }

                r = nr;
                c = nc;
                if (!visited[r][c]) {
                    visited[r][c] = true;
                    visitedCount++;
                }
            }
        }

        return new int[]{r, c, direction, visitedCount};
    }
}
