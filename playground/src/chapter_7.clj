;;; clojure for the brave and true
;;;
;;; chapter 7
(ns chapter-7)

;; trivial macro, demonstrating a "sip of its power"
(defmacro backwards
  [form]
  (reverse form))

(backwards (" backwards" " am" "I" str)) ; => "I am backwards."

;; main // lein entry point.
(defn -main
  []
  nil)
