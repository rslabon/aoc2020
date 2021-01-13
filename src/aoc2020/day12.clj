(ns aoc2020.day12
  (:require [clojure.string :as str]))

(defn parse-instruction
  [instruction]
  (let [action (.charAt instruction 0)
        value (read-string (.substring instruction 1))]
    (condp = action
      \E [:east value]
      \W [:west value]
      \N [:north value]
      \S [:south value]
      \L [:left value]
      \R [:right value]
      \F [:forward value]
      )
    ))

(defn turn
  [degree direction from]
  (let [directions [:east :south :west :north]
        size (count directions)
        n (/ degree 90)
        next-fn (condp = direction
                  :right #(+ % n)
                  :left #(- % n))
        current-idx (.indexOf directions from)
        next-idx (mod (next-fn current-idx) size)]
    (nth directions next-idx)
    ))

(defn opposite
  [direction]
  (direction {:east  :west
              :west  :east
              :north :south
              :south :north}))

(defn count-directions
  [state]
  (reduce + (map #(Math/abs %) (filter number? (map last state)))))

(defn nav
  [state instruction]
  (let [[action value] (parse-instruction instruction)
        current-direction (:current state)]
    (cond
      (= current-direction action) (update state action #(+ % value))
      (= action :forward) (let [opposite-direction (opposite current-direction)]
                            (-> state
                                (update current-direction #(-> %
                                                               (+ value)
                                                               (- (opposite-direction state))))
                                (assoc opposite-direction 0)))
      (or (= action :right) (= action :left)) (let [new-direction (turn value action current-direction)]
                                                (assoc state :current new-direction))
      :else (update state action #(+ % value))
      )))

(defn solve-1
  [input]
  (let [lines (str/split-lines input)
        initial-state {:north 0 :south 0 :west 0 :east 0 :current :east}]
    (count-directions (reduce nav initial-state lines))))