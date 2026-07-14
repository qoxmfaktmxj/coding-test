import java.util.*;

class Solution {
    private static final long INF = Long.MAX_VALUE / 4;

    private static class Edge {
        int to;
        int cost;

        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    private static class Node implements Comparable<Node> {
        int vertex;
        long cost;

        Node(int vertex, long cost) {
            this.vertex = vertex;
            this.cost = cost;
        }

        public int compareTo(Node other) {
            // TODO: 핵심 로직을 복원하세요.
        }
    }

    public long solution(int n, int[][] roads, int start,
                         int end, int[] checkpoints) {
        List<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] road : roads) {
            graph[road[0]].add(new Edge(road[1], road[2]));
            graph[road[1]].add(new Edge(road[0], road[2]));
        }

        int k = checkpoints.length;
        int[] points = new int[k + 2];
        points[0] = start;
        for (int i = 0; i < k; i++) {
            points[i + 1] = checkpoints[i];
        }
        points[k + 1] = end;

        long[][] pairCost = new long[k + 2][k + 2];
        for (int i = 0; i < k + 2; i++) {
            long[] dist = dijkstra(graph, points[i]);
            for (int j = 0; j < k + 2; j++) {
                // TODO: 핵심 로직을 복원하세요.
            }
        }

        if (k == 0) {
            // TODO: 핵심 로직을 복원하세요.
        }

        long[][] dp = new long[1 << k][k];
        for (long[] row : dp) {
            Arrays.fill(row, INF);
        }
        for (int i = 0; i < k; i++) {
            // TODO: 핵심 로직을 복원하세요.
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

        long answer = INF;
        int fullMask = (1 << k) - 1;
        for (int last = 0; last < k; last++) {
            long moveToEnd = pairCost[last + 1][k + 1];
            if (moveToEnd == INF) {
                continue;
            }
            answer = Math.min(answer,
                    // TODO: 핵심 로직을 복원하세요.
        }
        // TODO: 핵심 로직을 복원하세요.
    }

    private long[] dijkstra(List<Edge>[] graph, int source) {
        long[] dist = new long[graph.length];
        Arrays.fill(dist, INF);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[source] = 0L;
        pq.offer(new Node(source, 0L));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.cost != dist[current.vertex]) {
                continue;
            }
            for (Edge edge : graph[current.vertex]) {
                long nextCost = current.cost + edge.cost;
                if (nextCost < dist[edge.to]) {
                    dist[edge.to] = nextCost;
                    pq.offer(new Node(edge.to, nextCost));
                }
            }
        }
        return dist;
    }
}
