(ns aoc2020.day6-test
  (:require [clojure.test :refer :all]
            [aoc2020.day6 :refer :all]))

(deftest custom-customs-test
  (testing "row"
    (is (= 0 (custom-customs "")))
    (is (= 1 (custom-customs "a")))
    (is (= 1 (custom-customs "aa")))
    (is (= 2 (custom-customs "ab")))
    (is (= 3 (custom-customs "abc")))
    )
  (testing "column"
    (is (= 0 (custom-customs "\n")))
    (is (= 1 (custom-customs "a\n")))
    (is (= 2 (custom-customs "a\nb")))
    (is (= 3 (custom-customs "a\nb\nc")))
    (is (= 2 (custom-customs "a\nb\na")))
    )
  (testing "row & column"
    (is (= 3 (custom-customs "a\nbc"))))
    (is (= 11 (custom-customs "abc\n\na\nb\nc\n\nab\nac\n\na\na\na\na\n\nb")))
  )
