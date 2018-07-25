(ns fpwd.core)

(def filename "/Users/inxiti/code/clojure-for-the-brave-and-true/fpwd/suspects.csv")
(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns."
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(parse (slurp filename))
  ;; => (["Edward Cullen" "10"]
      ;; ["Bella Swan" "0"]
      ;; ["Charlie Swan" "0"]
      ;; ["Jacob Black" "3"]
      ;; ["Carlisle Cullen" "6"])

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(mapify (parse (slurp filename)))
  ;; => ({:name "Edward Cullen",   :glitter-index 10}
      ;; {:name "Bella Swan",      :glitter-index  0}
      ;; {:name "Charlie Swan",    :glitter-index  0}
      ;; {:name "Jacob Black",     :glitter-index  3}
      ;; {:name "Carlisle Cullen", :glitter-index  6})

(first (mapify (parse (slurp filename))))
  ;; => {:glitter-index 10, :name "Edward Cullen"}

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(glitter-filter 3 (mapify (parse (slurp filename))))
  ;; => ({:name "Edward Cullen",   :glitter-index 10}
      ;; {:name "Jacob Black",     :glitter-index  3}
      ;; {:name "Carlisle Cullen", :glitter-index  6})

;; exercises
;;

;; exercise 4.1
(defn glitter-filter-names
  [minimum-glitter records]
  (map :name (filter #(>= (:glitter-index %) minimum-glitter) records)))

;; exercise 4.2
;; TODO: append a mapped version to the mapified records
(defn append
  "Appends `name`, and `glitter-index` to the list of suspects."
  [name glitter-index records]
  (str records name "," glitter-index "\n"))

;; exercise 4.3
(defn validate
  "Validates `records` contains `keywords`."
  [keywords records]
  records)

;; exercise 4.4
(defn csvifer
  "Returns the mapified records back into a CSV string."
  [records]
  records)