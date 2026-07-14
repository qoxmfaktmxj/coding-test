import java.util.*;

class Solution {
    private int[][] cost;
    private boolean[] visited;
    private int answer;

    public int solution(int[][] cost) {
        this.cost = cost;
        this.visited = new boolean[cost.length];
        this.answer = Integer.MAX_VALUE;
        visited[0] = true;
        dfs(0, 1, 0);
        return answer;
    }

    private void dfs(int current, int visitedCount, int totalCost) {
        if (totalCost >= answer) {
            return;
        }
        if (visitedCount == cost.length) {
            answer = Math.min(answer, totalCost);
            return;
        }

        for (int next = 1; next < cost.length; next++) {
            if (visited[next]) {
                continue;
            }
            visited[next] = true;
            dfs(next, visitedCount + 1,
                    totalCost + cost[current][next]);
            visited[next] = false;
        }
    }
}
