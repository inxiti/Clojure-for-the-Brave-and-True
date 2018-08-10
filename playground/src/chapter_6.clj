;;; clojure for the brave and true
;;;
;;; chapter 6

(ns chapter-6)

;; demonstrating that symbols map to functions, variables, anything really.

inc ; => function
'inc ; => inc
(map inc [1 2 3]) ; => [2 3 4]
'(map inc [1 2 3]) ; => (map inc [1 2 3])

;; main

(defn -main
  [& args]
  (println "Hello, world"))

