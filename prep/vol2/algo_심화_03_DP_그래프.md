# 2편 · 알고리즘 심화 (3) DP 핵심 4종

> DP는 "상태 정의 → 점화식"이 전부. 대표 4문제로 감을 잡으세요. 전부 컴파일·실행 검증됨.

---

## 문제 8. 편집 거리 (Levenshtein)

> 문자열 `a`를 `b`로 바꾸는 **최소 편집 횟수**(삽입/삭제/교체 각 1회)를 반환하세요.
> 예: `"sunday" → "saturday"` = 3

**정답 (검증됨)**
```java
class Solution {
    public int solution(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;   // b가 빈 문자열 → i번 삭제
        for (int j = 0; j <= m; j++) dp[0][j] = j;   // a가 빈 문자열 → j번 삽입
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];                       // 같으면 그대로
                else
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],          // 교체
                                   Math.min(dp[i - 1][j],              // 삭제
                                            dp[i][j - 1]));            // 삽입
            }
        return dp[n][m];
    }
}
```
**해설**
- `dp[i][j]` = a의 앞 i글자를 b의 앞 j글자로 바꾸는 최소 비용. **문장으로 상태를 정의할 수 있어야 함**.
- 마지막 글자가 같으면 대각선 그대로, 다르면 세 방향(교체/삭제/삽입) 최소 +1.
- 검증: sunday→saturday=3, horse→ros=3, intention→execution=5.

**⚠️ 함정**: 초기값(빈 문자열 대비 i, j)을 빠뜨리면 전부 틀림. 인덱스 `i-1/j-1`(문자열은 0-index, dp는 1-index).

---

## 문제 9. 최장 증가 부분 수열 (LIS)

> 배열에서 **엄격히 증가하는 가장 긴 부분 수열의 길이**를 반환하세요.
> 예: `[10,9,2,5,3,7,101,18]` → 4 (`2,3,7,18` 또는 `2,3,7,101`)

**정답 (검증됨, O(n log n))**
```java
import java.util.*;
class Solution {
    public int solution(int[] a) {
        List<Integer> tail = new ArrayList<>();       // tail[k]=길이 k+1 수열의 최소 끝값
        for (int x : a) {
            int lo = 0, hi = tail.size();
            while (lo < hi) {                          // x가 들어갈 lower_bound 찾기
                int mid = (lo + hi) / 2;
                if (tail.get(mid) < x) lo = mid + 1; else hi = mid;
            }
            if (lo == tail.size()) tail.add(x);        // 최댓값 갱신 → 길이 +1
            else tail.set(lo, x);                      // 아니면 그 자리 교체(더 작은 끝값)
        }
        return tail.size();
    }
}
```
**해설**
- `tail` 리스트에 "길이별 최소 끝값"을 유지하며 이분탐색으로 위치 결정 → O(n log n).
- 길이만 필요할 때의 정석. (O(n²) DP `dp[i]=max(dp[j])+1`도 있지만 느림)
- 검증: [10,9,2,5,3,7,101,18]=4, [0,1,0,3,2,3]=4, [7,7,7]=1(엄격 증가라 1).

**⚠️ 함정**: `tail`은 실제 LIS가 아니라 **길이 계산용**(값 자체는 정답 수열이 아님). "이상"이면 `<=`, "초과(엄격)"면 `<`로 이분 조건 조정.

---

## 문제 10. 2×N 타일링

> `2 × n` 바닥을 `1×2`, `2×1` 타일로 채우는 **경우의 수**를 반환하세요.
> 예: n=4 → 5

**정답 (검증됨)**
```java
class Solution {
    public long solution(int n) {
        if (n <= 2) return n;            // f(1)=1, f(2)=2
        long a = 1, b = 2;               // f(1), f(2)
        for (int i = 3; i <= n; i++) {
            long c = a + b;              // f(i)=f(i-1)+f(i-2)
            a = b; b = c;
        }
        return b;
    }
}
```
**해설**
- 마지막 칸을 세로 타일 1개(→ f(n-1)) 또는 가로 타일 2개(→ f(n-2))로 채움 → **피보나치 점화식**.
- 검증: n=1→1, n=4→5, n=5→8.
- 실제 시험은 보통 `% 1_000_000_007` 요구 → 매 스텝 모듈러.

**⚠️ 함정**: 초기값 `f(1)=1, f(2)=2`. 값이 커지므로 `long` + (요구 시) 모듈러 연산. 배열 없이 변수 2개로 O(1) 공간.

---

## 문제 11. 동전 교환 최소 개수

> 동전 종류 `coins`로 금액 `amount`를 만드는 **최소 동전 개수**(불가능하면 −1)를 반환하세요.
> 예: `[1,5,12]`, 15 → 3 (5+5+5)

**정답 (검증됨)**
```java
import java.util.*;
class Solution {
    public int solution(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);          // 불가능 표식(충분히 큰 값)
        dp[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins)
                if (c <= a) dp[a] = Math.min(dp[a], dp[a - c] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```
**해설**
- `dp[a]` = 금액 a를 만드는 최소 동전 수. 각 동전 c에 대해 `dp[a-c]+1`로 갱신 → **무한 개수 배낭류 DP**.
- 검증: [1,5,12] 15→3, [1,2,5] 11→3, [2] 3→−1.
- 그리디(큰 동전부터)는 [1,5,12]·15에서 12+1+1+1=4로 **틀림** → 반드시 DP.

**⚠️ 함정**: "최소 개수"는 그리디가 일반적으로 실패(위 반례). 불가능 판정을 위한 초기 큰 값 + 마지막 체크. dp 크기 `amount+1`.

---

### ✅ 이 장 핵심
- 편집거리: `dp[i][j]` 앞 i·j글자, 같으면 대각선 / 다르면 3방향 +1
- LIS: 길이별 최소 끝값 + 이분탐색 O(n log n)
- 타일링: 피보나치, `long`(+모듈러)
- 동전 최소: 무한 배낭 DP (그리디 금지)
