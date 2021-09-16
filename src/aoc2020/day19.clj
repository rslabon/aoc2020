(ns aoc2020.day19
  (:require [clojure.string :as str]))

(defn parse-rule-def-line
  [line]
  (let [single-char (re-matches #"(\d+): \"(\w)\"" line)
        nested-rules (re-matches #"(\d+): ((\s*\d+)+)(\s\|((\s\d+)+))?" line)]
    (cond (not= nil single-char) (let [[_ idx char] single-char
                                       idx (read-string idx)
                                       char (str/replace char "\"" "")
                                       [char] char]
                                   {idx char})
          (not= nil nested-rules) (let [[_ idx rules _ _ sub-rules] nested-rules
                                        idx (read-string idx)
                                        rules (str/split rules #" ")
                                        rules (mapv read-string rules)
                                        sub-rules (if (nil? sub-rules)
                                                    []
                                                    (mapv read-string (-> sub-rules
                                                                          (str/trim)
                                                                          (str/split #" ")
                                                                          )))]
                                    {idx (remove empty? [rules sub-rules])}))))

(defn parse-rule-def
  [input]
  (let [lines (str/split-lines input)]
    (reduce (fn [acc line] (merge acc (parse-rule-def-line line))) {} lines)
    ))

(defn gen
  ([rules-def rules] (filter not-empty (gen rules-def rules "")))
  ([rules-def rules text]
   (if (and (not-empty rules) (coll? (first rules)))
     (flatten (map #(gen rules-def % text) rules))
     (reduce (fn [acc val]
               (let [rule-body (get rules-def val)]
                 (if (char? rule-body)
                   (map #(str % rule-body) (flatten [acc]))
                   (gen rules-def rule-body acc)))
               ) text rules))))