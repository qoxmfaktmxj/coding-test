class Solution {
    public int solution(int timeLimit, int[][] tests) {
        int[] dp = new int[timeLimit + 1];

        for (int[] test : tests) {
            int time = test[0];
            int score = test[1];
            for (int used = timeLimit; used >= time; used--) {
                dp[used] = Math.max(
                        dp[used],
                        dp[used - time] + score
                );
            }
        }
        return dp[timeLimit];
    }
}
