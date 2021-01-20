(ns aoc2020.day13-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day13 :refer :all]
            [clojure.java.io :as io]))

(deftest find-test
  (testing "find"

    (is (= [59 5] (find-bus-id-and-delay 939 [7 13 59 31 19])))

    ))

(deftest solve-1-test
  (testing "solve-1"

    (is (= 3789 (solve-1 (slurp (io/resource "day13.txt")))))

    ))

(deftest found-timestamp?-test
  (testing "found-timestamp?"

    (is (= true (found-timestamp? 1068781 [[7 0],[13 1],[59 4],[31 6],[19 7]])))

    ))

(deftest solve-2-example
  (testing "solve-2-example"

    (is (= 1068781 (solve-2 [[7 0],[13 1],[59 4],[31 6],[19 7]])))
    (is (= 3417 (solve-2 [[17 0],[13 2],[19 3]])))
    (is (= 1202161486 (solve-2 [[1789 0],[37 1],[47 2][1889 3]])))
    (is (= 1261476 (solve-2 [[67 0],[7 1],[59 3][61 4]])))

    ))

(deftest solve-2-input
  (testing "solve-2-input"

    (is (= 0 (solve-2 (parse-2 (slurp (io/resource "day13.txt"))))))

    ))
