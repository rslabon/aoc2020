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

;(deftest solve-1-test
;  (testing "solve-1"
;    (is (= 1408133923393 (solve-1 input-line)))
;    ))

(deftest precedence-test
  (testing "precedence"
    (is (= [1] (precedence 1)))
    (is (= ['+] (precedence '+)))
    (is (= [1 '* 2] (precedence [1 '* 2])))
    (is (= [[[1 '* 2] '+ 3]] (precedence [[1 '* 2] '+ 3])))
    (is (= [1 '* 2 '* 3] (precedence [1 '* 2 '* 3])))
    (is (= [[1 '+ 2]] (precedence [1 '+ 2])))
    (is (= [[[1 '+ 2] '+ 3]] (precedence [1 '+ 2 '+ 3])))
    (is (= [[1 '* [2 '+ 3]] '* [[[4 '+ 5] '+ 6]]] (precedence [[1 '* 2 '+ 3] '* [4 '+ 5 '+ 6]])))

    (is (= 51 (calc (precedence (parse-expr "1 + (2 * 3) + (4 * (5 + 6))")))))
    (is (= 46 (calc (precedence (parse-expr "2 * 3 + (4 * 5)")))))
    (is (= 1445 (calc (precedence (parse-expr "5 + (8 * 3 + 9 + 3 * 4 * 3)")))))
    (is (= 669060 (calc (precedence (parse-expr "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))))
    (is (= 23340 (calc (precedence (parse-expr "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))))
    ))

(defn solve-2 [lines]
  (let [expressions (map (comp calc precedence parse-expr) lines)]
    (apply + expressions)))

;(deftest solve-2-test
;  (testing "solve-2"
;    (is (= 314455761823725 (solve-2 input-line)))
;    ))
