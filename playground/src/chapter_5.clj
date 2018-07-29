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
  "Adds all values in the sequence `vals` , and returns their sum."
  ([vals]
   (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     #_(sum (rest vals) (+ (first vals) accumulating-total))
     (recur (rest vals) (+ (first vals) accumulating-total)))))

(sum '(0 1 2 3 4 5 15)) ;; => 30

;; function composition
;;

;; comp can work with any number of functions.
((comp inc *) 2 3) ;; => 7

;; an rpg character
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character) ;; => 10
(c-str character) ;; => 4
(c-dex character) ;; => 5

;; alternative without using comp, displays the elegance of function composition
;; (fn [c] (:strength (:attributes c)))

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2)))) ;; (+ (/ 10 2) 1), then coerce to int

(spell-slots character) ;; => 6

;; using comp to make `spell-slots`, you must use an anonymous function if
;; multiple arguments are used
(def spell-slots-comp
  (comp int
        inc
        #(/ % 2)
        c-int))

(spell-slots-comp character)

;; an example of how comp works with just two functions. `two-comp` returns a
;; function that accepts any number of arguments, applies g to them, and then
;; passes the result of that to f.
(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

((two-comp inc +) 3 4) ;; => 8

;; TODO: more experience needed with reducing so it's natural feeling/thinking
;; my implementation of comp
(defn my-comp
  [& fs]
  (fn [& args]
    (reduce #(%2 %)
            (apply (last fs) args)
            (rest (reverse fs))))) ;; reverse so right to left happens in order

((my-comp (partial * 2) inc +) 1 3 5) ;; sums, inc, doubles => 20 

(defn sleep-identity
	"Returns the given value after 1 second"
	[x]
	(Thread/sleep 1000)
	x)

;; (sleep-identity "Mr. Fantastico") ;; waits 1 second => "Mr. Fantastico"
;; (sleep-identity "Mr. Fantastico") ;; waits 1 second => "Mr. Fantastico"

;; memoize/cache results for `arguments`(does not perform cache invalidation)
(def memo-sleepy-identifier (memoize sleep-identity))

;; (memo-sleepy-identifier "Mr. Fantastico") ;; waits 1 second => "Mr. Fantastico"
;; (memo-sleepy-identifier "Mr. Fantastico") ;; cached, returns immediately

;; -----------------------------------------------------------------------------
;; main
;;

(defn -main
  []
  (println "hello, world"))
