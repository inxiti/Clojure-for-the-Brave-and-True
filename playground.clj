; my "playground" for experimenting with ideas

(ns :main)

(defn f
	"Just runs a bunch of random code in order to help me experiment."

	([arg]
	(do
		(str "Goodbye, " arg ".")))

	([]
	(do
		(str "Annnd we're done.")))
)

(f "Test-user")
