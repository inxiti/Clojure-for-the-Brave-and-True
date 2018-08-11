;;; clojure for the brave and true
;;;
;;; chapter 6

(ns chapter-6)

;; demonstrating that symbols map to functions, variables, anything really.
;;

inc ; => function
'inc ; => inc
(map inc [1 2 3]) ; => [2 3 4]
'(map inc [1 2 3]) ; => (map inc [1 2 3])

; this process is called interning a var
(def great-books ["East of Eden" "The Glass Bead Game"])

; => #'chapter-6/great-books

; get a map of the interned vars
(ns-interns *ns*)

(get (ns-interns *ns*) 'great-books) ; => #'chapter-6/great-books

; #' grabs hold of the var corresponding to the symbol that follows it.
; deref vars to get the objects they point to.
(deref #'chapter-6/great-books) ; => ["East of Eden" "The Glass Bead Game"]

; normally, you would just use the symbol to get the contents
great-books ; ["East of Eden" "The Glass Bead Game"]

; you can overwrite bindings, aka: name collisions, so use namespaces to
; avoid it. the following overwrites the previous great-books value.
(def great-books ["The Power of Bees" "Journey to Upstairs"])

;; -----------------------------------------------------------------------------
;; main
;;

(defn -main
  [& args]
  (println "Hello, world"))

