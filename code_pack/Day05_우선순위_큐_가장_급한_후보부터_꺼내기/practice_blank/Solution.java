import java.util.*;

class Solution {
    public int solution(int[][] chargingSessions, int turnaround) {
        Arrays.sort(chargingSessions,
                Comparator.comparingInt(a -> a[0]));

        PriorityQueue<Long> releaseTimes = new PriorityQueue<>();
        int answer = 0;

        for (int[] session : chargingSessions) {
            // TODO: 핵심 로직을 복원하세요.
                    && releaseTimes.peek() <= session[0]) {
                releaseTimes.poll();
            }

            releaseTimes.offer((long) session[1] + turnaround);
            answer = Math.max(answer, releaseTimes.size());
        }
        return answer;
    }
}
