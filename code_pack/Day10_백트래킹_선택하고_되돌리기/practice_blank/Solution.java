class Solution {
    private int[] values;
    private int k;
    private int target;
    private long answer;

    public long solution(int[] values, int k, int target) {
        this.values = values;
        this.k = k;
        this.target = target;
        this.answer = Long.MAX_VALUE;
        choose(0, 0, 0L);
        return answer;
    }

    private void choose(int start, int count, long sum) {
        if (count == k) {
            answer = Math.min(answer, Math.abs(sum - target));
            return;
        }
        int need = k - count;
        if (values.length - start < need) {
            return;
        }

        for (int i = start; i < values.length; i++) {
            choose(i + 1, count + 1, sum + values[i]);
        }
    }
}
