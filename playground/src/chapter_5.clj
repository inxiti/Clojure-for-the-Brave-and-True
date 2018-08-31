;;; clojure for the brave and true
;;;
;;; chapter 5

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

;; exercise 5.1
(defn attr
  "Returns the `attribute` from the character sheet."
  [attribute]
  (partial (comp attribute :attributes)))

;; ((attr :dexterity) character) ;; => 5

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

;; exercises
;;

;; look up and use the update-in function
(def p {:name "James" :age 26 :address {:state "WA"}})

(defn my-comp
  "My implementation of the comp function."
  [& fs]
  (fn [& args]
    (reduce (fn [acc n] (n acc))   ;; apply fns to the accumulator until done
            (apply (last fs) args) ;; initial value, applying last f to argument
            (rest (reverse fs))))) ;; reverse the fns, effectively dropping last

;; hint: use [m [k & ks] v]
(defn my-assoc-in
  "My implementation of the assoc-in function."
  [m [k & ks] v]
  (if ks
    (assoc m k (my-assoc-in (k m) ks v)) ;; recursively assoc keywords
    (assoc m k v))) ;; just assoc

;; example of assoc-in being used
(assoc-in p [:name] "Jameson") ;; {:name "Jameson", :age 26}

;; + is the f, 4 is the first of & args
(update-in p [:age] + 4) ;; {:name "James" :age 30}

;; reimplement update-in
(defn my-update-in
  "My implementation of the update-in function."
  [m [k & ks] f & args]
  (if ks
    (assoc m k (apply my-update-in (k m) ks f args)) ;; traverse maps, then f it
    (assoc m k (apply f (k m) args)))) ;; apply f to keyworded value 

;; -----------------------------------------------------------------------------
;; main
;;

(defn -main
  []
  (println "hello, world"))

