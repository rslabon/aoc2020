(ns aoc2020.day19-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day19 :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def puzzle-input (slurp (io/resource "day19.txt")))

;0: 4 1 5
;1: 2 3 | 3 2
;2: 4 4 | 5 5
;3: 4 5 | 5 4
;4: "a"
;5: "b"

;ababbb
;bababa
;abbbab
;aaabbb
;aaaabbb

(def rules-def {0 [[4 1 5] []], 1 [[2 3] [3 2]], 2 [[4 4] [5 5]], 3 [[4 5] [5 4]], 4 \a, 5 \b})

(defn to-regex
  ([rules-def] (str "^" (to-regex rules-def [0]) "$"))
  ([rules-def rules]
   (if (empty? rules)
     ""
     (let [rule (first rules)
           rule-body (get rules-def rule)
           rule-body (if (= rule 8) [[42]] rule-body)
           rule-body (if (= rule 11) [[]] rule-body)
           rule-regex (if (char? rule-body) (str rule-body)
                                            (if (coll? (first rule-body))
                                              (let [sub-rules (first rule-body)
                                                    sub-rules2 (second rule-body)
                                                    r1 (to-regex rules-def sub-rules)
                                                    r2 (to-regex rules-def sub-rules2)
                                                    r (if (empty? r2) r1 (str "(" r1 "|" r2 ")"))]
                                                r)
                                              (reduce (fn [acc other-rule] (str acc (to-regex rules-def [other-rule]))) "" rule-body)
                                              )
                                            )
           rule-regex (cond
                        (= rule 8) (str "(" rule-regex ")+")
                        (= rule 11) (let [rule42 (to-regex rules-def [42])
                                          rule31 (to-regex rules-def [31])]
                                      (str "(" (reduce (fn [acc val] (str acc "|" "((" rule42 "){" val "}" "(" rule31 "){" val "})")) (str "((" rule42 "){1}" "(" rule31 "){1})") (range 2 10)) ")")
                                      )
                        :else rule-regex
                        )
           result (str rule-regex (to-regex rules-def (rest rules)))]
       result
       ))))

(deftest to-regex-test
  (testing "to-regex"
    (is (= "^a$" (to-regex {0 \a})))
    (is (= "^aaa$" (to-regex {0 [1 1 1], 1 \a})))
    (is (= "^(a|b)$" (to-regex {0 [[1] [2]], 1 \a, 2 \b})))
    (is (= "^a$" (to-regex {0 [[1] []], 1 \a, 2 \b})))
    (is (= "^a$" (to-regex {0 [[1]], 1 \a, 2 \b})))
    (is (= "^aa$" (to-regex {0 [[1 2]], 1 [[2]], 2 \a})))
    (is (= "^aa$" (to-regex {0 [[1 2]], 1 [[2]], 2 \a})))
    (is (= "^a((aa|bb)(ab|ba)|(ab|ba)(aa|bb))b$" (to-regex rules-def)))
    ))

(deftest gen-test
  (testing "gen"
    (is (= ["aaa"] (gen {1 \a} [1 1 1])))
    (is (= ["aaa" "bbb"] (gen {1 \a, 2 \b} [[1 1 1] [2 2 2]])))
    (is (= ["aabb"] (gen {1 \a, 2 \b, 3 [[1 2]]} [[1 3 2]])))
    (is (= (set ["aaaabb" "aaabab" "abbabb" "abbbab" "aabaab" "aabbbb" "abaaab" "ababbb"]) (set (gen rules-def [0]))))
    ))

(defn valid?
  [text rules-def]
  (let [possible-matches (set (gen rules-def [0]))
        _ (println possible-matches)]
    (contains? possible-matches text)
    ))

(deftest valid?-test
  (testing "valid?"
    (is (= true (valid? "ababbb" rules-def)))
    ;(is (= true (valid? "abbbab" rules-def)))
    ;(is (= false (valid? "bababa" rules-def)))
    ;(is (= false (valid? "aaabbb" rules-def)))
    ;(is (= false (valid? "aaaabbb" rules-def)))
    ))

(def input "2: 4 4 | 5 5\n4: \"a\"\n0: 4 1 5\n1: 2 3 | 3 2\n3: 4 5 | 5 4\n5: \"b\"\n0: 4 1 5")

(deftest parse-rule-def-test
  (testing "parse-rule-def"
    (is (= {0 \a} (parse-rule-def "0: \"a\"")))
    (is (= {1 [[2 3] []]} (parse-rule-def "1: 2 3")))
    (is (= {1 [[2 3] [4 5]]} (parse-rule-def "1: 2 3 | 4 5")))
    (is (= rules-def (parse-rule-def input)))
    ))

(defn solve-1
  [input]
  (let [[rules-def-input messages] (str/split input #"\n\n")
        rules-def (parse-rule-def rules-def-input)
        messages (str/split-lines messages)
        possible-matches (set (gen rules-def [0]))
        matched (map #(contains? possible-matches %) messages)
        ]
    (count (filter true? matched))
    ))

(defn solve-2
  [input]
  (let [[rules-def-input messages] (str/split input #"\n\n")
        rules-def (parse-rule-def rules-def-input)
        messages (str/split-lines messages)
        regex (re-pattern (to-regex rules-def))
        matched (map #(not= nil (re-matches regex %)) messages)
        ]
    (count (remove false? matched))
    ))

(deftest solve-1-test
  (testing "solve-1"
    (is (= 113 (solve-1 puzzle-input)))
    ))

(deftest solve-2-test
  (testing "solve-2"
    (is (= 253 (solve-2 puzzle-input)))
    ))