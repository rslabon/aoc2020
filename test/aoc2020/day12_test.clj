(ns aoc2020.day12-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day12 :refer :all]
            [clojure.java.io :as io]))

;(def input (slurp (io/resource "day12.txt")))

(deftest parse-instruction-test
  (testing "parse-instruction"

    (is (= [:east 1] (parse-instruction "E1")))
    (is (= [:west 1] (parse-instruction "W1")))
    (is (= [:north 1] (parse-instruction "N1")))
    (is (= [:south 1] (parse-instruction "S1")))
    (is (= [:left 1] (parse-instruction "L1")))
    (is (= [:right 1] (parse-instruction "R1")))
    (is (= [:forward 1] (parse-instruction "F1")))

    ))

(deftest count-directions-test
  (testing "count-directions"

    (is (= 25 (count-directions {:x 17 :y -8 :fx nop :fy -})))

    ))

(deftest nav-test
  (testing "nav"

    (is (= {:x 10 :y 0 :fx + :fy nop}
           (nav {:x 0 :y 0 :fx + :fy nop} "F10")))

    (is (= {:x 10 :y 3 :fx + :fy nop}
           (nav {:x 10 :y 0 :fx + :fy nop} "N3")))

    (is (= {:x 17 :y 3 :fx + :fy nop}
           (nav {:x 10 :y 3 :fx + :fy nop} "F7")))

    (is (=  {:x 17 :y 3 :fx nop :fy -}
           (nav {:x 17 :y 3 :fx + :fy nop} "R90")))

    (is (= {:x 17 :y -8 :fx nop :fy -}
           (nav {:x 17 :y 3 :fx nop :fy -} "F11")))

    ))

(deftest solve-1-test
  (testing "solve 1"

    ;(is (= 582 (solve-1 input)))

    ))