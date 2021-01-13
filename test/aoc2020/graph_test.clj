(ns aoc2020.graph-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.graph :refer :all]))

(def empty-graph {:nodes {}})
(def nodeA {:value "A" :adj #{}})

(deftest get-node-test
  (is (= nodeA (get-node {:nodes {:A nodeA}} "A")))
  (is (= nodeA (get-node empty-graph "A")))
  )

(deftest add-node-test
  (let [graph (add-edge empty-graph "A" "B")
        graph (add-edge graph "A" "C")
        graph (add-edge graph "B" "C")
        ]
    (is (= {:nodes {
                    :A {:value "A", :adj #{"C" "B"}},
                    :B {:value "B", :adj #{"C"}},
                    :C {:value "C", :adj #{}}}}
           graph)))
  )