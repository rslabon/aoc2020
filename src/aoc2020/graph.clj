(ns aoc2020.graph)

(defn has-node
  [graph value]
  (contains? (graph :nodes) (keyword value)))

(defn get-node
  [graph value]
  (if (has-node graph value)
    ((graph :nodes) (keyword value))
    {:value value :adj #{}}
    )
  )

(defn node-as-map
  [node]
  {(keyword (node :value)) node}
  )

(defn add-edge [graph from to]
  (let [to-node (get-node graph to)
        from-node (get-node graph from)
        from-node (update-in from-node [:adj] #(conj % to))
        new-nodes [from-node to-node]
        nodes-map (apply merge
                         (graph :nodes)
                         (map node-as-map new-nodes))
        new-graph {:nodes nodes-map}
        _ (println new-graph)]
    new-graph))

(defn has-edge
  [graph from to]
  (let [from-node (graph (keyword from))]
    (if (nil? from-node)
      false
      (contains? (from-node :adj) to))))


