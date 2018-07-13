(ns playground)

; experiment with figwheel/clojurescript

; to reload in repl, (use 'your.namespace :reload)
; (use 'playground :reload)

; This needs to be... something more or better organized.
; Definitely reorganize, refactor, etc

; TODO: better understand sequences, specifically lazy sequences

; sequences are sequential views over other collections
(seq "Clojure") ;= (\C \l \o \j \u \r \e)
(seq {:a 5 :b 6}) ;= ([:a 5] [:b 6])
(seq (java.util.ArrayList. (range 5))) ;= (0 1 2 3 4)
(seq (into-array ["Clojure" "Programming"])) ;= ("Clojure" "Programming")

; many functions that work on sequences call `seq` on their arguments implicitly
(map str "Clojure") ;= ("C" "l" "o" "j" "u" "r" "e")
(set "Programming") ;= #{\a \g \i \m \n \o \P \r}
(first "Clojure") ;= \C
(rest "Clojure") ;= (\l \o \j \u \r \e)

; sequences are not list
; sequences are potentially infinite, thus things like count must be evaluated by forcing it, as
; sequences are lazy
; range returns a lazy sequence that are only produced when needed, count'ing a lazy seq is one way
; to ensure that it is fully realized, since all of the seq's values must be produced in order to
; be counted
(let [s (range 1e6)]
  (time (count s))) ;= 100.422425ms

; list always track their size, or it's a constant time operation to count them
(let [s (apply list (range 1e6))]
  (time (count s))) ;= 0.011696ms

; two ways to create a sequence are cons and list*
; cons a list, 0 is the head value, the range is the list being the tail
(cons 0 (range 1 5)) ;= (0 1 2 3 4)

; list works similarly, taking any number of head values
(list* 0 1 2 3 (range 4 10)) ;= (0 1 2 3 4 5 6 7 8 9)

; lazy seqs are sequences that are evaluated lazily, where results are produced as the result of
; a computation performed on demand when a consumer attempts to access them; as a result each value
; is always computed once, and only once

; the process of accessing a lazy sequence is called realization, when all values in a lazy
; sequence have been computed, it is said to be "fully realized"

; not entirely useful as it's being initialized by a fully realized list
(lazy-seq [1 2 3]) ;= (1 2 3)

; better example is using it to generate a list of random integers, that are realized when asked
; for the next in a list

; potentially generates an infinite amount of ints... be wary to specify how many you need when pulling
; from random-ints(ex: (take 10 (random-ints 1000)))
(defn random-ints
  "Returns a lazy seq of random integers in the range [0..max]."
  [max]
  (lazy-seq
   (println "realizing random number")
   (cons (rand-int max)
         (random-ints max))))

; take 10 from the generated random-ints(generated lazily, realized when called)
; warning: taking an infinite amount of lazy-seq generated values will result in clojure realizing
; an infinite amount of values
(def rands (take 10 (random-ints 50)))
(first rands)
(nth rands 3)
(count rands) ;= 10, realizes a bunch first
(count rands) ;= 10, already realized

; when we define the lazy seq in a var, its contents simply do not exist, until it is "realized"
; or accessed

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

; types
'(:a :b 1) ; list
[:a :b 1] ; vector
{:name "Person's Name"} ; map
#{1 2 3} ; set

; because of effecient implementations, conj to add an item to a list will prepend the value
; weird, neat, necessary to remember. anything else would require traversing the list, which grows
; more costly as n grows.
(conj '(1 2 3) 4) ; '(4 1 2 3)

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

; memoize doc
;
; Returns a memoized version of a referentially transparent function. The
; memoized version of the function keeps a cache of the mapping from arguments
; to results and, when calls with the same arguments are repeated often, has
; higher performance at the expense of higher memory use.

; memoization is built-in(a simple implementation at least). works with referentially transparent
; functions. meaning, you could replace the function/expression with a value and the result would
; be the exact same. example: passing (+ 1 1) is as referentially transparent as 2.

; while memoize is good, it's awful at cache invalidation. for that,
; clojure.core.cache may be better. it offers far more options/functionality for
; the same purpose.

; (let [m-prime? (memoize prime?)]
;   (time (m-prime? 1125899906842679))  ; Elapsed time: 1108.850452 msecs
;   (time (m-prime? 1125899906842679))) ; Elapsed time: 0.014115 msecs

(def v [1 2 3])
(defn test-v
  [ns]
  (into v ns))

; begin
(def l [0 1 2 3 4 5 6 7 8 9])

; (println "Using a list defined as:" l)
; (println "Using thread-last macro to get the tail of list, increase all by 1,"
;  "and print the result.")

; (defn -main
;   []
;   (test-arity (range 1 5)) ; 1 to 5, excludes 5
;   (rd 10)
;   (max 0 10 5 -15 48) ; so many functions take more arguments than expected, for example max
;   (#(filter % ["a" 5 "b" 6]) string?)
;   (#(filter % ["a" 5 "b" 6]) number?) ; same function literal, different predicate, resulting in
;                                       ; different returned vector
;   ; create a partial function mapping multiplication, and calling it with multiple lists as args
;   ((partial map *) [1 2 3] [4 5 6] [7 8 9])
;   (reversed-sum-str 1 5 10 15 20)
;   (reversed-sum-str-f 1 5 10 15 20 25)
;   (test-type-of 1 2 3)
;   (eval (quote '(+ 1 2 3)))) ; evals to (+ 1 2 3), eval it again to get the result: 6

(defn -main
  []
  (println "hello, world."))