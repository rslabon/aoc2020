(ns aoc2020.day9)

(defn sum? [numbers sum]
  (let [invalid-number (/ sum 2) ; sum cannot be made from the same numbers
        complement-numbers (map #(- sum %) numbers)
        to-find-numbers (remove #(= invalid-number %) complement-numbers)]
    (boolean (some #(contains? (set numbers) %) to-find-numbers))))

(defn xmas
  [numbers window]
  (loop [offset 0]
    (if (>= (+ offset window) (count numbers))
      nil
      (let [preamble (subvec numbers offset (+ offset window))
            number (nth numbers (+ offset window))]
        (if (sum? preamble number)
          (recur (inc offset))
          number)))))

(defn xmas-2
  [numbers sum-find]
  (loop [start 0
         end 1]
    (let [sum (reduce + (subvec numbers start end))]
      (cond
        (= sum sum-find) (let [cont-set (subvec numbers start end)
                               min (apply min cont-set)
                               max (apply max cont-set)]
                           (+ min max))
        (< sum sum-find) (recur start (+ 1 end))
        (> sum sum-find) (recur (+ 1 start) (+ 2 start)))
      )))