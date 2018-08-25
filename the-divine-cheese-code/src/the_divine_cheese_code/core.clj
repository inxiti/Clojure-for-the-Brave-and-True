(ns the-divine-cheese-code.core
  (:require [clojure.java.browse :as browse]
            [the-divine-cheese-code.visualization.svg :refer [xml]])
  (:gen-class))

;; is equivalent to, you don't have to quote ' using `ns`
;; (in-ns 'the-divine-cheese-code.core)
;; (require 'the-divine-cheese-code.visualization.svg)

;; ensure that the SVG code is evaluated
;; (require '[the-divine-cheese-code.visualization.svg :as svg])

;; refer the namespace so that you don't have to use the fully qualified name
;; to reference SVG functions
;; (refer 'the-divine-cheese-code.visualization.svg)


(def heists
  [{:locations "Cologne, Germany"
    :cheese-name "Archbishop Hildebold's Cheese Pretzel"
    :lat 50.95
    :lng 6.97}
   {:locations "Zurich, Switzerland"
    :cheese-name "The Standard Emmental"
    :lat 47.37
    :lng 8.55}
   {:locations "Marseille, France"
    :cheese-name "Le Fromage de Cosquer"
    :lat 43.30
    :lng 5.37}
   {:locations "Zurich, Switzerland"
    :cheese-name "The Lesser Emmental"
    :lat 47.37
    :lng 8.55}
   {:locations "Vatican City"
    :cheese-name "The Cheese of Turin"
    :lat 41.90
    :lng 12.45}])

(defn url
  [filename]
  (str "file:///"
       (System/getProperty "user.dir")
       "/"
       filename))

(defn template
  [contents]
  (str "<style>polyline { fill:none; stroke:#5881d8; stroke-width:3 }</style>"
       contents))

(defn -main
  [& args]
  (let [filename "map.html"]
    (->> heists
         (xml 50 100)
         template
         (spit filename))
    (browse/browse-url (url filename))))
