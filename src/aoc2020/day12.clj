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

(defn move-ship-to-waypoint
  [waypoint ship]
  (merge ship {:x (+ (:x waypoint) (:x ship))
               :y (+ (:y waypoint) (:y ship))}))

(defn mul
  [state value]
  (merge state {:x (* value (:x state))
                :y (* value (:y state))}))

(defn turn2
  [degree direction state]
  (loop [n (/ degree 90)
         result state]
    (if (= n 0)
      result
      (recur (dec n)
             (condp = direction
               :right {:x (:y result) :y (* -1 (:x result))}
               :left {:x (* -1 (:y result)) :y (:x result)}
               )
             ))))

(defn nav2
  [state instruction]
  (let [[action value] (parse-instruction instruction)
        waypoint (:waypoint state)
        ship (:ship state)]
    (condp = action
      :forward {:waypoint waypoint :ship (move-ship-to-waypoint (mul waypoint value) ship)}
      :north {:waypoint (assoc waypoint :y (+ (:y waypoint) value)) :ship ship}
      :south {:waypoint (assoc waypoint :y (- (:y waypoint) value)) :ship ship}
      :east {:waypoint (assoc waypoint :x (+ (:x waypoint) value)) :ship ship}
      :west {:waypoint (assoc waypoint :x (- (:x waypoint) value)) :ship ship}
      :right {:waypoint (turn2 value action waypoint) :ship ship}
      :left {:waypoint (turn2 value action waypoint) :ship ship}
      )))

(defn solve-2
  [input]
  (let [lines (str/split-lines input)
        initial-state {:waypoint {:x 10 :y 1}
                       :ship     {:x 0 :y 0}}
        result (reduce nav2 initial-state lines)]
    (count-directions (:ship result))))