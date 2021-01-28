(ns aoc2020.day16
  (:require [clojure.string :as str]))

(defn parse-line
  [line]
  (let [[_ key a0 a1 b0 b1] (re-find #"([\s\w]+): (\d+)-(\d+) or (\d+)-(\d+)" line)
        ranges [[a0 a1] [b0 b1]]
        ranges (mapv (fn [[x y]] [(Long/parseLong x) (Long/parseLong y)]) ranges)]
    {key ranges}))

(defn parse-rules
  [input]
  (let [lines (str/split-lines input)
        parsed-lines (mapv parse-line lines)
        parsed-lines (reduce merge parsed-lines)]
    parsed-lines
    ))

(defn in-range?
  [n ranges]
  (boolean (some (fn [[x y]] (and (>= n x) (<= n y))) ranges)))

(defn find-invalid
  [ticket ranges]
  (vec (filter #(not (in-range? % ranges)) ticket)))

(defn parse-ticket
  [line]
  (let [ticket (str/split line #",")
        ticket (mapv #(Long/parseLong %) ticket)]
    ticket))

(defn parse-tickets
  [input]
  (let [[_ & lines] (str/split-lines input)
        tickets (mapv parse-ticket lines)]
    tickets
    ))

(defn ranges-from-rules
  [rules]
  (reduce into [] (mapv (fn [[_ ranges]] ranges) rules)))

(defn solve-1
  [input]
  (let [[rules-text _ nearby-tickets-text] (str/split input #"\n\n")
        rules (parse-rules rules-text)
        ranges (ranges-from-rules rules)
        nearby-tickets (parse-tickets nearby-tickets-text)]
    (reduce + (flatten (mapv #(find-invalid % ranges) nearby-tickets)))
    ))

(defn match-rules
  [rules numbers]
  (let [found (filter (fn [[_ ranges]]
                        (every? #(in-range? % ranges) numbers)
                        ) rules)]
    (mapv first found)))

(defn is-valid-ticket?
  [ranges ticket]
  (empty? (find-invalid ticket ranges))
  )

(defn get-column
  [matrix column]
  (mapv (fn [ticket] (nth ticket column)) matrix)
  )

(defn find-possibilities
  [rules valid-columns]
  (loop [current-valid-columns valid-columns
         result []]
    (if (empty? current-valid-columns)
      result
      (let [valid-column (first current-valid-columns)
            possible-rules (match-rules rules valid-column)]
        (recur (rest current-valid-columns) (conj result possible-rules)))
      )))

(defn drop-from-possibilities
  [value possibilities]
  (mapv (fn [group] (filter #(not= value %) group)) possibilities))

(defn indexed-at-front
  [array]
  (map-indexed (fn [idx item] (conj item idx)) array))

(defn choose-best
  [possibilities]
  (let [possibilities (indexed-at-front possibilities)]
    (loop [current-possibilities (sort-by count possibilities)
           result []]
      (if (empty? current-possibilities)
        (mapv second (sort-by first result))
        (let [group (first current-possibilities)
              value (first group)
              index (last group)
              new-possibilities (sort-by count (drop-from-possibilities value (rest current-possibilities)))]
          (recur
            new-possibilities
            (conj result [index value]))
          )))))

(defn valid-tickets
  [tickets rules]
  (filterv #(is-valid-ticket? (ranges-from-rules rules) %) tickets))

(defn take-part
  [ticket part-text]
  (filter (fn [[key _]] (str/includes? key part-text)) ticket))

(defn transpose-matrix
  [matrix]
  (let [number-of-columns (count (first matrix))]
    (for [i (range 0 number-of-columns)] (get-column matrix i))))

(defn solve-2
  [input]
  (let [[rules-text your-ticket-text nearby-tickets-text] (str/split input #"\n\n")
        rules (parse-rules rules-text)
        [your-ticket] (parse-tickets your-ticket-text)
        nearby-tickets (parse-tickets nearby-tickets-text)
        valid-tickets (valid-tickets nearby-tickets rules)
        valid-columns (transpose-matrix valid-tickets)
        possibilities (find-possibilities rules valid-columns)
        rule-names (choose-best possibilities)
        complete-ticket (zipmap rule-names your-ticket)
        departure-parts (take-part complete-ticket "departure")]
    (reduce * (vals departure-parts))
    ))