import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public int[] solution(int[][] warehouse, int[] start, String[] commands) {
        int rows = warehouse.length;
        int cols = warehouse[0].length;
        int r = start[0];
        int c = start[1];
        int direction = start[2];
        int movedCount = 0;

        for (String command : commands) {
            if (command.equals("LEFT")) {
                direction = (direction + 3) % 4;
                continue;
            }
            if (command.equals("RIGHT")) {
                direction = (direction + 1) % 4;
                continue;
            }

            int steps = Integer.parseInt(command.substring(3));
            for (int step = 0; step < steps; step++) {
                int nr = r + DR[direction];
                int nc = c + DC[direction];

                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    break;
                }
                if (warehouse[nr][nc] == 1) {
                    break;
                }

                r = nr;
                c = nc;
                movedCount++;
            }
        }

        return new int[]{r, c, direction, movedCount};
    }
}
