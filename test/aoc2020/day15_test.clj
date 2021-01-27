(ns aoc2020.day15-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day15 :refer :all]))

(def input [12 1 16 3 11 0])

(deftest solve-test
  (testing "solve"

    (is (= 0 (solve [0 3 6] 4)))
    (is (= 3 (solve [0 3 6] 5)))
    (is (= 3 (solve [0 3 6] 6)))
    (is (= 1 (solve [0 3 6] 7)))
    (is (= 0 (solve [0 3 6] 8)))
    (is (= 4 (solve [0 3 6] 9)))
    (is (= 0 (solve [0 3 6] 10)))

    (is (= 436 (solve [0 3 6] 2020)))
    (is (= 1 (solve [1 3 2] 2020)))
    (is (= 10 (solve [2 1 3] 2020)))
    (is (= 27 (solve [1 2 3] 2020)))
    (is (= 78 (solve [2 3 1] 2020)))
    (is (= 438 (solve [3 2 1] 2020)))
    (is (= 1836 (solve [3 1 2] 2020)))

    (is (= 1696 (solve input 2020)))
    (is (= 6895259 (solve [2 3 1] 30000000)))
    (is (= 37385 (solve input 30000000)))
    ))