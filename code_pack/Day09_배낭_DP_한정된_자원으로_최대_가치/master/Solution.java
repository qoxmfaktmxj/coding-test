class Solution {
    public int solution(int capacity, int[][] items) {
        int[] dp = new int[capacity + 1];

        for (int[] item : items) {
            int weight = item[0];
            int value = item[1];

            for (int c = capacity; c >= weight; c--) {
                dp[c] = Math.max(dp[c], dp[c - weight] + value);
            }
        }
        return dp[capacity];
    }
}
