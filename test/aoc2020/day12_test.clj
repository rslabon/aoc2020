(ns aoc2020.day12-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day12 :refer :all]
            [clojure.java.io :as io]))

(def input (slurp (io/resource "day12.txt")))

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

(deftest turn-test
  (testing "turn-right"

    (is (= :south (turn 90 :right :east)))
    (is (= :west (turn 90 :right :south)))
    (is (= :north (turn 90 :right :west)))
    (is (= :east (turn 90 :right :north)))

    )

  (testing "turn-left"

    (is (= :north (turn 90 :left :east)))
    (is (= :west (turn 90 :left :north)))
    (is (= :south (turn 90 :left :west)))
    (is (= :east (turn 90 :left :south)))

    (is (= :west (turn 180 :left :east)))
    (is (= :south (turn 270 :left :east)))
    (is (= :east (turn 360 :left :east)))

    ))

(deftest count-directions-test
  (testing "count-directions"

    (is (= 25 (count-directions {:north 0 :south 8 :west 0 :east 17 :current :south})))

    ))

(deftest nav-test
  (testing "nav"

    (is (= {:north 0 :south 0 :west 0 :east 10 :current :east}
           (nav {:north 0 :south 0 :west 0 :east 0 :current :east} "F10")))

    (is (= {:north 3 :south 0 :west 0 :east 10 :current :east}
           (nav {:north 0 :south 0 :west 0 :east 10 :current :east} "N3")))

    (is (= {:north 3 :south 0 :west 0 :east 17 :current :east}
           (nav {:north 3 :south 0 :west 0 :east 10 :current :east} "F7")))

    (is (= {:north 3 :south 0 :west 0 :east 20 :current :east}
           (nav {:north 3 :south 0 :west 0 :east 10 :current :east} "F10")))

    (is (= {:north 3 :south 0 :west 0 :east 17 :current :south}
           (nav {:north 3 :south 0 :west 0 :east 17 :current :east} "R90")))

    (is (= {:north 0 :south 8 :west 0 :east 17 :current :south}
           (nav {:north 3 :south 0 :west 0 :east 17 :current :south} "F11")))

    ))

(deftest solve-1-test
  (testing "solve 1"

    ;(is (= 582 (solve-1 input)))

    ))