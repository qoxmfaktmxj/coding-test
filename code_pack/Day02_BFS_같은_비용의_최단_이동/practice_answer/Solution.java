import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public int solution(int[][] city, int[] start, int[] target) {
        int rows = city.length;
        int cols = city[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{start[0], start[1]});
        dist[start[0]][start[1]] = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            if (r == target[0] && c == target[1]) {
                return dist[r][c];
            }

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d];
                int nc = c + DC[d];

                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (city[nr][nc] == 1 || dist[nr][nc] != -1) {
                    continue;
                }

                dist[nr][nc] = dist[r][c] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }

        return -1;
    }
}
