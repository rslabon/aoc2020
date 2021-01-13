(ns aoc2020.day8-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day8 :refer :all])
  (:require [clojure.java.io :as io]))



(deftest execute-test
  (is (= {:acc 0 :pc 1 :loop false} (execute [[:nop 0]])))
  (is (= {:acc 1 :pc 1 :loop false} (execute [[:acc +1]])))
  (is (= {:acc -10 :pc 1 :loop false} (execute [[:acc -10]])))
  (is (= {:acc 0 :pc 1 :loop false} (execute [[:jmp +1]])))
  (is (= {:acc 0 :pc 2 :loop false} (execute [[:nop +0] [:jmp +1]])))
  (is (= {:acc 1 :pc 3 :loop false} (execute [[:nop +0] [:jmp +1] [:acc +1]])))
  (is (= {:acc 2 :pc 4 :loop false} (execute [[:nop +0] [:jmp +2] [:acc -1] [:acc +2]]))))


(deftest parse-instructions-test
  (is (= [[:nop +0]] (parse-instructions "nop +0")))
  (is (= [[:nop +0] [:acc +10]] (parse-instructions "nop +0\nacc +10"))))


(deftest handheld-halting-test
  (is (= 5 (handheld-halting "nop +0\nacc +1\njmp +4\nacc +3\njmp -3\nacc -99\nacc +1\njmp -4\nacc +6"))))

(deftest my-puzzle
  (let [input (io/resource "day8.txt")
        code (slurp input)]
    (is (= 1859 (handheld-halting code)))))

(deftest find-mutation-next-index-test
  (is (= 0 (find-mutation-next-index 0 [[:nop 0]])))
  (is (= 1 (find-mutation-next-index 0 [[:acc +1] [:jmp +1]])))
  (is (= 1 (find-mutation-next-index 1 [[:acc +1] [:jmp +1]])))
  (is (= -1 (find-mutation-next-index 0 [[:acc +1]])))
  )

(deftest mutate-operation-test
  (is (= [:jmp +1] (mutate-operation [:nop +1])))
  (is (= [:nop +1] (mutate-operation [:jmp +1])))
  (is (= [:acc +1] (mutate-operation [:acc +1])))
  )

(deftest mutate-code-test
  (is (= {:instructions [[:jmp 0]] :idx 1} (mutate-instructions 0 [[:nop 0]])))
  (is (= {:instructions [[:acc +1] [:jmp 0]] :idx 2} (mutate-instructions 0 [[:acc +1] [:nop 0]])))
  (is (= {:instructions [[:acc +1] [:jmp 0][:jmp +1]] :idx 2} (mutate-instructions 0 [[:acc +1] [:nop 0][:jmp +1]])))
  )

;(deftest my-puzzle-2
;  (let [input (io/resource "day8.txt")
;        code (slurp input)]
;    (is (= 1235 (handheld-halting-fix code)))))
