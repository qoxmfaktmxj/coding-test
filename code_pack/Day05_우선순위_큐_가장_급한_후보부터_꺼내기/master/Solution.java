import java.util.*;

class Solution {
    public int solution(int[][] sessions) {
        Arrays.sort(sessions, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();
        int answer = 0;

        for (int[] session : sessions) {
            while (!endTimes.isEmpty() && endTimes.peek() <= session[0]) {
                endTimes.poll();
            }
            endTimes.offer(session[1]);
            answer = Math.max(answer, endTimes.size());
        }
        return answer;
    }
}
