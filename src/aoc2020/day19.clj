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

(defn repeat-regex-parts
  "Return regex as \"((a{1}b{1})|(a{2}b{2})|...|(a{n}b{n}))\""
  [a b n]
  (str "(" (reduce (fn [acc val] (str acc "|" "((" a "){" val "}" "(" b "){" val "})")) (str "((" a "){1}" "(" b "){1})") (range 2 n)) ")")
  )

(defn to-regex
  ([rules-def] (str "^" (to-regex rules-def [0]) "$"))
  ([rules-def rules]
   (if (empty? rules)
     ""
     (let [rule (first rules)
           rule-body (get rules-def rule)
           rule-body (if (= rule 8) [[42]] rule-body)
           rule-body (if (= rule 11) [[]] rule-body)
           rule-regex (if (char? rule-body) (str rule-body)
                                            (if (coll? (first rule-body))
                                              (let [sub-rules (first rule-body)
                                                    sub-rules2 (second rule-body)
                                                    r1 (to-regex rules-def sub-rules)
                                                    r2 (to-regex rules-def sub-rules2)
                                                    r (if (empty? r2) r1 (str "(" r1 "|" r2 ")"))]
                                                r)
                                              (reduce (fn [acc other-rule] (str acc (to-regex rules-def [other-rule]))) "" rule-body)
                                              )
                                            )
           rule-regex (cond
                        (= rule 8) (str "(" rule-regex ")+")
                        (= rule 11) (let [rule42 (to-regex rules-def [42])
                                          rule31 (to-regex rules-def [31])]
                                      (repeat-regex-parts rule42 rule31 10))
                        :else rule-regex
                        )
           result (str rule-regex (to-regex rules-def (rest rules)))]
       result
       ))))