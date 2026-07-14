import java.util.*;

class Solution {
    private static final long INF = Long.MAX_VALUE / 4;

    public long solution(long[][] cost) {
        int pointCount = cost.length;
        int k = pointCount - 2;
        int end = pointCount - 1;

        if (k == 0) {
            return cost[0][end];
        }

        long[][] dp = new long[1 << k][k];
        for (long[] row : dp) {
            Arrays.fill(row, INF);
        }

        for (int i = 0; i < k; i++) {
            if (cost[0][i + 1] != -1) {
                dp[1 << i][i] = cost[0][i + 1];
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
                    long move = cost[current + 1][next + 1];
                    if (move == -1) {
                        continue;
                    }

                    int nextMask = mask | (1 << next);
                    dp[nextMask][next] = Math.min(
                            dp[nextMask][next],
                            dp[mask][current] + move
                    );
                }
            }
        }

        int fullMask = (1 << k) - 1;
        long answer = INF;
        for (int last = 0; last < k; last++) {
            if (cost[last + 1][end] == -1) {
                continue;
            }
            answer = Math.min(answer,
                    dp[fullMask][last] + cost[last + 1][end]);
        }
        return answer == INF ? -1L : answer;
    }
}
