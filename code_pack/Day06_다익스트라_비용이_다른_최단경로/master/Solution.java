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
            return Long.compare(this.cost, other.cost);
        }
    }

    public long solution(int[][] cost) {
        int rows = cost.length;
        int cols = cost[0].length;
        long[][] dist = new long[rows][cols];
        for (long[] row : dist) {
            Arrays.fill(row, INF);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[0][0] = cost[0][0];
        pq.offer(new Node(0, 0, dist[0][0]));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.cost != dist[current.r][current.c]) {
                continue;
            }

            for (int d = 0; d < 4; d++) {
                int nr = current.r + DR[d];
                int nc = current.c + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }

                long nextCost = current.cost + cost[nr][nc];
                if (nextCost < dist[nr][nc]) {
                    dist[nr][nc] = nextCost;
                    pq.offer(new Node(nr, nc, nextCost));
                }
            }
        }

        return dist[rows - 1][cols - 1];
    }
}
