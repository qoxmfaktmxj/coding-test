import java.util.*;

class Solution {
    public long solution(int[] enemies) {
        long total = 0L;
        List<Integer> positives = new ArrayList<>();

        for (int enemy : enemies) {
            total += enemy;
            if (enemy > 0) {
                positives.add(enemy);
            }
        }
        Collections.sort(positives);

        long answer = Math.max(1L, 1L - total);
        long positivePrefix = 0L;

        for (int enemy : positives) {
            answer = Math.max(
                    answer,
                    enemy + 1L - positivePrefix
            );
            positivePrefix += enemy;
        }
        return answer;
    }
}
