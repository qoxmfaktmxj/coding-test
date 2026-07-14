# 2편 · 알고리즘 심화 (2) 투포인터·DFS·그리디·파라메트릭

> "이걸 알면 유형 즉답" 4대장. 전부 컴파일·실행 검증됨.

---

## 문제 4. 합이 target인 연속 부분수열 개수 (슬라이딩 윈도우)

> 양의 정수 배열 `a`에서 **연속한 원소들의 합이 `target`**인 구간의 개수를 반환하세요.
> 예: `[1,2,3,2,5]`, target=5 → **3** (`[2,3]`, `[3,2]`, `[5]`)

**정답 (검증됨)**
```java
class Solution {
    public int solution(int[] a, int target) {
        int cnt = 0, sum = 0, left = 0;
        for (int right = 0; right < a.length; right++) {
            sum += a[right];                              // 오른쪽 확장
            while (sum > target && left <= right) sum -= a[left++];  // 초과하면 왼쪽 축소
            if (sum == target) cnt++;
        }
        return cnt;
    }
}
```
**해설**
- **양수 배열**이면 창을 넓히면 합↑, 좁히면 합↓ → 투포인터(슬라이딩 윈도우) O(n).
- 오른쪽을 밀며 더하고, 초과하면 왼쪽을 당겨 줄이고, 딱 맞으면 카운트.

**⚠️ 함정**: **음수가 섞이면 단조성이 깨져** 슬라이딩 윈도우 불가 → 누적합 + 해시(prefix sum) 필요. "양의 정수" 조건 확인.

**🔗 프로그래머스**: "연속된 부분 수열의 합" 계열.

---

## 문제 5. 타겟 넘버 (DFS 완전탐색)

> 각 수 앞에 `+` 또는 `−`를 붙여 더한 값이 `target`이 되는 **경우의 수**를 반환하세요.
> 예: `[1,1,1,1,1]`, target=3 → **5**

**정답 (검증됨)**
```java
class Solution {
    public int solution(int[] numbers, int target) {
        return dfs(numbers, 0, 0, target);
    }
    private int dfs(int[] n, int idx, int sum, int target) {
        if (idx == n.length) return sum == target ? 1 : 0;   // 다 썼을 때 판정
        return dfs(n, idx + 1, sum + n[idx], target)          // +
             + dfs(n, idx + 1, sum - n[idx], target);         // -
    }
}
```
**해설**
- 각 원소에서 +/− 두 갈래로 분기 → 이진 트리 완전탐색. 잎(idx==끝)에서 합이 target이면 1.
- 원소 수 N이면 2^N 경우. **N ≤ 20 정도**면 완전탐색 OK(그 이상은 DP/메모이제이션).

**⚠️ 함정**: 종료 조건은 "인덱스 끝"에서만 판정(중간에 sum==target이라고 세면 안 됨). BFS로도 가능하나 DFS가 간결.

**🔗 프로그래머스**: Lv.2 "타겟 넘버"(DFS/BFS 대표문제).

---

## 문제 6. 구명보트 (그리디 + 투포인터)

> 각 사람 무게 `people`, 보트 한도 `limit`. 보트당 **최대 2명 & 합이 limit 이하**.
> 모두 옮기는 **최소 보트 수**를 반환하세요. 예: `[70,50,80,50]`, limit=100 → **3**

**정답 (검증됨)**
```java
import java.util.*;
class Solution {
    public int solution(int[] people, int limit) {
        Arrays.sort(people);
        int i = 0, j = people.length - 1, cnt = 0;
        while (i <= j) {
            if (people[i] + people[j] <= limit) i++;   // 제일 가벼운 사람도 같이 태움
            j--;                                        // 제일 무거운 사람은 무조건 탑승
            cnt++;
        }
        return cnt;
    }
}
```
**해설**
- 정렬 후 **가장 무거운 사람(j)** 기준으로, 가장 가벼운 사람(i)과 같이 탈 수 있으면 둘, 아니면 혼자.
- 무거운 쪽부터 처리하는 게 그리디의 핵심(무거운 사람은 어차피 배를 차지하므로).

**⚠️ 함정**: 3명 이상 못 태움(한도만 맞으면 되는 게 아니라 인원 제한 2). 정렬 필수. `i <= j` 경계(가운데 1명 남는 경우).

**🔗 프로그래머스**: Lv.2 "구명보트".

---

## 문제 7. 랜선 자르기 (파라메트릭 서치)

> 길이 배열 `lines`를 잘라 **길이가 같은 랜선을 최소 `need`개** 만들 때,
> 만들 수 있는 **최대 랜선 길이**를 반환하세요. 예: `[802,743,457,539]`, need=11 → **200**

**정답 (검증됨)**
```java
class Solution {
    public long solution(int[] lines, int need) {
        long lo = 1, hi = 0, ans = 0;
        for (int x : lines) hi = Math.max(hi, x);       // 답의 상한 = 최장 랜선
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;              // 길이를 이분
            long cnt = 0;
            for (int x : lines) cnt += x / mid;         // mid로 자르면 몇 개?
            if (cnt >= need) { ans = mid; lo = mid + 1; }   // 충분 → 더 길게 시도
            else hi = mid - 1;                              // 부족 → 짧게
        }
        return ans;
    }
}
```
**해설**
- "**~을 최대로 하는 값**"인데 `길이↑ → 개수↓`가 단조 → **답(길이)을 이분탐색**(파라메트릭 서치).
- `mid`로 잘라 총 개수가 need 이상이면 더 길게, 부족하면 짧게.
- 개수 합은 크므로 `long`. 검증: 200(합 4+3+2+2=11), 201이면 10개라 부족.

**⚠️ 함정**: 합/길이 오버플로 → `long`. `mid=(lo+hi)/2` 대신 `lo+(hi-lo)/2`. lo/hi 갱신 방향과 "만족 시 답 저장" 패턴.

**🔗 프로그래머스/백준**: "입국심사", 백준 1654 "랜선 자르기", 2805 "나무 자르기".

---

### ✅ 이 장 핵심
- 슬라이딩 윈도우 = **양수 + 연속 구간** (음수면 prefix+해시)
- DFS 완전탐색 = 분기 재귀, 잎에서 판정, N≤20
- 그리디 구명보트 = 정렬 후 **무거운 사람 기준 투포인터**
- 파라메트릭 = "최대/최소를 만드는 값"을 이분, `long`·경계 주의
