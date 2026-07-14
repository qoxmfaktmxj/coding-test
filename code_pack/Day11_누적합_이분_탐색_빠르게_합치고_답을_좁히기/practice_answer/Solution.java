class Solution {
    public long solution(int[] lessons, int m) {
        long left = 0L;
        long right = 0L;
        for (int lesson : lessons) {
            left = Math.max(left, lesson);
            right += lesson;
        }

        long answer = right;
        while (left <= right) {
            long mid = (left + right) >>> 1;
            if (canSplit(lessons, m, mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }

    private boolean canSplit(int[] lessons, int m, long limit) {
        int groups = 1;
        long sum = 0L;

        for (int lesson : lessons) {
            if (sum + lesson > limit) {
                groups++;
                sum = lesson;
                if (groups > m) {
                    return false;
                }
            } else {
                sum += lesson;
            }
        }
        return true;
    }
}
