(ns aoc2020.day17
  (:require [clojure.string :as str]))

(defn get-grid-3d
  [grid-3d x y z] (get grid-3d [x y z] :inactive))

(defn get-grid-4d
  [grid-4d x y z w] (get grid-4d [x y z w] :inactive))

(defn neighbors
  ([x y z]
   (filter (fn [[dx dy dz]] (not= [x y z] [dx dy dz]))
           (for [dx (range -1 2 1) dy (range -1 2 1) dz (range -1 2 1)] [(+ x dx) (+ y dy) (+ z dz)]))))

(defn next-cube-state
  [state neighbors-state]
  (let [active-count (count (filter #(= :active %) neighbors-state))]
    (cond
      (and (= :active state) (or (= 2 active-count) (= 3 active-count))) :active
      (and (= :inactive state) (= active-count 3)) :active
      :else :inactive
      )
    ))

(defn parse-line
  [x z line]
  (let [parsed-line (map-indexed (fn [y val] [[x y z] (condp = val "#" :active "." :inactive)]) (str/split line #""))
        only-active-parsed-line (filter (fn [[_ state]] (= state :active)) parsed-line)
        result (reduce (fn [acc [coords state]] (assoc acc coords state)) {} only-active-parsed-line)]
    result
    ))

(defn parse
  [input]
  (let [lines (str/split input #"\n")]
    (apply merge (map-indexed (fn [x val] (parse-line x 0 val)) lines))
    ))

(defn min-max
  [numbers]
  (if (empty? numbers)
    [0 0]
    [(apply min numbers) (apply max numbers)]))

(defn boundaries
  [coords]
  (let [xs (map #(nth % 0) coords)
        ys (map #(nth % 1) coords)
        zs (map #(nth % 2) coords)]
    {:x (min-max xs), :y (min-max ys), :z (min-max zs)}
    ))

(defn count-active
  [grid-3d]
  (count (filter #(= % :active) (vals grid-3d))))

(defn next-state
  [grid-3d x y z]
  (let [state (get-grid-3d grid-3d x y z)
        the-neighbors (neighbors x y z)
        the-neighbors-states (map (fn [[x y z]] (get-grid-3d grid-3d x y z)) the-neighbors)
        new-state (next-cube-state state the-neighbors-states)]
    [[x y z] new-state]))

(defn next-grid
  [grid-3d]
  (let [{[x-min x-max] :x, [y-min y-max] :y, [z-min z-max] :z} (boundaries (keys grid-3d))
        coords (for [xs (range (- x-min 1) (+ x-max 2))
                     ys (range (- y-min 1) (+ y-max 2))
                     zs (range (- z-min 1) (+ z-max 2))] [xs ys zs])
        new-grid (map (fn [[x y z]] (next-state grid-3d x y z)) coords)
        new-grid (reduce (fn [acc [coords state]] (assoc acc coords state)) grid-3d new-grid)]
    new-grid
    ))

(defn solve-1
  ([grid] (solve-1 grid 0))
  ([grid iter] (if (= iter 6)
                 grid
                 (recur (next-grid grid) (inc iter))
                 ))
  )

(defn parse-line-2
  [x z w line]
  (let [parsed-line (map-indexed (fn [y val] [[x y z w] (condp = val "#" :active "." :inactive)]) (str/split line #""))
        only-active-parsed-line (filter (fn [[_ state]] (= state :active)) parsed-line)
        result (reduce (fn [acc [coords state]] (assoc acc coords state)) {} only-active-parsed-line)]
    result
    ))

(defn parse-2
  [input]
  (let [lines (str/split input #"\n")]
    (apply merge (map-indexed (fn [x val] (parse-line-2 x 0 0 val)) lines))
    ))

(defn neighbors-2
  ([x y z w]
   (filter (fn [[dx dy dz dw]] (not= [x y z w] [dx dy dz dw]))
           (for [dx (range -1 2 1) dy (range -1 2 1) dz (range -1 2 1) dw (range -1 2 1)]
             [(+ x dx) (+ y dy) (+ z dz) (+ w dw)]))))

(defn next-state-2
  [grid-4d x y z w]
  (let [state (get-grid-4d grid-4d x y z w)
        the-neighbors (neighbors-2 x y z w)
        the-neighbors-states (map (fn [[x y z w]] (get-grid-4d grid-4d x y z w)) the-neighbors)
        new-state (next-cube-state state the-neighbors-states)]
    [[x y z w] new-state]))

(defn boundaries-2
  [coords]
  (let [xs (map #(nth % 0) coords)
        ys (map #(nth % 1) coords)
        zs (map #(nth % 2) coords)
        ws (map #(nth % 3) coords)]
    {:x (min-max xs), :y (min-max ys), :z (min-max zs), :w (min-max ws)}
    ))

(defn next-grid-2
  [grid-4d]
  (let [{[x-min x-max] :x, [y-min y-max] :y, [z-min z-max] :z, [w-min w-max] :w} (boundaries-2 (keys grid-4d))
        coords (for [xs (range (- x-min 1) (+ x-max 2))
                     ys (range (- y-min 1) (+ y-max 2))
                     zs (range (- z-min 1) (+ z-max 2))
                     ws (range (- w-min 1) (+ w-max 2))] [xs ys zs ws])
        new-grid (map (fn [[x y z w]] (next-state-2 grid-4d x y z w)) coords)
        new-grid (reduce (fn [acc [coords state]] (assoc acc coords state)) grid-4d new-grid)]
    new-grid
    ))

(defn solve-2
  ([grid] (solve-2 grid 0))
  ([grid iter] (if (= iter 6)
                 grid
                 (recur (next-grid-2 grid) (inc iter))
                 ))
  )
