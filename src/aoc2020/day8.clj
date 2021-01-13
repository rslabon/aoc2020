(ns aoc2020.day8
  (:require [clojure.string :as str]))

(defn nop [state argument] (update state :pc inc))
(defn acc [state argument] {:acc (+ argument (state :acc)) :pc (inc (state :pc))})
(defn jmp [state argument] (update state :pc + argument))
(def env {:nop nop :acc acc :jmp jmp})

(defn execute [instructions]
  (loop [state {:acc 0 :pc 0}
         pc-history []]
    (if (or (>= (state :pc) (count instructions)) (.contains pc-history (state :pc)))
      (assoc state :loop (.contains pc-history (state :pc)))
      (let [[operation argument] (nth instructions (state :pc))
            new-state ((env operation) state argument)]
        (recur new-state (conj pc-history (state :pc)))
        ))))

(defn parse-instructions
  [code]
  (let [lines (map #(str/split % #" ") (str/split code #"\n"))]
    (map
      (fn [[operation argument]] [(keyword operation) (read-string argument)])
      lines)))

(defn find-mutation-next-index
  [start-index instructions]
  (loop [index start-index]
    (if (>= index (count instructions))
      -1
      (let [[operation] (nth instructions index)]
        (if (or (= operation :nop) (= operation :jmp))
          index
          (recur (inc index)))))
    ))

(defn mutate-operation
  [[operation argument]]
  (cond
    (= :nop operation) [:jmp argument]
    (= :jmp operation) [:nop argument]
    :else [operation argument]))

(defn mutate-instructions
  [index instructions]
  (let [next-index (find-mutation-next-index index instructions)]
    (if (>= next-index 0)
      {:instructions (update instructions next-index mutate-operation) :idx (inc next-index)}
      {:instructions instructions :idx next-index}
      )))

(defn handheld-halting
  [input]
  (let [code (parse-instructions input)
        state (execute code)]
    (state :acc)))

(defn handheld-halting-fix
  [input]
  (let [instructions (vec (parse-instructions input))]
    (loop [state (execute instructions)
           mutation-index 0]
      (if (and (>= mutation-index 0) (state :loop))
        (let [mutation (mutate-instructions mutation-index instructions)]
          (recur
            (execute (mutation :instructions))
            (mutation :idx)))
        (state :acc)
        ))))