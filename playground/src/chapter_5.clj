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

;; referentially transparent, same result for same argument
(defn analysis
  [text]
  (str "Character count: " (count text)))

;; not referentially transparent, the contents of the file can change outside of
;; this function.)
(defn analyze-file
  [filename]
  (analysis (slurp filename)))

;; def defines a global variable
(def great-baby-name "Rosanthony")
great-baby-name ;; => "Rosanthony"

;; let defines a lexical scope variable bound within its parentheses
(let [great-baby-name "Bloodthunder"]
  great-baby-name) ;; => "Bloodthunder"

great-baby-name ;; => "Rosanthony"

(defn sum
  ([vals]
   (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     #_(sum (rest vals) (+ (first vals) accumulating-total))
     (recur (rest vals) (+ (first vals) accumulating-total)))))

(sum [0 1 2 3 4 5 15]) ;; => 30

(defn -main
  []
  (println "hello, world"))
