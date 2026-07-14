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
            // TODO: 핵심 로직을 복원하세요.
        }
    }

    public long solution(int[][] board, int[] start,
                         int[] end, int[][] stops) {
        int k = stops.length;
        int[][] points = new int[k + 2][2];
        points[0] = start.clone();
        for (int i = 0; i < k; i++) {
            points[i + 1] = stops[i].clone();
        }
        points[k + 1] = end.clone();

        long[][] pairCost = new long[k + 2][k + 2];
        for (int i = 0; i < k + 2; i++) {
            long[][] dist = dijkstra(board, points[i]);
            for (int j = 0; j < k + 2; j++) {
                // TODO: 핵심 로직을 복원하세요.
            }
        }

        if (k == 0) {
            long move = pairCost[0][1];
            // TODO: 핵심 로직을 복원하세요.
        }

        long[][] dp = new long[1 << k][k];
        for (long[] row : dp) {
            Arrays.fill(row, INF);
        }

        for (int i = 0; i < k; i++) {
            if (pairCost[0][i + 1] != INF) {
                // TODO: 핵심 로직을 복원하세요.
            }
        }

        for (int mask = 1; mask < (1 << k); mask++) {
            for (int current = 0; current < k; current++) {
                if (dp[mask][current] == INF) {
                    continue;
                }
                for (int next = 0; next < k; next++) {
                    if ((mask & (1 << next)) != 0) {
                        continue;
                    }
                    long move = pairCost[current + 1][next + 1];
                    if (move == INF) {
                        continue;
                    }
                    int nextMask = mask | (1 << next);
                    // TODO: 핵심 로직을 복원하세요.
                            dp[nextMask][next],
                            // TODO: 핵심 로직을 복원하세요.
                    );
                }
            }
        }

        int fullMask = (1 << k) - 1;
        long answer = INF;
        for (int last = 0; last < k; last++) {
            long moveToEnd = pairCost[last + 1][k + 1];
            if (moveToEnd == INF) {
                continue;
            }
            answer = Math.min(answer,
                    // TODO: 핵심 로직을 복원하세요.
        }

        if (answer == INF) {
            return -1L;
        }
        // TODO: 핵심 로직을 복원하세요.
    }

    private long[][] dijkstra(int[][] board, int[] source) {
        int rows = board.length;
        int cols = board[0].length;
        long[][] dist = new long[rows][cols];
        for (long[] row : dist) {
            Arrays.fill(row, INF);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[source[0]][source[1]] = 0L;
        pq.offer(new Node(source[0], source[1], 0L));

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
                if (board[nr][nc] == -1) {
                    continue;
                }

                long nextCost = current.cost + board[nr][nc];
                if (nextCost < dist[nr][nc]) {
                    dist[nr][nc] = nextCost;
                    pq.offer(new Node(nr, nc, nextCost));
                }
            }
        }
        return dist;
    }
}
