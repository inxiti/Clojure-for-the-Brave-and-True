(ns chapter-6)

; showing that symbols map to functions, variables, anything really. asdasdasd;lkasjdf;kljasdajsd
; asdf
inc ; => function
'inc ; => inc
(map inc [1 2 3]) ; => [2 3 4]
'(map inc [1 2 3]) ; => (map inc [1 2 3])

(defn -main
  []
(println "Hello, world"))

(-main)
