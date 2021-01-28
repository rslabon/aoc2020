(ns aoc2020.day16-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day16 :refer :all]
            [clojure.java.io :as io]))

(def input (slurp (io/resource "day16.txt")))
(def example "class: 1-3 or 5-7\nrow: 6-11 or 33-44\nseat: 13-40 or 45-50\n\nyour ticket:\n7,1,14\n\nnearby tickets:\n7,3,47\n40,4,50\n55,2,20\n38,6,12")
(def example2 "class: 0-1 or 4-19\nrow: 0-5 or 8-19\nseat: 0-13 or 16-19\n\nyour ticket:\n11,12,13\n\nnearby tickets:\n3,9,18\n15,1,5\n5,14,9")

(deftest parse-test
  (is (= {"x" [[27 374] [395 974]] "y" [[40 287] [295 953]]}
         (parse-rules "x: 27-374 or 395-974\ny: 40-287 or 295-953")))

  (is (= [[7, 1, 14]] (parse-tickets "your ticket:\n7,1,14")))
  )

(deftest in-range-test
  (is (= true (in-range? 28 [[27 374] [395 974]])))
  (is (= true (in-range? 395 [[27 374] [395 974]])))
  (is (= true (in-range? 974 [[27 374] [395 974]])))
  (is (= true (in-range? 27 [[27 374] [395 974]])))
  (is (= false (in-range? 26 [[27 374] [395 974]])))
  (is (= false (in-range? 975 [[27 374] [395 974]])))
  )

(deftest find-invalid-test
  (is (= [] (find-invalid [1 2 3] [[1 3]])))
  (is (= [1] (find-invalid [1 2 3] [[2 4]])))
  (is (= [1 6 10] (find-invalid [1 2 3 6 8 9 10] [[2 4] [7 9]])))
  )

(deftest find-invalid-test
  (is (= [] (find-invalid [1 2 3] [[1 3]])))
  (is (= [1] (find-invalid [1 2 3] [[2 4]])))
  (is (= [1 6 10] (find-invalid [1 2 3 6 8 9 10] [[2 4] [7 9]])))
  )

(deftest solve-1-test
  (is (= 71 (solve-1 example)))
  (is (= 26941 (solve-1 input)))
  )

(deftest is-valid-ticket-test
  (is (= true (is-valid-ticket? [[1 3]] [1 2 3])))
  (is (= false (is-valid-ticket? [[1 3]] [1 2 4])))
  (is (= false (is-valid-ticket? [[1 3] [5 6]] [8])))
  (is (= true (is-valid-ticket? [[1 3] [5 6]] [1 2 3 5 6])))
  )

(deftest find-possibilities-test
  (is (= ["seat"]
         (match-rules {"class" [[0 1] [4 19]] "seat" [[0 13] [16 19]]}
                      [18 3 9]
                      )))

  (is (= [["class" "seat"] ["class" "seat"]]
         (find-possibilities {"class" [[0 1] [4 19]] "seat" [[0 13] [16 19]]}
                             [[0 1] [0 1]]
                             )))
  )

(deftest get-column-test
  (is (= [3 15 5] (get-column [[3 9 18] [15 1 5] [5 14 9]] 0)))
  )

(deftest choose-best-test
  (is (= [\a \c \d \b] (choose-best [[\a \b] [\a \b \c] [\a \d] [\a \b \d]])))
  )

(deftest solve-2-test
  (is (= 634796407951 (solve-2 input)))
  )