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
