class Solution {
    public long[] solution(int[] numbers, int[][] queries) {
        long[] prefix = new long[numbers.length + 1];
        for (int i = 0; i < numbers.length; i++) {
            prefix[i + 1] = prefix[i] + numbers[i];
        }

        long[] answer = new long[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int left = queries[i][0];
            int right = queries[i][1];
            answer[i] = prefix[right + 1] - prefix[left];
        }
        return answer;
    }
}
