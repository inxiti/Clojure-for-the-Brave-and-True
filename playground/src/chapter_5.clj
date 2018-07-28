(ns chapter-5)

;; example of a referentially transparent function, returns same value for same
;; args.
(+ 1 2) ;; => 3

(defn wisdom
  [words]
  (str words ", Daniel-san"))

(wisdom "Always bathe on Fridays")

;; different return despite same args, therefore not referentially transparent.
(defn year-end-evaluation
  []
  (if (> (rand) 0.5)
    "You get a raise!"
    "Better luck next year!"))

;; not referentially transparent, the contents of the file can change outside of
;; this function.)
(defn analyze-file
  [filename]
  (analysis (slurp filename)))

;; referentially transparent, same result for same argument
(defn analysis
  [text]
  (str "Character count: " (count text)))

(defn -main
  []
  (println "hello, world"))
