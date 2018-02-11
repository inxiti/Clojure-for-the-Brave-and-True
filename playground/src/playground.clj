(ns playground)

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
  "
)

; forms
(defn error-message
  "Creates an error message depending on severity."
  [severity]
  (str "error: OH GOD! It's "
    (if (= severity :severe) "SEVERE!" "not that bad.")
  )
)

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

; begin
(def l [0 1 2 3 4 5 6 7 8 9])

(println "Using a list defined as:" l)
(println "Using thread-last macro to get the tail of list, increase all by 1,"
  "and print the result.")

(defn -main [] (println (f l)))
