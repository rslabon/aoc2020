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

;(defn check
;  ([rules-def rules chars]
;   (let [_ (println rules chars)]
;     (cond
;       (and (empty? rules) (empty? chars)) true
;       (empty? rules) true
;       (empty? chars) false
;       :else (let [rule (first rules)
;                   char (first chars)
;                   rule-body (get rules-def rule)
;                   _ (println "rule=" rule "___" rule-body "___" (rest rules) chars)
;                   ]
;               (and
;                 (cond (char? rule-body) (= rule-body char)
;                       (coll? rule-body) (if (coll? (first rule-body))
;                                           (or (check rules-def (first rule-body) chars) (check rules-def (second rule-body) chars))
;                                           (check rules-def rule-body chars)))
;                 (check rules-def (rest rules) (rest chars)))))))
;  ([rules-def text]
;   (check rules-def [0] (vec (.toCharArray text)))
;   ))

(defn check
  ([rules-def rules chars]
   (cond
     (and (empty? rules) (empty? chars)) [true []]
     (empty? rules) [true chars]
     (empty? chars) [false []]
     :else (let [rule (first rules)
                 char (first chars)
                 rule-body (get rules-def rule)
                 [valid chars-to-match]
                 (cond (char? rule-body) (if (= rule-body char) [true (rest chars)] [false chars])
                       (coll? rule-body) (if (coll? (first rule-body))
                                           (let [[sub-valid sub-chars-to-match] (check rules-def (first rule-body) chars)]
                                             (if (true? sub-valid)
                                               [sub-valid sub-chars-to-match]
                                               (check rules-def (second rule-body) chars))
                                             )
                                           (check rules-def rule-body chars)))
                 _ (println "xxx_" valid rules rule rule-body chars)
                 ]
             (if (true? valid)
               (check rules-def (rest rules) chars-to-match)
               [valid chars]
               ))))
  ([rules-def text]
   (let [[_ not-matched-chars] (check rules-def [0] (vec (.toCharArray text)))]
     (do
       (println not-matched-chars)
       (empty? not-matched-chars)
       )
     )))

(deftest check-test
  (testing "check"
    (is (= true (check {0 \a} "a")))
    (is (= true (check {0 [1 1 1], 1 \a} "aaa")))
    (is (= false (check {0 [[1 1 1] []], 1 \a} "b")))
    (is (= false (check {0 [1 1 1], 1 \a} "aab")))
    (is (= true (check {0 [[1 1][2 2]], 1 \a, 2 \b} "aa")))
    (is (= true (check {0 [[1 1][2 2]], 1 \a, 2 \b} "bb")))
    (is (= false (check {0 [[1 1][2 2]], 1 \a, 2 \b} "ab")))
    (is (= false (check {0 [[1 1][2 2]], 1 \a, 2 \b} "a")))
    (is (= false (check {0 [[1 1][2 2]], 1 \a, 2 \b} "aaa")))
    (is (= true (check {0 [[1 1][1 3]], 1 \a, 2 \b, 3 [[2 1]]} "aba")))
    (is (= false (check {0 [[1 1][1 3]], 1 \a, 2 \b, 3 [[2 1]]} "abaa")))
    (is (= true (check {0 [[1 1][1 3]], 1 \a, 2 \b, 3 [[2 1 2 1]]} "ababa")))
    (is (= false (check {0 [[1 1][1 3]], 1 \a, 2 \b, 3 [[2 1 2 1]]} "abaaa")))
    (is (= true (check {0 [[1 1][]], 1 \a} "aa")))
    (is (= true (check {0 [[1 1] [1 3 1]], 1 \a, 2 \b, 3 [[2 1 2 1]]} "ababaa")))
    (is (= true (check rules-def "ababbb")))
    (is (= true (check rules-def "abbbab")))
    (is (= false (check rules-def "bababa")))
    (is (= false (check rules-def "aaabbb")))
    (is (= false (check rules-def "aaabbb")))
    (is (= false (check rules-def "aaaabbb")))

    (is (= true (check rules-def "aaaabb")))
    (is (= true (check rules-def "aaabab")))
    (is (= true (check rules-def "abbabb")))
    (is (= true (check rules-def "abbbab")))
    (is (= true (check rules-def "aabaab")))
    (is (= true (check rules-def "aabbbb")))
    (is (= true (check rules-def "abaaab")))
    (is (= true (check rules-def "ababbb")))

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

;(defn solve-1
;  [input]
;  (let [[rules-def-input messages] (str/split input #"\n\n")
;        rules-def (parse-rule-def rules-def-input)
;        messages (str/split-lines messages)
;        possible-matches (set (gen rules-def [0]))
;        matched (map #(contains? possible-matches %) messages)
;        ]
;    (count (filter true? matched))
;    ))

(defn solve-1
  [input]
  (let [[rules-def-input messages] (str/split input #"\n\n")
        rules-def (parse-rule-def rules-def-input)
        _ (println rules-def)
        messages ["aabbabbbbbbbaaaaaabaaabb"]
        matched (map #(check rules-def %) messages)
        _ (println matched)
        ;possible-matches (set (gen rules-def [0]))
        ;matched (map #(contains? possible-matches %) messages)
        ;_ (println matched)
        ]
    (count (filter true? matched))
    ))

(deftest solve-1-test
  (testing "solve-1"
    (is (= 113 (solve-1 puzzle-input)))
    ))