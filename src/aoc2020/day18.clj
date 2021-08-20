(ns aoc2020.day18
  (:require [clojure.string :as str]))

(defn parse-expr
  [input]
  (let [line (str/replace input "(" "[")
        line (str/replace line ")" "]")
        line (str "[" line "]")
        line (read-string line)]
    line))

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

