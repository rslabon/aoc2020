(ns aoc2020.day14-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day14 :refer :all]
            [clojure.java.io :as io]))


(def example-input "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\nmem[8] = 11\nmem[7] = 101\nmem[8] = 0")
(def example-input2 "mask = 000000000000000000000000000000X1001X\nmem[42] = 100\nmask = 00000000000000000000000000000000X0XX\nmem[26] = 1")

(deftest write-mem-test
  (testing "write-memory"

    (is (= {8 2r1011 :mask "XXXX"}
           (write-memory {:mask "XXXX"} 8 2r1011)))

    (is (= {8 2r1011 :mask "XXXXX"}
           (write-memory {8 1000 :mask "XXXXX"} 8 2r1011)))

    (is (= {8 2r1001001 :mask "1XXXX0X"}
           (write-memory {:mask "1XXXX0X"} 8 2r1011)))

    ))

(deftest apply-line-test
  (testing "apply-line"

    (is (= {:mask "XX0101"}
           (apply-line "mask = XX0101" {} write-memory)))

    (is (= {1 6 :mask "XXXX"}
           (apply-line "mem[1] = 6" {:mask "XXXX"} write-memory)))

    ))

(deftest solve-1-test
  (testing "solve-1"

    (is (= 165 (solve-1 example-input)))
    (is (= 16003257187056 (solve-1 (slurp (io/resource "day14.txt")))))

    ))

(deftest write-mem2-test
  (testing "write-memory2"

    (is (= ["0" "1"] (floating-combinations "X" 0)))
    (is (= ["00" "01" "10" "11"] (floating-combinations "XX" 0)))
    (is (= ["000" "010"] (floating-combinations "0X0" 0)))
    (is (= ["011010" "011011" "111010" "111011"] (floating-combinations "X1101X" 0)))

    (is (= {2r011010 19 2r011011 19 2r111010 19 2r111011 19 :mask "X1001X"}
           (write-memory2 {:mask "X1001X"} 2r101010 19)))

    ))

(deftest solve-2-test
  (testing "solve-2"

    (is (= 208 (solve-2 example-input2)))
    (is (= 3219837697833 (solve-2 (slurp (io/resource "day14.txt")))))

    ))