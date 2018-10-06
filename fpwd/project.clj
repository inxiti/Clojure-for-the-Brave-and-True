(defproject fpwd "0.1.0-SNAPSHOT"
  :description "fpwd, from clojure for the brave and true"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot fpwd.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
