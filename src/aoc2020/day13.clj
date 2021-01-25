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

(defn found-timestamp?
  [timestamp timetable]
  (let [timestamps (mapv (fn [[bus-id offset]] (mod (+ timestamp offset) bus-id)) timetable)]
    (every? #(= 0 %) timestamps)
    ))

(defn parse-2
  [input]
  (let [[_ bus-ids] (str/split-lines input)
        bus-ids (str/split bus-ids #",")
        bus-ids (map-indexed (fn [idx bus-id] [bus-id idx]) bus-ids)
        bus-ids (filter (fn [[bus-id _]] (not= "x" bus-id)) bus-ids)
        bus-ids (mapv (fn [[bus-id idx]] [(Long/parseLong bus-id) idx]) bus-ids)]
    bus-ids))

(defn inv-mod
  [a b]
  (.modInverse (biginteger a) (biginteger b)))

(defn chinese-remainder
  [n a]
  (let [N (reduce * n)
        result (reduce + (for [i (range (count n))]
                           (let [ai (nth a i)
                                 ni (nth n i)
                                 mi (bigint (Math/floor (/ N ni)))
                                 ci (* mi (inv-mod mi ni))]
                             (* ai ci))))]
    (mod result N)))

(defn solve-2
  [timetable]
  (let [new-timetable (mapv (fn [[bus-id offset]] [bus-id (- bus-id offset)]) timetable)
        n (mapv first new-timetable)
        a (mapv second new-timetable)]
    (chinese-remainder n a)))