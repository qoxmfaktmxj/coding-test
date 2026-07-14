import java.util.*;

class Solution {
    public int solution(int[][] reservations, int cooldown) {
        Arrays.sort(reservations,
                Comparator.comparingInt(a -> a[0]));
        PriorityQueue<Long> releaseTimes = new PriorityQueue<>();
        int answer = 0;

        for (int[] reservation : reservations) {
            while (!releaseTimes.isEmpty()
                    && releaseTimes.peek() <= reservation[0]) {
                releaseTimes.poll();
            }
            releaseTimes.offer((long) reservation[1] + cooldown);
            answer = Math.max(answer, releaseTimes.size());
        }
        return answer;
    }
}
