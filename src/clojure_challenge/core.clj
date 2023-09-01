(ns clojure-challenge.core
  (:gen-class))

(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

(defn satisfies-conditions? [item]
  (let [has-iva? (some #(= (:tax/rate %) 19) (:taxable/taxes item))
        has-retention? (some #(= (:retention/rate %) 1) (:retentionable/retentions item))]
    (and (or has-iva? has-retention?)
         (not (and has-iva? has-retention?)))))

(defn filter-invoice-items [invoice]
  (->> invoice
       :invoice/items
       (filter satisfies-conditions?)
       vec)) ;

(def filtered-items (filter-invoice-items invoice))

(defn -main
  [& args]

  (println "Solution 1 Thread-last Operator ->>")
  (println filtered-items)

  )