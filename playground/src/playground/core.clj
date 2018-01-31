(ns playground.core)

(defn f
	"Just runs a bunch of random code in order to help me experiment."

	([arg]
	(do
		(str "Goodbye, " arg ".")))

	([]
	(do
		(str "Annnd we're done.")))
)

; no-argument and argument versions

; (defn -main [] (println (f)))
(defn -main [] (println (f "Test-user")))