(ns aoc2020.day18
  (:require [clojure.string :as str]))

(defn parse-expr
  [input]
  (let [line (str/replace input "(" "[")
        line (str/replace line ")" "]")
        line (str "[" line "]")
        line (read-string line)]
    line))

(defn precedence
  ([expressions] (precedence expressions true))
  ([expressions resolve]
   (cond
     (number? expressions) [expressions]
     (symbol? expressions) [expressions]
     (empty? expressions) []
     (< (count expressions) 3) expressions
     :else (let [first (nth expressions 0)
                 first (if (and resolve (coll? first)) (precedence first) first)
                 second (nth expressions 1)
                 third (nth expressions 2)
                 third (if (coll? third) (precedence third) third)]
             (vec (cond
                    (= second '+) (precedence (concat [[first second third]] (drop 3 expressions)) false)
                    (= second '*) (concat [first second] (precedence (conj (drop 3 expressions) third) false))
                    ))))))

(defn apply-operation
  [operation first-operand second-operand]
  (let [operation (if (fn? operation) operation @(resolve (symbol operation)))]
    (operation first-operand second-operand)))

(defn calc [expression]
  (cond
    (number? expression) expression
    (empty? expression) nil
    (= (count expression) 1) (calc (first expression))
    :else (let [expression (vec expression)
                first-operand (calc (nth expression 0))
                operation (nth expression 1)
                second-operand (calc (nth expression 2))
                expression (drop 3 expression)
                result (apply-operation operation first-operand second-operand)
                expression (conj expression result)]
            (recur expression))))

