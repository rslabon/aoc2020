(ns aoc2020.day11
  (:require [clojure.string :as str]))

(def grid-value {"." :floor "L" :empty-seat "#" :occupied-seat})

(defn parse-grid
  [input]
  (let [lines (str/split input #"\n")
        grid-of-chars (mapv #(str/split % #"") lines)]
    (mapv #(mapv grid-value %) grid-of-chars)))

(defn grid-cell
  [grid row-idx col-idx]
  (nth (nth grid row-idx) col-idx))

(defn grid-width
  [grid]
  (count (nth grid 0)))

(defn grid-height
  [grid]
  (count grid))

(defn cells
  [f-row f-col grid row-idx col-idx]
  (let [width (grid-width grid)
        height (grid-height grid)]
    (loop [r (f-row row-idx)
           c (f-col col-idx)
           cells []]
      (if (or (>= r height) (>= c width) (< r 0) (< c 0))
        cells
        (recur (f-row r) (f-col c) (conj cells (grid-cell grid r c)))
        ))))

(defn first-visible
  [cells]
  (first (filter #(not= % :floor) cells))
  )

(defn adj
  [grid f row-idx col-idx]
  (let [right (f (cells identity inc grid row-idx col-idx))
        left (f (cells identity dec grid row-idx col-idx))
        down (f (cells inc identity grid row-idx col-idx))
        up (f (cells dec identity grid row-idx col-idx))
        down-right (f (cells inc inc grid row-idx col-idx))
        down-left (f (cells inc dec grid row-idx col-idx))
        up-right (f (cells dec inc grid row-idx col-idx))
        up-left (f (cells dec dec grid row-idx col-idx))]
    (filterv #(not= % nil) [right left down up down-right down-left up-right up-left])))

(defn adj-first
  [grid row-idx col-idx]
  (adj grid first row-idx col-idx))

(defn adj-first-visible
  [grid row-idx col-idx]
  (adj grid first-visible row-idx col-idx))

(defn rules
  [cell adj-cells]
  (condp = cell
    :floor cell
    :empty-seat (if (not-any? #(= % :occupied-seat) adj-cells)
                  :occupied-seat
                  cell)
    :occupied-seat (if (>= (count (filter #(= % :occupied-seat) adj-cells)) 4)
                     :empty-seat
                     cell)
    )
  )

(defn rules2
  [cell adj-cells]
  (condp = cell
    :floor cell
    :empty-seat (if (not-any? #(= % :occupied-seat) adj-cells)
                  :occupied-seat
                  cell)
    :occupied-seat (if (>= (count (filter #(= % :occupied-seat) adj-cells)) 5)
                     :empty-seat
                     cell)
    )
  )

(defn grid-apply
  [grid rules-fn adj-fn]
  (vec (map-indexed (fn [row-idx row]
                      (vec (map-indexed (fn [col-idx cell]
                                          (rules-fn cell (adj-fn grid row-idx col-idx))
                                          ) row))) grid)))

(defn grid-occurrences
  [grid value]
  (count (filter #(= value %) (flatten grid))))

(defn seating-system
  [input rules-fn adj-fn]
  (grid-occurrences
    (loop [prev-grid nil
           curr-grid (parse-grid input)]
      (if (= curr-grid prev-grid)
        curr-grid
        (recur curr-grid (grid-apply curr-grid rules-fn adj-fn)))
      )
    :occupied-seat
    ))