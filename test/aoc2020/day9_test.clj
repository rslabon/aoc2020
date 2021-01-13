(ns aoc2020.day9-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day9 :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]))



(def xmas-puzzle (vec (map read-string (str/split (slurp (io/resource "day9.txt")) #"\n"))))
(def xmas-input [35 20 15 25 47 40 62 55 65 95 102 117 150 182 127 219 299 277 309 576])

(deftest sum-test
  (is (= true (sum? [1 2 3] 3)))
  (is (= true (sum? [1 2 3] 5)))
  (is (= true (sum? [1 2 3] 4)))
  (is (= false (sum? [2 4] 4)))
  (is (= false (sum? [1 2 3] 6)))
  (is (= false (sum? [1 2 3] 7)))
  )

(deftest xmas-test
  (is (= 127 (xmas xmas-input 5)))
  (is (= 776203571 (xmas xmas-puzzle 25)))
  (is (= 104800569 (xmas-2 xmas-puzzle 776203571)))
  )

(deftest xmas2-test
  (is (= 62 (xmas-2 xmas-input 127)))
  )