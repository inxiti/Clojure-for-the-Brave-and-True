(ns chapter-3)

; putting it all together

; vector of maps. each map has the name of the body part, and relative size of
; the body part.
(def asym-hobbit-body-parts
  [{:name "head" :size 3}
   {:name "left-eye" :size 1}
   {:name "left-ear" :size 1}
   {:name "mouth" :size 1}
   {:name "nose" :size 1}
   {:name "neck" :size 2}
   {:name "left-shoulder" :size 3}
   {:name "left-upper-arm" :size 3}
   {:name "chest" :size 10}
   {:name "back" :size 10}
   {:name "left-forearm" :size 3}
   {:name "abdomen" :size 6}
   {:name "left-kidney" :size 1}
   {:name "left-hand" :size 2}
   {:name "left-knee" :size 2}
   {:name "left-thigh" :size 4}
   {:name "left-lower-leg" :size 3}
   {:name "left-achilles" :size 1}
   {:name "left-foot" :size 2}])

(defn matching-part
  "Replaces the `part` with \"right-\", if it contains \"left-\"."
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a `:name`, and `:size`."
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

; uses reduce in place of loop/recur(already existing functionality on part of
; reduce, don't reinvent the whell)
(defn better-symmetrize-body-parts
  [asym-body-parts]
  ; (reduce (fn [final-body-parts part]
  ;           (into final-body-parts (set [part (matching-part part)])))
  (reduce #(into %1
                 (set [%2 (matching-part %2)]))
          []
          asym-body-parts))

;  determine which part of a hobbit is hit
(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

;; exercises
(defn add100
  "Adds 100 to `n`. `n` may be 1, or more arguments."
  [& n]
  (reduce + 100 n))

(defn make-adder
  "Creates a partial function, adding `n` to whatever number is passed to it."
  [n]
  (partial + n))

; main
(defn -main
  []
  #_(better-symmetrize-body-parts asym-hobbit-body-parts)
  #_(hit asym-hobbit-body-parts)
  (println (add100 1 2))
  (println ((make-adder 50) 10)))

(-main)