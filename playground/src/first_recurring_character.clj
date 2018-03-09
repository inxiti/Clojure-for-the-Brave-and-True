; https://www.reddit.com/r/dailyprogrammer/comments/7cnqtw/20171113_challenge_340_easy_first_recurring/
; [2017-11-13] Challenge #340 [Easy] First Recurring Character

(ns first-recurring-character
  (:require [clojure.string :as string]))

(defn first-recurring-character
  "Returns the [index letter] that recurs first in the string."
  [s]
  (let [recurrences (map (fn [[i l]] (string/index-of s l (inc i))) (map-indexed vector s))
        index       (first (remove nil? recurrences))
        letter      (get s index)]
    (when (some? letter)
      [index (str letter)])))

(defn -main
  []
  (first-recurring-character "ABCDEBC")) ;= [5 "B"]
