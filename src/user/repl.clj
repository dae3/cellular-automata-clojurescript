(ns user.repl)
(require '[shadow.cljs.devtools.api :as shadow])

; setup workspace forms
(defn startup []
  (shadow/watch :frontend)
  (shadow/repl :frontend))

; GO!
(startup)
