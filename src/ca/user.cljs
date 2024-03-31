(ns ca.user
  (:require [ca.render.events :as cee]
            [ca.render.core :as cec]
            [ca.core :as cc]
             [cljs.pprint :as pp :refer [pprint]]))

(defn fifty-fifty [] (> 1 (rand-int 2)))
(def tiny-u (cc/create-universe 3 3 fifty-fifty))
(def medium-u (cc/create-universe 10 10 fifty-fifty))
(def large-u (cc/create-universe 100 100 fifty-fifty))
(def a (atom nil))

;(cee/start-stop)
;(pprint @cec/state)
;(cc/generation tiny-u)
;(pprint @a)
;(reset! a tiny-u)
(reset! a medium-u)
(swap! a cc/generation)
(count (filter identity (:data @a)))

;(map identity (transient (:data medium-u)))
;(loop [d (transient (:data medium-u))]
;  (when (first d)
;    (println "a")
;    (recur (rest d))))


(simple-benchmark [u (atom (cc/create-universe 100 100 fifty-fifty))]
                  (swap! u cc/generation)
                  10)



(pprint tiny-u)
(cc/generation-recur tiny-u)
(cc/generation-recur medium-u)
(cc/generation-recur large-u)
(cc/alive-conway-neighbour-count tiny-u 2 2)
(cc/conwayneighbour-list tiny-u 2 2)

(for [row (range (count tiny-u)) column (range (count (first tiny-u)))]
  (cc/alive-conway-neighbour-count tiny-u row column))

(for [row (range (count tiny-u)) column (range (count (first tiny-u)))]
 [row column])

(let [maxrow (dec (count tiny-u)) maxcolumn (dec (count (first tiny-u)))]
  (loop [row 0 column 0 u tiny-u]
   (cond
     (and (<= row maxrow) (< column maxcolumn)) (recur row (inc column) (assoc-in u [row column] (+ row column)))
     (= column maxcolumn) (recur (inc row) 0 (assoc-in u [row column] (+ row column)))
     :else u)))


(assoc-in tiny-u [0 0] nil)
(assoc-in tiny-u [0 2] nil)

(simple-benchmark [u (cc/create-universe 100 100 fifty-fifty)]
                  (cc/generation u)
                  10)
