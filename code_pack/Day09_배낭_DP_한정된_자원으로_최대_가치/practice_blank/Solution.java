class Solution {
    public int solution(int timeLimit, int[][] tests) {
        int[] dp = new int[timeLimit + 1];

        for (int[] test : tests) {
            int time = test[0];
            int score = test[1];
            for (int used = timeLimit; used >= time; used--) {
                // TODO: 핵심 로직을 복원하세요.
                        dp[used],
                        // TODO: 핵심 로직을 복원하세요.
                );
            }
        }
        // TODO: 핵심 로직을 복원하세요.
    }
}
