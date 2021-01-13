(ns aoc2020.day7-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.day7 :refer :all]))

(let [input "light red bags contain 1 bright white bag, 2 muted yellow bags.\ndark orange bags contain 3 bright white bags, 4 muted yellow bags.\nbright white bags contain 1 shiny gold bag.\nmuted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\nshiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\ndark olive bags contain 3 faded blue bags, 4 dotted black bags.\nvibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\nfaded blue bags contain no other bags.\ndotted black bags contain no other bags."]
  (deftest handy-haversacks-test
    (testing "to-bag-spec"
      (is (= {"dotted black" []} (to-bag-spec "dotted black bags contain no other bags")))
      (is (= {"light red" ["bright white" "muted yellow"]} (to-bag-spec "light red bags contain 1 bright white bag, 2 muted yellow bags.")))
      (is (= {"bright white" ["shiny gold"]} (to-bag-spec "bright white bags contain 1 shiny gold bag.")))
      (is (= {"dark orange" ["bright white" "muted yellow"]} (to-bag-spec "dark orange bags contain 3 bright white bags, 4 muted yellow bags.")))
      (is (= {
              "muted yellow" ["shiny gold" "faded blue"],
              "light red"    ["bright white" "muted yellow"],
              "dotted black" [],
              "dark orange"  ["bright white" "muted yellow"],
              "bright white" ["shiny gold"],
              "shiny gold"   ["dark olive" "vibrant plum"],
              "faded blue"   [],
              "vibrant plum" ["faded blue" "dotted black"],
              "dark olive"   ["faded blue" "dotted black"]
              }
             (to-bags-spec input)))
      )
    (testing "handy-haversacks"
      (is (= 4 (handy-haversacks input)))
      )))