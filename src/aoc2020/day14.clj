(ns aoc2020.day14
  (:require [clojure.string :as str]))

(defn apply-mask-bit
  [m-char v-char]
  (if (= \X m-char)
    v-char
    m-char))

(defn binary-string
  [n length]
  (let [nb (seq (Long/toBinaryString n))
        nb-size (count nb)]
    (into nb (repeat (- length nb-size) \0))))

(defn apply-mask-to-value
  [mask binary-value]
  (let [value-with-mask (for [i (range 0 (count mask))] (apply-mask-bit (nth mask i) (nth binary-value i)))
        value-with-mask (apply str value-with-mask)
        value-with-mask (Long/parseLong value-with-mask 2)]
    value-with-mask))

(defn write-memory
  [memory address value]
  (let [mask (:mask memory)
        m-size (count mask)
        b-value (binary-string value m-size)
        value-with-mask (apply-mask-to-value mask b-value)]
    (assoc memory address value-with-mask))
  )

(defn apply-mask-bit2
  [m-char v-char]
  (if (= \0 m-char)
    v-char
    m-char))

(defn str-replace-at-index
  [s index value]
  (apply str (assoc (vec s) index value)))

(defn floating-combinations
  ;floating-bits = X1101X -> 011010, 011011, 111010, 111011
  [floating-bits index]
  (let [size (count floating-bits)]
    (if (>= index size)
      [floating-bits]
      (if (= \X (nth floating-bits index))
        (vec (concat
               (floating-combinations (str-replace-at-index floating-bits index "0") (inc index))
               (floating-combinations (str-replace-at-index floating-bits index "1") (inc index))
               ))
        (floating-combinations floating-bits (inc index))
        ))))

(defn apply-mask-to-address
  [mask binary-address]
  (let [address-with-mask (for [i (range 0 (count mask))] (apply-mask-bit2 (nth mask i) (nth binary-address i)))
        address-with-mask (apply str address-with-mask)
        addresses (floating-combinations address-with-mask 0)
        addresses (map #(Long/parseLong % 2) addresses)]
    addresses))

(defn write-memory2
  [memory address value]
  (let [mask (:mask memory)
        m-size (count mask)
        b-address (binary-string address m-size)
        addresses (apply-mask-to-address mask b-address)]
    (reduce (fn [mem addr] (assoc mem addr value)) memory addresses)
    )
  )

(defn parse-command
  [line]
  (cond
    (str/starts-with? line "mask") (let [[_ mask] (re-find #"mask = ([X01]+)" line)]
                                     [:mask mask])
    (str/starts-with? line "mem") (let [[_ address value] (re-find #"mem\[(\d+)\] = (\d+)" line)]
                                    [:memory (Long/parseLong address) (Long/parseLong value)]
                                    )
    ))

(defn apply-line
  [line memory write-fn]
  (let [[opcode & args] (parse-command line)]
    (condp = opcode
      :mask (assoc memory :mask (first args))
      :memory (write-fn memory (first args) (second args))
      )))

(defn sum-of-memory
  [memory]
  (reduce + (vals (dissoc memory :mask))))

(defn solve
  [input write-fn]
  (let [lines (str/split-lines input)
        initial-memory {}
        final-memory (reduce
                       (fn [memory line] (apply-line line memory write-fn))
                       initial-memory
                       lines)]
    (sum-of-memory final-memory)))

(defn solve-1
  [input]
  (solve input write-memory))

(defn solve-2
  [input]
  (solve input write-memory2))