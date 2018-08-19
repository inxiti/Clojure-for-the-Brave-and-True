(ns the-divine-cheese-code.visualization.svg)

(defn latlng->point
  "Convert lat/lng map to comma-separated string."
  [latlng]
  (str (:lat latlng) "," (:lng latlng)))

(defn points
  "Prettify the lat, and lng points."
  [locations]
  (clojure.string/join " " (map latlng->point locations)))
