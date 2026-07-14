class Solution {
    public long solution(int[] energy) {
        if (energy.length == 1) {
            return energy[0];
        }

        long[] dp = new long[energy.length];
        dp[0] = energy[0];
        dp[1] = (long) energy[0] + energy[1];

        for (int i = 2; i < energy.length; i++) {
            dp[i] = energy[i] + Math.min(dp[i - 1], dp[i - 2]);
        }
        return dp[energy.length - 1];
    }
}
