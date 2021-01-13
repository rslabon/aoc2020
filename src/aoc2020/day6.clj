(ns aoc2020.day6
  (:require [clojure.string :as str]))

(defn custom-customs
  [input]
  (let [groups (str/split input #"\n\n")
        count-yes (fn [group] (count (set (.toCharArray (str/replace group #"\n" "")))))]
    (reduce + (map count-yes groups))))



