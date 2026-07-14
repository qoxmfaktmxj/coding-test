import java.util.*;

class Solution {
    public int solution(int[][] inspections, int cleanupTime) {
        Arrays.sort(inspections, (a, b) -> {
            if (a[1] != b[1]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[0], b[0]);
        });

        int selected = 0;
        long availableAt = Long.MIN_VALUE;

        for (int[] inspection : inspections) {
            if (inspection[0] >= availableAt) {
                selected++;
                availableAt = (long) inspection[1] + cleanupTime;
            }
        }
        return selected;
    }
}
