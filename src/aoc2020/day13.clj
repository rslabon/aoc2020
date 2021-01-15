(ns aoc2020.day13
  (:require [clojure.string :as str]))

(defn multiple-or-greater
  [number min]
  (let [value (* number (int (/ min number)))
        value (if (< value min)
                (+ value number)
                value)]
    value
    ))

(defn find-bus-id-and-delay
  [timestamp bus-ids]
  (let [[bus-timestamp bus-id] (first (sort (map
                                                (fn [bus-id] [(multiple-or-greater bus-id timestamp) bus-id])
                                                bus-ids)))
        delay (- bus-timestamp timestamp)]
    [bus-id delay]))

(defn parse
  [input]
  (let [[timestamp bus-ids] (str/split-lines input)
        timestamp (read-string timestamp)
        bus-ids (str/split bus-ids #",")
        bus-ids (filter #(not= % "x") bus-ids)
        bus-ids (map read-string bus-ids)]
    [timestamp bus-ids]))

(defn solve-1
  [input]
  (let [[timestamp bus-ids] (parse input)
        [bus-id delay] (find-bus-id-and-delay timestamp bus-ids)]
    (* bus-id delay)))