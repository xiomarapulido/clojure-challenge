(ns clojure-challenge.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure-challenge.invoice-spec :as invoice-spec]
            [clojure.spec.alpha :as s]
            [clojure.data.json :as json]))

;Exercise 1
(def invoice (clojure.edn/read-string (slurp "invoice.edn")))
(defn filter_invoices? [item]
  (let [has-iva? (some #(= (:tax/rate %) 19) (:taxable/taxes item))
        has-retention? (some #(= (:retention/rate %) 1) (:retentionable/retentions item))]
    (and (or has-iva? has-retention?)
         (not (and has-iva? has-retention?)))))
(defn filter-invoice-items [invoice]
  (->> invoice
       :invoice/items
       (filter filter_invoices?)
       vec))                                                ;
(def filtered-items (filter-invoice-items invoice))
;END Exercise 1

;Exercise 2
(defn load-edn-data [edn-file]
  (-> edn-file
      io/resource
      io/file
      slurp
      edn/read-string))

(defn map-json-to-edn [json-file edn-data]
  (let [json-data (json/read-str (slurp json-file) true)
        invoice-map (assoc-in edn-data [:invoice/items] (:items (:invoice json-data)))]
    (if (s/valid? ::invoice-spec/invoice invoice-map)
      invoice-map
      nil)))


;END Exercise 2

(defn -main []
  ;Exercise 1
  (println filtered-items)
  ;END Exercise 1

  ;Exercise 2
  (let [edn-data (load-edn-data "invoice.edn")
        mapped-data (map-json-to-edn "invoice.json" edn-data)]
    (println (pr-str mapped-data))))
;END Exercise 2

(-main)
