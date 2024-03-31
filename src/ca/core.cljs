(ns ca.core)

(defn create-universe
  "Create a universe of the given dimensions, initializing each cell using the given func"
  [rows columns init-func]
  (vec (repeatedly rows #(vec (repeatedly columns init-func)))))

; logic
(defn conwayneighbour-list
  "Return a list of vectors of the Conway neighbours of the cell at the given coordinates"
  [universe row column]
  (let
    [maxrow (dec (count universe))
     maxcolumn (dec (count (first universe)))
     left (if (neg? (dec column)) maxcolumn (dec column))
     right (if (> (inc column) maxcolumn) 0 (inc column))
     above (if (neg? (dec row)) maxrow (dec row))
     below (if (> (inc row) maxrow) 0 (inc row))]
    (list [above left] [above column] [above right] [row left] [row right] [below left] [below column] [below right])))

(defn alive-at? [universe row column]
  (nth (nth universe row) column))

(defn aliveneighbour-count
 "The number of alive neighbours of the cell at the given coordinates"
  [neighbour-func universe row column]
  (count (filter identity
                 (map
                   #(apply alive-at? universe %)
                   (neighbour-func universe row column)))))

(defn alive-conway-neighbour-count [universe row column] (aliveneighbour-count conwayneighbour-list universe row column))

(defn cell-next-alive?
  "Return the next generation value for the cell with the given number of alive neighbours"
  [alive alive-neighbours]
  (cond
    (and (not alive) (= alive-neighbours 3)) true
    (and alive (> alive-neighbours 3)) false
    (and alive (or (= alive-neighbours 2) (= alive-neighbours 3))) true
    (and alive (< alive-neighbours 2)) false
    :else alive))

(defn generation-recur [universe]
  (let [maxrow (dec (count universe))
        maxcolumn (dec (count (first universe)))
        next-val (fn [u row column] (cell-next-alive? (alive-at? u row column) (alive-conway-neighbour-count u row column)))]
    (loop [row 0 column 0 u universe]
      (cond
        (and (<= row maxrow) (< column maxcolumn)) (recur row (inc column) (assoc-in u [row column] (next-val u row column)))
        (= column maxcolumn) (recur (inc row) 0 (assoc-in u [row column] (next-val u row column)))
        :else u))))
