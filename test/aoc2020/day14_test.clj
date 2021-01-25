(ns aoc2020.day14-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day14 :refer :all]
            [clojure.java.io :as io]))


(def example-input "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\nmem[8] = 11\nmem[7] = 101\nmem[8] = 0")
(def example-input2 "mask = 000000000000000000000000000000X1001X\nmem[42] = 100\nmask = 00000000000000000000000000000000X0XX\nmem[26] = 1")

(deftest write-mem-test
  (testing "write-mem"

    (is (= {8 2r1011 :mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"}
           (write-mem {:mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"} 8 2r1011)))

    (is (= {8 2r1011 :mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"}
           (write-mem {8 1000 :mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"} 8 2r1011)))

    (is (= {8 2r1001001 :mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"}
           (write-mem {:mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"} 8 2r1011)))

    ))

(deftest apply-line-test
  (testing "apply-line"

    (is (= {:mask "XX0101"}
           (apply-line "mask = XX0101" {})))

    (is (= {1 6 :mask "XXXX"}
           (apply-line "mem[1] = 6" {:mask "XXXX"})))

    ))

(deftest solve-1-test
  (testing "solve-1"

    (is (= 165 (solve-1 example-input)))
    (is (= 16003257187056 (solve-1 (slurp (io/resource "day14.txt")))))

    ))

(deftest write-mem2-test
  (testing "write-mem2"

    (is (= ["0" "1"] (floating-comb "X" 0)))
    (is (= ["00" "01" "10" "11"] (floating-comb "XX" 0)))
    (is (= ["000" "010"] (floating-comb "0X0" 0)))
    (is (= ["011010" "011011" "111010" "111011"] (floating-comb "X1101X" 0)))

    (is (= {2r011010 19 2r011011 19 2r111010 19 2r111011 19 :mask "X1001X"}
           (write-mem2 {:mask "X1001X"} 2r101010 19)))

    ))

(deftest solve-2-test
  (testing "solve-2"

    (is (= 208 (solve-2 example-input2)))
    (is (= 3219837697833 (solve-2 (slurp (io/resource "day14.txt")))))

    ))