(ns playground)

; This needs to be... something more or better organized.

; TODO: better understand sequences, specifically lazy sequences

(comment
  "
    So multiline comments being a form is kind of goofy, hilarious, and not too
  unexpected.

  INTERESTING:
    :namedWhatever are keywords(denoted as such by beginning with :), and are
  identifiers that evaluate to themselves(for very fast equality tests, so
  they're useful for replacing constant strings). {:key 123} is a map which maps
  the key/keyword `:key` to the value `123`. similar to {'key': 1} in javascript
  in at least usage afaik.

  TODO:
    Experiment with thread-last macro(->>)
  ")

; forms
(defn error-message
  "Creates an error message depending on severity."
  [severity]
  (str "error: OH GOD! It's "
       (if (= severity :severe) "SEVERE!" "not that bad.")))

(defn numberIncreaseBy1
  "Increase each number in a list by one."
  [l]
  (when (seq l)
    (map #(+ 1 %) l)))

; reimplementing some list manipulation functions
(defn head [[first]] first)
(defn tail [[first & remaining]] remaining)

; (defn armstrong?
;   "Checks if the given number `n` is an armstrong number."
;   [n]
;   (let [numbers (map #(Character/getNumericValue %) (str n))
;         c       (count numbers)
;         l       (map #(Math/pow % c) numbers)]
;     (== n (reduce + l))))

(defn armstrong?
  "Checks if the given number `x` is an armstrong number."
  [x]
  (let [coll (map #(Character/getNumericValue %) (str x))
        size (count coll)]
    (->> coll
         (map #(Math/pow % size))
         (reduce +)
         (== x))))

(defn f
  "Just runs a bunch of random code in order to help me experiment."
  ([param]
   (do
     (let [biggerList (numberIncreaseBy1 param)]
       (->> biggerList
            (tail)
            (numberIncreaseBy1))))
   (armstrong? 123)))

(defn test-quoting
  []
  '(+ 1 2 3 4))

(defn test-arity
  [ns]
  (apply * ns))

(defn rd
  [n]
  (reduce + (range (inc n))))

; composes a function: addition(essentially sums, convert to string via str, and reverse the
; returned string)
(def reversed-sum-str (comp clojure.string/reverse str +))

; with out composition
(defn reversed-sum-str-f
  [& ns]
  (clojure.string/reverse (str (apply + ns))))

(defn test-type-of
  [& ns]
  ns)

; brilliant

; comp and threading(-> and ->>) enable point free style/tacit programming(composition of funcitons
; without parameters)

; composes a "new function", split between lower and upper case, convert all to lower case,
; interpose - between each split word, join them all together, and finally convert the string to
; a keyword
(def camel->keyword (comp keyword
                          clojure.string/join
                          (partial interpose "-")
                          (partial map clojure.string/lower-case)
                          #(clojure.string/split % #"(?<=[a-z])(?=[A-Z])")))

(defn adder
  [n]
  (fn [x] (+ n x)))

; usage
; (def add5 (adder 5)) ; adds 5 to whatever it is called with

(defn prime?
  [n]
  (cond
    (== 2 n) true
    (or (== 1 n)
        (even? n)) false
    :else (->> (range 3 (inc (Math/sqrt n)) 2)
               (filter #(zero? (rem n %)))
               empty?)))

; memoization is built-in(a simple implementation at least). works with referentially transparent
; functions. meaning, you could replace the function/expression with a value and the result would
; be the exact same. example: passing (+ 1 1) is as referentially transparent as 2.

; (let [m-prime? (memoize prime?)]
;   (time (m-prime? 1125899906842679))  ; Elapsed time: 1108.850452 msecs
;   (time (m-prime? 1125899906842679))) ; Elapsed time: 0.014115 msecs

; begin
(def l [0 1 2 3 4 5 6 7 8 9])

; (println "Using a list defined as:" l)
; (println "Using thread-last macro to get the tail of list, increase all by 1,"
;  "and print the result.")

(defn -main
  []
  (test-arity (range 1 5)) ; 1 to 5, excludes 5
  (rd 10)
  (max 0 10 5 -15 48) ; so many functions take more arguments than expected, for example max
  (#(filter % ["a" 5 "b" 6]) string?)
  (#(filter % ["a" 5 "b" 6]) number?) ; same function literal, different predicate, resulting in
                                      ; different returned vector
  ; create a partial function mapping multiplication, and calling it with multiple lists as args
  ((partial map *) [1 2 3] [4 5 6] [7 8 9])
  (reversed-sum-str 1 5 10 15 20)
  (reversed-sum-str-f 1 5 10 15 20 25)
  (test-type-of 1 2 3)
  (eval (quote '(+ 1 2 3))))
