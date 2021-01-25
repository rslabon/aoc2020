(ns aoc2020.day14
  (:require [clojure.string :as str]))

(defn apply-mask-bit
  [m-char v-char]
  (if (= \X m-char)
    v-char
    m-char))

(defn write-mem
  [memory address value]
  (let [mask (:mask memory)
        b-value (seq (Long/toBinaryString value))
        v-size (count b-value)
        m-size (count mask)
        b-value (into b-value (repeat (- m-size v-size) \0))
        value-with-mask (for [i (range 0 m-size)] (apply-mask-bit (nth mask i) (nth b-value i)))
        value-with-mask (apply str value-with-mask)
        value-with-mask (Long/parseLong value-with-mask 2)]
    (assoc memory address value-with-mask))
  )

(defn apply-mask-bit2
  [m-char v-char]
  (if (= \0 m-char)
    v-char
    m-char))

(defn floating-comb
  ;value = X1101X -> 011010, 011011, 111010, 111011
  [value index]
  (let [size (count value)]
    (if (>= index size)
      [value]
      (if (= \X (nth value index))
        (vec (concat
               (floating-comb (apply str (assoc (vec value) index "0")) (inc index))
               (floating-comb (apply str (assoc (vec value) index "1")) (inc index))
               ))
        (floating-comb value (inc index))
        ))))

(defn write-mem2
  [memory address value]
  (let [mask (:mask memory)
        b-address (seq (Long/toBinaryString address))
        a-size (count b-address)
        m-size (count mask)
        b-address (into b-address (repeat (- m-size a-size) \0))
        address-with-mask (for [i (range 0 m-size)] (apply-mask-bit2 (nth mask i) (nth b-address i)))
        address-with-mask (apply str address-with-mask)
        generated-addresses (floating-comb address-with-mask 0)
        generated-addresses (map #(Long/parseLong % 2) generated-addresses)]
    (reduce (fn [mem addr] (assoc mem addr value)) memory generated-addresses)
    )
  )

(defn apply-line
  [line memory]
  (cond
    (str/starts-with? line "mask") (let [[_ mask] (re-find #"mask = ([X01]+)" line)]
                                     (assoc memory :mask mask))
    (str/starts-with? line "mem") (let [[_ address value] (re-find #"mem\[(\d+)\] = (\d+)" line)]
                                    (write-mem memory (Long/parseLong address) (Long/parseLong value))
                                    )
    ))

(defn solve-1
  [input]
  (loop [memory {}
         lines (str/split-lines input)]
    (if (empty? lines)
      (reduce + (vals (dissoc memory :mask)))
      (recur (apply-line (first lines) memory) (rest lines))
      )))

(defn apply-line2
  [line memory]
  (cond
    (str/starts-with? line "mask") (let [[_ mask] (re-find #"mask = ([X01]+)" line)]
                                     (assoc memory :mask mask))
    (str/starts-with? line "mem") (let [[_ address value] (re-find #"mem\[(\d+)\] = (\d+)" line)]
                                    (write-mem2 memory (Long/parseLong address) (Long/parseLong value))
                                    )
    ))

(defn solve-2
  [input]
  (loop [memory {}
         lines (str/split-lines input)]
    (if (empty? lines)
      (reduce + (vals (dissoc memory :mask)))
      (recur (apply-line2 (first lines) memory) (rest lines))
      )))