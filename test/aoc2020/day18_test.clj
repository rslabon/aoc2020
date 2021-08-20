(ns aoc2020.day18-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day18 :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(deftest parse-expr-test
  (testing "parse-expr"
    (is (= [[1 '+ 1]] (parse-expr "(1 + 1)")))
    (is (= [[[2 '+ 4 '* 9] '* [6 '+ 9 '* 8 '+ 6] '+ 6] '+ 2 '+ 4 '* 2] (parse-expr "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))
    ))

(deftest calc-test
  (testing "calc"
    (is (= 1 (calc 1)))
    (is (= nil (calc [])))
    (is (= 1 (calc [1])))
    (is (= 2 (calc [1 '+ 1])))
    (is (= 2 (calc [1 + 1])))
    (is (= 2 (calc [[1 + 1]])))
    (is (= 3 (calc [[1 + 1] + 1])))
    (is (= 71 (calc [1 + 2 * 3 + 4 * 5 + 6])))
    (is (= 51 (calc [1 + [2 * 3] + [4 * [5 + 6]]])))
    (is (= 13632 (calc [[[2 + 4 * 9] * [6 + 9 * 8 + 6] + 6] + 2 + 4 * 2])))
    (is (= 13632 (calc [[[2 '+ 4 '* 9] '* [6 '+ 9 '* 8 '+ 6] '+ 6] '+ 2 '+ 4 '* 2])))
    (is (= 13632 (calc (parse-expr "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))
    ))

(def input-line (str/split (slurp (io/resource "day18.txt")) #"\n"))

(defn solve-1 [lines]
  (let [expressions (map (comp calc parse-expr) lines)]
    (apply + expressions)))

(deftest solve-1-test
  (testing "solve-1"
    (is (= 1408133923393 (solve-1 input-line)))
    ))

(defn precedence
  [expressions]
  (cond
    (empty? expressions) expressions
    (< (count expressions) 3) expressions
    :else (let [first (nth expressions 0)
                second (nth expressions 1)
                third (nth expressions 2)
                _ (println expressions "|" first "|" second "|" third)]
            (cond
              (symbol? first) (vec (concat [first] (precedence (drop 1 expressions))))
              (= second '+) (vec (concat [[first second third]] (precedence (drop 3 expressions))))
              :else (vec (concat [first second] (precedence (drop 2 expressions))))
              ))))

(deftest precedence-test
  (testing "precedence"
    ;(is (= [1 '* 2] (precedence [1 '* 2])))
    ;(is (= [[1 '+ 2]] (precedence [1 '+ 2])))
    ;(is (= [[1 '+ 2] '* [3 '+ 4] '* [5 '+ 6]] (precedence [1 '+ 2 '* 3 '+ 4 '* 5 '+ 6])))
    ;(is (= [2 '* [3 '+ [4 '* 5]]] (precedence [2 '* 3 '+ [4 '* 5]])))
    (is (= [5 '+ [8 '* [[3 '+ 9] '+ 3] '* 4 '* 3]] (precedence [5 '+ [8 '* 3 '+ 9 '+ 3 '* 4 '* 3]])))


    ;(is (= [[[[[2 '+ 4] '* 9] '* [[6 '+ 9] '* [8 '+ 6]] '+ 6] '+ 2] '+ 4 '* 2] (precedence [[[2 + 4 * 9] * [6 + 9 * 8 + 6] + 6] + 2 + 4 * 2])))
    ;(is (= 23340 (calc (comp precedence parse-expr "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))
    ))

(defn solve-2 [lines]
  (let [expressions (map (comp calc precedence parse-expr) lines)]
    (apply + expressions)))

(deftest solve-2-test
  (testing "solve-2"
    (is (= 0 (solve-2 input-line)))
    ))
