(ns aoc2020.day19-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day19 :refer :all]))


(def rules-def {0 [[4 1 5] []], 1 [[2 3] [3 2]], 2 [[4 4] [5 5]], 3 [[4 5] [5 4]], 4 \a, 5 \b})

(defn count-matched
  [text text-idx rules-def rule-idx]
  (let [rule-body (get rules-def rule-idx)
        _ (println text-idx)
        ]
    (cond
      (char? rule-body) (= rule-body (get text text-idx))
      (number? rule-body) (count-matched text text-idx rules-def rule-body)
      ;(empty? rule-body) 0
      (coll? (first rule-body)) (let [[sub-rule-1 sub-rule-2] rule-body
                                      ;_ (println sub-rule-1 sub-rule-2)
                                      match-sub-rule-1 (map-indexed (fn [idx item] (count-matched text (+ idx text-idx) rules-def item)) sub-rule-1)
                                      match-sub-rule-2  (map-indexed (fn [idx item] (count-matched text (+ idx text-idx) rules-def item)) sub-rule-2)
                                      _ (print match-sub-rule-1 match-sub-rule-2)
                                      ]
                                  (or (= #{true} (set match-sub-rule-1)) (= #{true} (set match-sub-rule-2)))
                                  ))))


(deftest count-matched-test
  (testing "count-matched"
    ;(is (= true (count-matched "a" 0 {0 \a} 0)))
    ;(is (= false (count-matched "b" 0 {0 \a} 0)))
    ;(is (= true (count-matched "ab" 0 {0 [[1 2] []], 1 \a, 2 \b} 0)))
    ;(is (= false (count-matched "cc" 0 {0 [[1 2] []], 1 \a, 2 \b} 0)))
    ;(is (= false (count-matched "aa" 0 {0 [[1 2] []], 1 \a, 2 \b} 0)))
    ;(is (= true (count-matched "ab" 0 {0 [[] [1 2]], 1 \a, 2 \b} 0)))
    ;(is (= false (count-matched "c" 0 {0 [[1 2] []], 1 \a, 2 \b, 3 \c} 0)))
    (is (= false (count-matched "ab" 0 {0 [[1] []], 1 \a, 2 \b} 0)))

    ;(is (= true (count-matched "abc" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [] ], 3 \b, 4 \c} 0)))
    ;(is (= true (count-matched "acb" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [4 3]], 3 \b, 4 \c} 0)))
    ;(is (= false (count-matched "cba" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [4 3]], 3 \b, 4 \c} 0)))
    ;(is (= false (count-matched "cba" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [4 3]], 3 \b, 4 \c} 0)))
    ;(is (= true (count-matched "aa" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [5]], 3 \b, 4 \c, 5 [[1] []]} 0)))
    ;(is (= false (count-matched "ac" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [5]], 3 \b, 4 \c, 5 [[1] []]} 0)))


    ;(is (= true (count-matched "ababbb" 0 rules-def 0)))
    ))

;0: 4 1 5
;1: 2 3 | 3 2
;2: 4 4 | 5 5
;3: 4 5 | 5 4
;4: "a"
;5: "b"

;a aa ab b

;ababbb
;bababa
;abbbab
;aaabbb
;aaaabbb

