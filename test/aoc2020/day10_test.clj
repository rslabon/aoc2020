(ns aoc2020.day10-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day10 :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str])
  )

(def example-input [1 4 5 6 7 10 11 12 15 16 19])
(def example-input2 [1 2 3 4 7 8 9 10 11 14 17 18 19 20 23 24 25 28 31 32 33 34 35 38 39 42 45 46 47 48 49])
(def problem-input (vec (sort (map read-string (str/split (slurp (io/resource "day10.txt")) #"\n")))))

(deftest find-next-number-test
  (is (= [0 1 2] (find-next-indexes [1 2 3 4] -1)))
  (is (= [0] (find-next-indexes [1 10] -1)))
  (is (= [0] (find-next-indexes example-input -1)))
  (is (= [1] (find-next-indexes example-input 0)))
  (is (= [2 3 4] (find-next-indexes example-input 1)))
  (is (= [3 4] (find-next-indexes example-input 2)))
  (is (= [4] (find-next-indexes example-input 3)))
  (is (= [5] (find-next-indexes example-input 4)))
  (is (= [6 7] (find-next-indexes example-input 5)))
  (is (= [7] (find-next-indexes example-input 6)))
  (is (= [8] (find-next-indexes example-input 7)))
  (is (= [9] (find-next-indexes example-input 8)))
  (is (= [10] (find-next-indexes example-input 9)))
  (is (= [] (find-next-indexes example-input 10)))
  )

(deftest difference-finder-test
  (is (= 35 (difference-finder example-input))))

(deftest difference-finder-test2
  (is (= 220 (difference-finder example-input2))))

(deftest difference-finder-test3
  (is (= 1876 (difference-finder problem-input))))

(deftest count-arrangements-test
  (testing "count-arrangements"

    (is (= 8 (count-arrangements example-input -1)))
    (is (= 19208 (count-arrangements example-input2 -1)))
    ;(is (= 14173478093824 (count-arrangements problem-input -1)))

    ))

