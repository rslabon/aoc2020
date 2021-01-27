(ns aoc2020.day15)

(defn create-state
  [numbers]
  (let [state (map-indexed (fn [i n] {n [i i]}) numbers)
        state (reduce merge state)
        state (assoc state :last-spoken-number (last numbers))]
    state))

(defn speak-number
  [state turn]
  (let [spoken-in-turns (get state (:last-spoken-number state))
        turns-difference (reduce - spoken-in-turns)]
    (-> state
        (assoc :last-spoken-number turns-difference)
        (update turns-difference #(if (nil? %) [turn turn] [turn (first %)]))
        )))

(defn solve
  [numbers max-turn]
  (loop [turn (count numbers)
         state (create-state numbers)]
    (if (= turn max-turn)
      (:last-spoken-number state)
      (recur (inc turn) (speak-number state turn))
      )))