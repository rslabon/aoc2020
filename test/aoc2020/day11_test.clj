(ns aoc2020.day11-test
  (:require [clojure.test :refer :all]
            [aoc2020.day11 :refer :all]
            [clojure.java.io :as io]))

(def example-input (slurp (io/resource "day11_example.txt")))
(def problem-input (slurp (io/resource "day11.txt")))

(deftest parse-grid-test
  (is (= [[:floor :empty-seat :occupied-seat]] (parse-grid ".L#")))
  (is (= [[:floor :floor :floor]
          [:empty-seat :empty-seat :empty-seat]
          [:occupied-seat :occupied-seat :occupied-seat]] (parse-grid "...\nLLL\n###")))
  )

(deftest adj-test

  (is (= [:empty-seat] (adj-first [[:floor :empty-seat]] 0 0)))

  (is (= [:floor] (adj-first [[:floor :empty-seat]] 0 1)))

  (is (= [:occupied-seat] (adj-first [[:floor]
                                      [:occupied-seat]] 0 0)))

  (is (= [:floor] (adj-first [[:floor]
                              [:occupied-seat]] 1 0)))

  (is (= [:empty-seat
          :occupied-seat
          :floor
          :empty-seat
          :empty-seat
          :occupied-seat
          :empty-seat
          :floor] (adj-first [[:floor :empty-seat :empty-seat]
                              [:occupied-seat :floor :empty-seat]
                              [:occupied-seat :floor :empty-seat]] 1 1)))

  (is (= [:floor
          :empty-seat
          :floor] (adj-first [[:floor :empty-seat :empty-seat]
                              [:occupied-seat :floor :empty-seat]
                              [:occupied-seat :floor :empty-seat]] 2 2)))
  )

(deftest apply-rules-to-cell-test
  (testing "floor"
    (is (= :floor (rules :floor [:floor :empty-seat :occupied-seat]))))

  (testing "empty"
    (is (= :empty-seat (rules :empty-seat [:floor :occupied-seat])))
    (is (= :occupied-seat (rules :empty-seat [:floor :empty-seat])))
    )

  (testing "occupied"
    (is (= :occupied-seat (rules :occupied-seat [:floor :floor :floor :floor])))
    (is (= :occupied-seat (rules :occupied-seat [:empty-seat :empty-seat :empty-seat :empty-seat])))
    (is (= :empty-seat (rules :occupied-seat [:occupied-seat :occupied-seat :occupied-seat :occupied-seat])))
    (is (= :empty-seat (rules :occupied-seat [:occupied-seat :occupied-seat :occupied-seat :occupied-seat :occupied-seat])))
    )
  )

(deftest grid-apply-rules-test
  (testing "floor never changes"
    (let [grid [[:floor :floor :floor]
                [:floor :floor :floor]
                [:floor :floor :floor]]]
      (is (= grid (grid-apply grid rules adj-first)))))

  (testing "become occupied"
    (let [grid [[:floor :floor :floor]
                [:empty-seat :empty-seat :floor]
                [:floor :floor :floor]]

          result [[:floor :floor :floor]
                  [:occupied-seat :occupied-seat :floor]
                  [:floor :floor :floor]]
          ]
      (is (= result (grid-apply grid rules adj-first))))

    (let [grid [[:empty-seat :empty-seat :empty-seat]
                [:empty-seat :empty-seat :empty-seat]
                [:empty-seat :empty-seat :empty-seat]]

          result [[:occupied-seat :occupied-seat :occupied-seat]
                  [:occupied-seat :occupied-seat :occupied-seat]
                  [:occupied-seat :occupied-seat :occupied-seat]]
          ]
      (is (= result (grid-apply grid rules adj-first))))

    (let [grid [[:empty-seat :floor :empty-seat]
                [:occupied-seat :floor :occupied-seat]
                [:floor :floor :floor]
                [:empty-seat :empty-seat :empty-seat]]

          result [[:empty-seat :floor :empty-seat]
                  [:occupied-seat :floor :occupied-seat]
                  [:floor :floor :floor]
                  [:occupied-seat :occupied-seat :occupied-seat]]
          ]
      (is (= result (grid-apply grid rules adj-first))))

    )

  (testing "become empty"
    (let [grid [[:floor :occupied-seat :floor]
                [:occupied-seat :occupied-seat :occupied-seat]
                [:floor :occupied-seat :floor]]

          result [[:floor :occupied-seat :floor]
                  [:occupied-seat :empty-seat :occupied-seat]
                  [:floor :occupied-seat :floor]]
          ]
      (is (= result (grid-apply grid rules adj-first))))

    (let [grid [[:floor :occupied-seat :floor]
                [:occupied-seat :occupied-seat :occupied-seat]
                [:floor :occupied-seat :floor]]

          result [[:floor :occupied-seat :floor]
                  [:occupied-seat :empty-seat :occupied-seat]
                  [:floor :occupied-seat :floor]]
          ]
      (is (= result (grid-apply grid rules adj-first))))

    (let [grid [[:occupied-seat :occupied-seat :occupied-seat]
                [:occupied-seat :occupied-seat :occupied-seat]
                [:occupied-seat :occupied-seat :occupied-seat]]

          result [[:occupied-seat :empty-seat :occupied-seat]
                  [:empty-seat :empty-seat :empty-seat]
                  [:occupied-seat :empty-seat :occupied-seat]]
          ]
      (is (= result (grid-apply grid rules adj-first)))
      ))

  )

(deftest example-test
  (is (= 37 (seating-system example-input rules adj-first))))

;(deftest problem-1
;  (is (= 2113 (seating-system problem-input rules adj-first))))

(deftest adj2-test
  (testing "right"
    (is (= [:occupied-seat] (adj-first-visible [[:empty-seat :floor :floor :occupied-seat :empty-seat]] 0 0)))
    (is (= [] (adj-first-visible [[:empty-seat]] 0 0)))
    )

  (testing "left"
    (is (= [:occupied-seat] (adj-first-visible [[:empty-seat :occupied-seat :floor :floor :empty-seat]] 0 4)))
    (is (= [] (adj-first-visible [[:empty-seat]] 0 0)))
    )

  (testing "down"
    (is (= [:occupied-seat] (adj-first-visible [[:empty-seat]
                                                [:floor]
                                                [:occupied-seat]] 0 0)))
    (is (= [] (adj-first-visible [[:empty-seat]] 0 0)))
    )

  (testing "up"
    (is (= [:empty-seat] (adj-first-visible [[:empty-seat]
                                             [:floor]
                                             [:occupied-seat]] 2 0)))
    (is (= [] (adj-first-visible [[:empty-seat]] 0 0)))
    )

  (testing "diagonal-down-right"
    (is (= [:occupied-seat :empty-seat :occupied-seat] (adj-first-visible [[:empty-seat :occupied-seat :floor]
                                                                           [:empty-seat :floor :floor]
                                                                           [:floor :floor :occupied-seat]] 0 0)))
    )

  (testing "diagonal-down-left"
    (is (= [:occupied-seat :empty-seat :occupied-seat] (adj-first-visible [[:empty-seat :occupied-seat :empty-seat]
                                                                           [:empty-seat :floor :empty-seat]
                                                                           [:occupied-seat :floor :floor]] 0 2)))
    )

  (testing "diagonal-up-right"
    (is (= [:empty-seat :empty-seat :empty-seat] (adj-first-visible [[:empty-seat :occupied-seat :empty-seat]
                                                                     [:empty-seat :floor :empty-seat]
                                                                     [:occupied-seat :floor :empty-seat]] 2 0)))
    )

  (testing "diagonal-up-left"
    (is (= [:occupied-seat :empty-seat :empty-seat] (adj-first-visible [[:empty-seat :occupied-seat :empty-seat]
                                                                        [:empty-seat :floor :empty-seat]
                                                                        [:occupied-seat :floor :empty-seat]] 2 2)))
    )

  )

(deftest example-test2
  (is (= 26 (seating-system example-input rules2 adj-first-visible))))

;(deftest problem-2
;  (is (= 1865 (seating-system problem-input rules2 adj2))))