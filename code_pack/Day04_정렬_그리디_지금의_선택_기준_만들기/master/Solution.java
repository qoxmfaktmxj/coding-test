import java.util.*;

class Solution {
    public int solution(int[][] meetings) {
        Arrays.sort(meetings, (a, b) -> {
            if (a[1] != b[1]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[0], b[0]);
        });

        int selected = 0;
        int lastEnd = Integer.MIN_VALUE;

        for (int[] meeting : meetings) {
            if (meeting[0] >= lastEnd) {
                selected++;
                lastEnd = meeting[1];
            }
        }
        return selected;
    }
}
