import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public int solution(String[] map) {
        int rows = map.length;
        int cols = map[0].length();
        int[][] itemId = new int[rows][cols];
        for (int[] row : itemId) {
            Arrays.fill(row, -1);
        }

        int startR = 0;
        int startC = 0;
        int itemCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char cell = map[r].charAt(c);
                if (cell == 'S') {
                    startR = r;
                    startC = c;
                } else if (cell == 'I') {
                    itemId[r][c] = itemCount++;
                }
            }
        }

        int fullMask = (1 << itemCount) - 1;
        boolean[][][] visited =
                new boolean[rows][cols][1 << itemCount];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{startR, startC, 0, 0});
        visited[startR][startC][0] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            int mask = current[2];
            int distance = current[3];

            if (map[r].charAt(c) == 'E' && mask == fullMask) {
                return distance;
            }

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d];
                int nc = c + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (map[nr].charAt(nc) == '#') {
                    continue;
                }

                int nextMask = mask;
                if (itemId[nr][nc] != -1) {
                    nextMask |= 1 << itemId[nr][nc];
                }
                if (visited[nr][nc][nextMask]) {
                    continue;
                }

                visited[nr][nc][nextMask] = true;
                queue.offer(new int[]{nr, nc, nextMask, distance + 1});
            }
        }
        return -1;
    }
}
