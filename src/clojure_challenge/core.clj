(ns clojure-challenge.core
  (:require
    [clojure.spec.alpha :as s]
    [clojure.data.json :as json]
    [clojure-challenge.invoice-spec :as invoice-spec]))

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
       vec)) ;

(def filtered-items (filter-invoice-items invoice))
(defn generate-invoice [file-name]
  (let [invoice-data (slurp file-name)
        invoice-map (json/read-str invoice-data true)]
    (println invoice-map)
    (if (s/valid? ::invoice-spec/invoice invoice-map)
      invoice-map
      nil)))

(defn -main [& args]

  (println "/************ Solution 1 Thread-last Operator ****************/")
  (println filtered-items)
  (println "/************ END ****************/")
  (println "/************ Solution 2: Core Generating Functions ****************/")
  (if (empty? args)
    (println "Please provide the JSON file name.")
    (let [file-name (first args)
          invoice (generate-invoice file-name)]
      (if invoice
        (do
          (println "Invoice generated:")
          (println invoice))
        (println "The invoice file is invalid."))))
  (println "/************ END ****************/"))