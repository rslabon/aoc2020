(ns aoc2020.day18-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day18 :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(deftest parse-test
  (testing "parse"
    (is (= [[1 '+ 1]] (parse "(1 + 1)")))
    (is (= [[[2 '+ 4 '* 9] '* [6 '+ 9 '* 8 '+ 6] '+ 6] '+ 2 '+ 4 '* 2] (parse "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))
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
    (is (= 13632 (calc (parse "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))
    ))

(def input-line (str/split (slurp (io/resource "day18.txt")) #"\n"))

(defn solve-1 [lines]
  (let [expressions (map (comp calc parse) lines)]
    (apply + expressions)))

(deftest solve-1-test
  (testing "solve-1"
    (is (= 1408133923393 (solve-1 input-line)))
    ))

