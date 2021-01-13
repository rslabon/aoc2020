(ns aoc2020.day10)

(defn difference-finder
  [numbers]
  (let [numbers (concat [0] numbers)
        x (butlast numbers)
        y (rest numbers)
        ; numbers [1 2 4 6 7 8 10]
        ; x     =   [1 2 4 6 7  8]
        ; y     =   [2 4 6 7 8 10]
        ; diff  =   [1 2 2 1 1  2]
        diffs (map - y x)]
    (*
      (count (filter #(= % 1) diffs))
      (inc (count (filter #(= % 3) diffs)))
      )
    ))

(defn find-next-indexes
  [numbers idx]
  (let [n (if (= idx -1)
            0
            (nth numbers idx))
        positive-idx (max 0 idx)
        numbers-view (subvec numbers positive-idx)
        numbers-to-find [(+ 1 n) (+ 2 n) (+ 3 n)]
        indexes (filterv #(not= % -1) (mapv #(.indexOf numbers-view %) numbers-to-find))
        ]
    (if (empty? indexes)
      indexes
      (mapv #(+ positive-idx %) (sort indexes))
      )))

(def count-arrangements
  (memoize (fn [numbers index]
             (let [next-indexes (find-next-indexes numbers index)]
               (if (empty? next-indexes)
                 1
                 (reduce + (mapv #(count-arrangements numbers %) next-indexes))
                 )))))
