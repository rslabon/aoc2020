(ns aoc2020.day7
  (:require [clojure.string :as str]))

(defn split-as-words [input] (str/split input #" "))

(defn bag
  [input]
  (let [words (split-as-words input)
        bag (str/join " " (nthrest words 1))]
    bag))

(defn to-bag-spec
  [input]
  (let [groups (map str/trim (->
                               input
                               (.replace "bags contain" ",")
                               (.replace "bags" "")
                               (.replace "bag" "")
                               (.replace "." "")
                               (str/split #",")
                               ))
        ]
    (if (str/includes? input "no other bags")
      {(str ((split-as-words input) 0) " " ((split-as-words input) 1)) []}
      {(first groups) (reduce #(merge %1 (bag %2)) [] (rest groups))}
      )))

(defn to-bags-spec
  [input]
  (reduce merge {} (map to-bag-spec (str/split input #"\n"))))

(defn- find-outer-colors
  [bags-spec color-to-find]
  (loop [colors (keys bags-spec)
         result `()]
    (if (empty? colors)
      (distinct result)
      (recur (rest colors)
             (if (some #(= % color-to-find) (get bags-spec (first colors)))
               (conj result (first colors))
               result)
             )
      )))

(defn handy-haversacks
  [input]
  (let [bags-spec (to-bags-spec input)
        find-colors (fn [colors] (reduce #(into %1 %2) `() (map #(find-outer-colors bags-spec %) colors)))]
    (loop [colors-to-find (find-colors `("shiny gold"))
           result `()]
      (if (empty? colors-to-find)
        (count (distinct result))
        (recur
          (find-colors colors-to-find)
          (into result colors-to-find)
          )))))