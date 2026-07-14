class Solution {
    public long solution(int[] reward) {
        if (reward.length == 0) {
            return 0L;
        }
        if (reward.length == 1) {
            return reward[0];
        }

        long[] dp = new long[reward.length];
        dp[0] = reward[0];
        dp[1] = Math.max(reward[0], reward[1]);

        for (int i = 2; i < reward.length; i++) {
            long skip = dp[i - 1];
            long take = dp[i - 2] + reward[i];
            dp[i] = Math.max(skip, take);
        }
        return dp[reward.length - 1];
    }
}
