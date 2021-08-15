(ns aoc2020.day17-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day17 :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(deftest get-grid-3d-test
  (testing "get-grid-3d"
    (is (= :active (get-grid-3d {[1 0 0] :active} 1 0 0)))
    ))

(deftest neighbors-test
  (testing "neighbors"
    (is (= 26 (count (neighbors 0 0 0))))
    ))

(deftest next-cube-state-test
  (testing "next-cube-state"
    (is (= :active (next-cube-state :active [:active :active :inactive])))
    (is (= :active (next-cube-state :active [:active :active :active])))
    (is (= :inactive (next-cube-state :active [:active :active :active :active])))
    (is (= :active (next-cube-state :inactive [:active :inactive :active :active])))
    (is (= :inactive (next-cube-state :inactive [:active :active :inactive])))
    ))

(deftest parse-test
  (testing "parse"
    (is (= {[0 1 0] :active} (parse ".#.")))
    (is (= {[0 1 0] :active [1 2 0] :active
            [2 0 0] :active [2 1 0] :active [2 2 0] :active
            } (parse ".#.\n..#\n###")))
    ))

(deftest boundaries-test
  (testing "boundaries"
    (is (= {:x [-1 1], :y [0 2], :z [2 5]} (boundaries [[1 0 2] [-1 2 5]])))
    (is (= {:x [0 0], :y [0 0], :z [0 0]} (boundaries [])))
    ))

(deftest count-active-test
  (testing "count-active"
    (is (= 5 (count-active (parse ".#.\n..#\n###"))))
    ))

(deftest next-state-test
  (testing "next-state"
    (is (= [[0 0 0] :inactive] (next-state (parse ".#.\n..#\n###") 0 0 0)))
    (is (= [[0 1 0] :inactive] (next-state (parse ".#.\n..#\n###") 0 1 0)))
    (is (= [[0 2 0] :inactive] (next-state (parse ".#.\n..#\n###") 0 2 0)))
    (is (= [[2 0 0] :inactive] (next-state (parse ".#.\n..#\n###") 2 0 0)))
    (is (= [[2 1 0] :active] (next-state (parse ".#.\n..#\n###") 2 1 0)))
    ))

(deftest next-grid-test
  (testing "next-grid"
    (is (= 11 (count-active (next-grid (parse ".#.\n..#\n###")))))
    (is (= 21 (count-active (next-grid (next-grid (parse ".#.\n..#\n###"))))))
    ))

(deftest solve-1-test
  (testing "solve-1"
    (is (= 112 (count-active (solve-1 (parse ".#.\n..#\n###")))))
    ;(is (= 263 (count-active (solve-1 (parse (slurp (io/resource "day17.txt")))))))
    ;(is (= 1680 (count-active (solve-2 (parse-2 (slurp (io/resource "day17.txt")))))))
    ))
