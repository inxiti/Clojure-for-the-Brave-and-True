(ns the-divine-cheese-code.core
  (:gen-class))

;; ensure that the SVG code is evaluated
(require '[the-divine-cheese-code.visualization.svg :as svg])

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

(defn -main
  "The main function."
  [& args]
  (println (svg/points heists)))
