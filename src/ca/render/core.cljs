(ns ca.render.core
  (:require [ca.core :as logic]))

(defn getelt-byid [id] (-> js/document (.getElementById id)))
(defn set-innerhtml [id content] (set! (.-innerHTML (getelt-byid id)) content))
(defn get-root [] (getelt-byid "root"))
(defn set-root [content] (set! (.-innerHTML (get-root)) content))

; initialize the universe
(def state (atom nil))
(defn init [width height initfn] (reset! state (logic/create-universe width height initfn)))

(defn svg-element
  "Create an SVG element"
  [document tagname content attributes]
  (let [e (.createElementNS document "http://www.w3.org/2000/svg" tagname)]
    (dorun (map #(.setAttribute e (name (key %)) (val %)) attributes) )
    (when content (.append e content))
    e))

(defn render-setup [elt radius]
  (set! (.-width elt) (* (* radius 1.5) (count (first @state)))))

(defn render []
  (let [r 2 svg (getelt-byid "vis")]
   (doall (for [row (range (count @state)) column (range (count (first @state)))]
            (.append svg
              (svg-element js/document "circle" nil
                {:cx (* (inc column) r 2)
                 :cy (* (inc row) r 2.5)
                 :r r
                 :fill (if (logic/alive-at? @state row column) "grey" "white")}))))))
