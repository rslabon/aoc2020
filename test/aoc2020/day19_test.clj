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
      (char? rule-body) (if (= rule-body (get text text-idx)) 1 0)
      (number? rule-body) (count-matched text text-idx rules-def rule-body)
      :else (let [[sub-rule-1 sub-rule-2] rule-body
                                      match-sub-rule-1 (map-indexed (fn [idx item] (count-matched text (+ idx text-idx) rules-def item)) sub-rule-1)
                                      match-sub-rule-2 (map-indexed (fn [idx item] (count-matched text (+ idx text-idx) rules-def item)) sub-rule-2)
                                      _ (print match-sub-rule-1 match-sub-rule-2)
                                      ]
                                  (max (reduce + match-sub-rule-1) (reduce + match-sub-rule-2))
                                  ))))

(defn valid?
  [text text-idx rules-def rule-idx]
  (= (count text) (count-matched text text-idx rules-def rule-idx))
  )

(deftest valid?-test
  (testing "valid?"
    ;(is (= true (valid? "a" 0 {0 \a} 0)))
    ;(is (= false (valid? "b" 0 {0 \a} 0)))
    ;(is (= true (valid? "ab" 0 {0 [[1 2] []], 1 \a, 2 \b} 0)))
    ;(is (= false (valid? "cc" 0 {0 [[1 2] []], 1 \a, 2 \b} 0)))
    ;(is (= false (valid? "aa" 0 {0 [[1 2] []], 1 \a, 2 \b} 0)))
    ;(is (= true (valid? "ab" 0 {0 [[] [1 2]], 1 \a, 2 \b} 0)))
    ;(is (= false (valid? "c" 0 {0 [[1 2] []], 1 \a, 2 \b, 3 \c} 0)))
    ;(is (= false (valid? "ab" 0 {0 [[1] []], 1 \a, 2 \b} 0)))

    ;(is (= true (valid? "abc" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [] ], 3 \b, 4 \c} 0)))
    ;(is (= true (valid? "acb" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [4 3]], 3 \b, 4 \c} 0)))
    ;(is (= false (valid? "cba" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [4 3]], 3 \b, 4 \c} 0)))
    ;(is (= false (valid? "cba" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [4 3]], 3 \b, 4 \c} 0)))
    ;(is (= true (valid? "aa" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [5]], 3 \b, 4 \c, 5 [[1] []]} 0)))
    ;(is (= false (valid? "ac" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [5]], 3 \b, 4 \c, 5 [[1] []]} 0)))
    ;(is (= true (valid? "abc" 0 {0 [[1 2] []], 1 \a, 2 [[3 4] [5]], 3 \b, 4 \c, 5 [[1] []]} 0)))


    ;(is (= true (valid? "ababbb" 0 rules-def 0)))
    (is (= 1 (count-matched "ababbb" 0 rules-def 0)))
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

