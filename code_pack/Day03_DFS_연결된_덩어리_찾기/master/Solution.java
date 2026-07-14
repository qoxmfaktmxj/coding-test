class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    private int rows;
    private int cols;
    private int[][] grid;
    private boolean[][] visited;

    public int[] solution(int[][] grid) {
        this.grid = grid;
        rows = grid.length;
        cols = grid[0].length;
        visited = new boolean[rows][cols];

        int componentCount = 0;
        int maxSize = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1 && !visited[r][c]) {
                    componentCount++;
                    maxSize = Math.max(maxSize, dfs(r, c));
                }
            }
        }

        return new int[]{componentCount, maxSize};
    }

    private int dfs(int r, int c) {
        visited[r][c] = true;
        int size = 1;

        for (int d = 0; d < 4; d++) {
            int nr = r + DR[d];
            int nc = c + DC[d];

            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                continue;
            }
            if (grid[nr][nc] == 0 || visited[nr][nc]) {
                continue;
            }

            size += dfs(nr, nc);
        }
        return size;
    }
}
