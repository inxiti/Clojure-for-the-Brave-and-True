(ns chapter-4)

;; treating lists, vectors, sets, and maps as sequences
;;

(seq '(1 2 3))
(seq [1 2 3])
(seq #{1 2 3}) ;; => (1 2 3)
;; the seq of a map consists a two-element key-value vectors
(seq {:name "Bill Compton" :occupation "Dead mopey guy"})
;; => ([:name "Bill Compton"] [:occupation "Dead mopey guy"])

(seq {:a 1 :b 2 :c 3}) ;; => ([:a 1] [:b 2] [:c 3])

;; convert the seq back into a map using into to stick the result into an
;; emptry map
(into {} (seq {:a 1 :b 2 :c 3})) ;; => {:a 1, :b 2, :c 3}

;; seq function examples
;;

;; map
;;

(map inc [1 2 3]) ;; => (2 3 4)
;; amount is determined by shortest list. if a list was length of 2 the result
;; would be a length of 2 as well, the rest would be discarded.
(map str ["a" "b" "c"] ["A" "B" "C"]) ;; ("aA" "bB" "cC")
;; same as
(list (str "a" "A") (str "b" "B") (str "c" "C"))

(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

;; take two vectors, and combine them into a list of maps
(map unify-diet-data
     human-consumption
     critter-consumption) ;; => ({:human 8.1, :critter 0.0}
                          ;;     {:human 7.3, :critter 0.2}
                          ;;     {:human 6.6, :critter 0.3}
                          ;;     {:human 5.0, :critter 1.1})

(def sum #(reduce + %)) ;; evaluates args each call, so it's slower compared to
                        ;; partial for side-effects free code..
;; (def sum (partial reduce +)) ;; partial evaluates args only once.
(def avg #(/ (sum %) (count %)))
;; stats iterates over a vector of functions, applying each fn to `numbers`
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [3 4 10]) ;; => (17 3 17/3)
(stats [80 1 44 13 6]) ;; => (144 5 144/5)

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities) ;; => ("Bruce Wayne", "Peter Parker", "Your mom",
                       ;;     "Your dad")

;; reduce
;;

(reduce + 0 (range 5))

;; initial value is {}. the associated new-map is returned, and passed into the
;; next iteration of the fn. meanwhile, the next #{key val} pair is passed.
(reduce (fn [new-map
             [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30
         :min 10}) ;; => {:max 31, :min 11}

;; take, drop, take-while, and drop-while
;;
;; take takes everything matching the predicate
;; drop drops everything matching the predicate
;;

;; takes 3 items from the next parameter, first `n` of the lazy sequence
;; returned by range
(take 3 (range 1 1000)) ;; => (1 2 3)
(drop 3 [1 2 3 4 5])

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(take-while #(< (:month %) 3) food-journal)
  ;; => ({:month 1 :day 1 :human 5.3 :critter 2.3}
  ;;     {:month 1 :day 2 :human 5.1 :critter 2.0}
  ;;     {:month 2 :day 1 :human 4.9 :critter 2.1}
  ;;     {:month 2 :day 2 :human 5.0 :critter 2.5})

(drop-while #(< (:month %) 3) food-journal)
  ;; => ({:month 3 :day 1 :human 4.2 :critter 3.3}
  ;;     {:month 3 :day 2 :human 4.0 :critter 3.8}
  ;;     {:month 4 :day 1 :human 3.7 :critter 3.9}
  ;;     {:month 4 :day 2 :human 3.7 :critter 3.6})

;; drop months less than 2, then take months less than 4 from that list(2-3) 
(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))
  ;; => ({:month 2 :day 1 :human 4.9 :critter 2.1}
  ;;     {:month 2 :day 2 :human 5.0 :critter 2.5}
  ;;     {:month 3 :day 1 :human 4.2 :critter 3.3}
  ;;     {:month 3 :day 2 :human 4.0 :critter 3.8})

;; filter and some
;;

(filter #(< (:human %) 5) food-journal)
  ;; => ({:month 2 :day 1 :human 4.9 :critter 2.1}
  ;;     {:month 3 :day 1 :human 4.2 :critter 3.3}
  ;;     {:month 3 :day 2 :human 4.0 :critter 3.8}
  ;;     {:month 4 :day 1 :human 3.7 :critter 3.9}
  ;;     {:month 4 :day 2 :human 3.7 :critter 3.6})

(some #(> (:critter %) 5) food-journal) ;; => nil
(some #(> (:critter %) 3) food-journal) ;; => true

;; using a set as the predicate
(some #{:fred} [:fred :wilma]) ;; => :fred

(some #(and (> (:critter %) 3) %) food-journal)
  ;; => {:month 3 :day 1 :human 4.2 :critter 3.3}

;; sorting
;;

(sort [3 2 1]) ;; => (1 2 3)
(sort-by count ["aaa" "c" "bb"]) ;; => ("c" "bb" "aaa")

;; misc
;;

(concat [1 2] [3 4]) ;; => (1 2 3 4)

;; lazy seq
;;

(def vampire-database
  {0 {:makes-blood-puns? false
      :has-pulse?        true
      :name              "McFishwich"}
   1 {:makes-blood-puns? false
      :has-pulse?        true
      :name              "McMackson"}
   2 {:makes-blood-puns? true
      :has-pulse?        false
      :name              "Damon Salvatore"}
   3 {:makes-blood-puns? true
      :has-pulse?        false
      :name              "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  "Returns record if vampire."
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(def mapped-details (map vampire-related-details (range 0 1000000)))
;; (time (first mapped-details)) ;; => takes 32 seconds because lazy seqs
                                 ;;    are chunked, into 32's.
;; chunking into 32s almost always results in better performance.
;; (println (time (first mapped-details))) ;; => second first, much faster.

;; (println (time (vampire-related-details 0)))
;; (println (identify-vampire vampire-database))

;; (println (identify-vampire (range 0 100000))) ;; => 32s, then answer.

;; infinite sequences
;;
;; lazy sequences aren't realized until they are accessed. their realization is
;; deferred until the time when they are accessed.

(concat (take 8 (repeat "na")) ["Batman!"])
  ;; => (na na na na na na na na Batman!)

(take 3 (repeatedly #(rand-int 10)))
  ;; => (5 3 3) ;; randomly chosen, different every run

;; cons constructs a sequence where x is the first element, and seq is the rest.
;;
;; this appends a lazy sequence onto 0, the 0 is fully realized, where as the
;; rest of the sequence is not, at least until they are accessed.
(defn even-numbers
  "Produces an infinite amount of even numbers beginning at `n`, or 0."
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers)) ;; => (0 2 4 6 8 10 12 14 16 18)

;; collection abstraction
;;

(empty? []) ;; => true
(empty? ["no!"]) ;; => false

;; main
;;

(defn -main
  []
  (println "Chapter 4."))

(-main)
