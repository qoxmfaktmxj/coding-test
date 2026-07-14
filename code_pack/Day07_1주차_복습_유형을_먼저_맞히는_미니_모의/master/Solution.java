import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public int solution(int[][] city) {
        int rows = city.length;
        int cols = city[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (city[r][c] == 1) {
                    dist[r][c] = 0;
                    queue.offer(new int[]{r, c});
                }
            }
        }
        if (queue.isEmpty()) {
            return -1;
        }

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            for (int d = 0; d < 4; d++) {
                int nr = current[0] + DR[d];
                int nc = current[1] + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (city[nr][nc] == -1 || dist[nr][nc] != -1) {
                    continue;
                }
                dist[nr][nc] = dist[current[0]][current[1]] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }

        int answer = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (city[r][c] == 0) {
                    if (dist[r][c] == -1) {
                        return -1;
                    }
                    answer = Math.max(answer, dist[r][c]);
                }
            }
        }
        return answer;
    }
}
