import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public int solution(String[] garage) {
        int rows = garage.length;
        int cols = garage[0].length();
        int[][] moduleId = new int[rows][cols];
        for (int[] row : moduleId) {
            Arrays.fill(row, -1);
        }

        int startR = -1;
        int startC = -1;
        int moduleCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char cell = garage[r].charAt(c);
                if (cell == 'S') {
                    startR = r;
                    startC = c;
                // TODO: 핵심 로직을 복원하세요.
                    moduleId[r][c] = moduleCount++;
                }
            }
        }

        int fullMask = (1 << moduleCount) - 1;
        boolean[][][] visited =
                new boolean[rows][cols][1 << moduleCount];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        // TODO: 핵심 로직을 복원하세요.
        visited[startR][startC][0] = true;

        while (!queue.isEmpty()) {
            // TODO: 핵심 로직을 복원하세요.
            int r = current[0];
            int c = current[1];
            int mask = current[2];
            int distance = current[3];

            if (garage[r].charAt(c) == 'E' && mask == fullMask) {
                // TODO: 핵심 로직을 복원하세요.
            }

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d];
                int nc = c + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (garage[nr].charAt(nc) == '#') {
                    continue;
                }

                int nextMask = mask;
                if (moduleId[nr][nc] != -1) {
                    nextMask |= 1 << moduleId[nr][nc];
                }
                if (visited[nr][nc][nextMask]) {
                    continue;
                }
                // TODO: 핵심 로직을 복원하세요.
                queue.offer(new int[]{nr, nc, nextMask, distance + 1});
            }
        }
        return -1;
    }
}
