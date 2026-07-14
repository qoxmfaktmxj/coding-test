import java.util.*;

class Solution {
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    private String[] network;
    private boolean[][] visited;
    private int rows;
    private int cols;

    public int[] solution(String[] network) {
        this.network = network;
        rows = network.length;
        cols = network[0].length();
        visited = new boolean[rows][cols];

        List<Integer> sizes = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (network[r].charAt(c) == '1' && !visited[r][c]) {
                    sizes.add(dfs(r, c));
                }
            }
        }

        Collections.sort(sizes);
        int[] answer = new int[sizes.size()];
        for (int i = 0; i < sizes.size(); i++) {
            answer[i] = sizes.get(i);
        }
        return answer;
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
            if (network[nr].charAt(nc) != '1' || visited[nr][nc]) {
                continue;
            }
            size += dfs(nr, nc);
        }
        return size;
    }
}
