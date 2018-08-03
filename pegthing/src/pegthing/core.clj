(ns pegthing.core
  (require [clojure.set :as set])
  (:gen-class))

;; example of the board data structure
;;
;;{1  {:pegged true, :connections {6 3, 4 2}},
;; 2  {:pegged true, :connections {9 5, 7 4}},
;; 3  {:pegged true, :connections {10 6, 8 5}},
;; 4  {:pegged true, :connections {13 8, 11 7, 6 5, 1 2}},
;; 5  {:pegged true, :connections {14 9, 12 8}},
;; 6  {:pegged true, :connections {15 10, 13 9, 4 5, 1 3}},
;; 7  {:pegged true, :connections {9 8, 2 4}},
;; 8  {:pegged true, :connections {10 9, 3 5}},
;; 9  {:pegged true, :connections {7 8, 2 5}},
;; 10 {:pegged true, :connections {8 9, 3 6}},
;; 11 {:pegged true, :connections {13 12, 4 7}},
;; 12 {:pegged true, :connections {14 13, 5 8}},
;; 13 {:pegged true, :connections {15 14, 11 12, 6 9, 4 8}},
;; 14 {:pegged true, :connections {12 13, 5 9}},
;; 15 {:pegged true, :connections {13 14, 6 10}},
;; :rows 5}

;; declare allows you to declare functions without first defining them, allowing
;; them to be referenced before they are defined.
(declare successful-move prompt-move game-over query-rows)

(defn tri*
  "Generates lazy sequence of triangular numbers."
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

;; call the lazy sequence and bind it to tri
(def tri (tri*))

;; take, and thus realize, the first 5 elements of tri
;; (take 5 tri) ;; => (1 3 6 10 15)

(defn triangular?
  "Is the number triangular? e.g. 1, 3, 6, 10, 15, 21, etc"
  [n]
  (= n (last (take-while #(>= n %) tri))))

;; (triangular? 5) ;; => false
;; (triangular? 6) ;; => true

(defn row-tri
  "The triangular number at the end of row n"
  [n]
  (last (take n tri)))

;; (row-tri 1) ;; => 1
;; (row-tri 2) ;; => 3
;; (row-tri 3) ;; => 6

(defn row-num
  "Returns row number the position belongs to: pos 1 in row 1,
  positions 2 and 3 in row 2, etc."
  [pos]
  (inc (count (take-while #(> pos %) tri))))

;; (row-num 1) ;; => 1
;; (row-num 3) ;; => 5

(defn connect
  "Form a mutual connection between two positions."
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce
     (fn [new-board
          [p1 p2]]
       (assoc-in new-board [p1 :connections p2] neighbor))
     board
     [[pos destination] [destination pos]])
    board))

;; [1, 4] and [4, 1] become {1 {:connections {4 2}} 4 {:connections {1 2}}}

;; (connect {} 15 1 2 4) ;; => {1 {:connections {4 2}}
                         ;;     4 {:connections {1 2}}}

;; examples of assoc-in: return a new map with the given value at the specified
;; nesting. also of get-in: get a value within a nested associative structure.
(comment
  (assoc-in {} [:cookie :monster :vocals] "Finntroll")
  ;; => {:cookie {:monster {:vocals "Finntroll"}}}
  (get-in {:cookie {:monster {:vocals "Finntroll"}}} [:cookie :monster])
  ;; => {:vocals "Finntroll"}
  (assoc-in {} [1 :connections 4] 2)
  ;; => {1 {:connections {4 2}}}
)
;; connect two positions functions
;;

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor)
                (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

;; (connect {} 15 1) ;; => {1 {:connections { 4 2}}  4 {:connections {1 2}}}
;; (connect [} 15 3) ;; => {3 {:connections {10 6}} 10 {:connections {3 6}}}

(defn add-pos
  "Pegs the position and performs connections."
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board
                 connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-down-left connect-down-right])))

;; (add-pos {} 15 1)
;; => {1 {:pegged true, :connections {4 2, 6 3}},
;;     4 {:connections {1 2}},
;;     6 {:connections {1 3}}}

;; using reduce to define clean from listing 5-1
;; (defn clean
;;   [text]
;;   (reduce (fn [string string-fn] (string-fn string))
;;           text
;;           [clojure.string/trim #(clojure.string/replace % #"lol" "LOL")]))

(defn new-board
  "Creates a new board with the given number of rows."
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos]
              (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))

;; moving pegs
;;
(defn pegged?
  "Does the position have a peg in it?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "Take the peg at a given position out of the board."
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "Put a peg in the board a given position."
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "Take peg out of p1, and place it in p2."
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

(defn valid-moves
  "Return a map of all valid movies for pos, where the key is the destination,
  and the value is the jumped position."
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))

(def my-board (assoc-in (new-board 5) [4 :pegged] false))
;; (valid-moves my-board 11) ;; => {4 7}

(defn valid-move?
  "Return jumped position if the move from p1 to p2 is valid, nil otherwise."
  [board p1 p2]
  (get (valid-moves board p1) p2))

;; (valid-move? my-board 1 4) ;; => 2
;; (valid-move? my-board 8 4) ;; => nil

(defn make-move
  "Move peg from p1 to p2, reomving jumped peg."
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))

;; using a ? indicates that the function is a predicate function, meant to be
;; used in boolean expressions.
(defn can-move?
  "Do any of the pegged positions have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))

;; (can-move? my-board) ;; => {4 2}

;; rendering, and printing the board
;;

;; player interaction
;;

;; -----------------------------------------------------------------------------
;; main
;;

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if (empty? args)
    (println (str "Hello, World!"))
    (println (str "Hello, World!\n\targs: " args))))
