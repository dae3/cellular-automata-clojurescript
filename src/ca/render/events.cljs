(ns ca.render.events
  (:require [ca.core :as c]
            [ca.render.core :as rc]))

(def running (atom false))
(defn start-stop [] (swap! running not))
(defn step []
  (rc/set-innerhtml "ri" (if @running "Running" "Stopped"))
  (when @running
    ((swap! rc/state c/generation-recur)(js/setTimeout step 500))))

(defn ^:dev/before-load unwatch-running [] remove-watch running :start-stop)
(add-watch running :start-stop step)

(defn ^:dev/before-load unevent []
  (-> (rc/getelt-byid "btnone") (.removeEventListener "click" start-stop)))
(-> (rc/getelt-byid "btnone") (.addEventListener "click" start-stop))

(defn ^:dev/before-load unwatch [] remove-watch rc/state :foo)
(add-watch rc/state :foo rc/render)

(rc/init 100 100 #(> 1 (rand-int 2)))
