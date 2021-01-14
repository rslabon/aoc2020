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

(defn count-directions
  [state]
  (+ (Math/abs (:x state)) (Math/abs (:y state))))

(defn nop
  [x & y]
  x)

(defn turn
  [fx fy degree direction]
  (let [clockwise [{:fx nop :fy -} {:fx - :fy nop} {:fx nop :fy +} {:fx + :fy nop}]
        idx (.indexOf clockwise {:fx fx :fy fy})
        size (count clockwise)
        n (/ degree 90)
        next-fn (condp = direction
                  :right #(+ % n)
                  :left #(- % n))
        next-idx (mod (next-fn idx) size)
        ]
    (nth clockwise next-idx)
    ))

(defn nav
  [state instruction]
  (let [[action value] (parse-instruction instruction)
        x (:x state)
        y (:y state)
        fx (:fx state)
        fy (:fy state)]
    (condp = action
      :forward (merge state {:x (fx x value) :y (fy y value)})
      :north (assoc state :y (+ y value))
      :south (assoc state :y (- y value))
      :east (assoc state :x (+ x value))
      :west (assoc state :x (- x value))
      :right (merge state (turn fx fy value action))
      :left (merge state (turn fx fy value action))
      )))

(defn solve-1
  [input]
  (let [lines (str/split-lines input)
        initial-state {:x 0 :y 0 :fx + :fy nop}]
    (count-directions (reduce nav initial-state lines))))