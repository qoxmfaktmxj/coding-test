import java.util.*;

class Solution {
    private static final long INF = Long.MAX_VALUE / 4;
    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    private static class Node implements Comparable<Node> {
        int r;
        int c;
        long cost;

        Node(int r, int c, long cost) {
            this.r = r;
            this.c = c;
            this.cost = cost;
        }

        public int compareTo(Node other) {
            return Long.compare(cost, other.cost);
        }
    }

    public long solution(int[][] city, int[] start, int[] target) {
        int rows = city.length;
        int cols = city[0].length;
        long[][] dist = new long[rows][cols];
        for (long[] row : dist) {
            Arrays.fill(row, INF);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[start[0]][start[1]] = city[start[0]][start[1]];
        pq.offer(new Node(start[0], start[1],
                dist[start[0]][start[1]]));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.cost != dist[current.r][current.c]) {
                continue;
            }
            if (current.r == target[0] && current.c == target[1]) {
                return current.cost;
            }

            for (int d = 0; d < 4; d++) {
                int nr = current.r + DR[d];
                int nc = current.c + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (city[nr][nc] == -1) {
                    continue;
                }

                long nextCost = current.cost + city[nr][nc];
                if (nextCost < dist[nr][nc]) {
                    dist[nr][nc] = nextCost;
                    pq.offer(new Node(nr, nc, nextCost));
                }
            }
        }
        return -1L;
    }
}
