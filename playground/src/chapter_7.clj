;;; clojure for the brave and true
;;;
;;; chapter 7
(ns chapter-7)

;; trivial macro, demonstrating a "sip of its power"
(defmacro backwards
  [form]
  (reverse form))

(backwards (" backwards" " am" "I" str)) ; => "I am backwards."

(def addition-list (list + 1 2))
(eval addition-list) ; => 3

;; main // lein entry point.
(defn -main
  []
  nil)
