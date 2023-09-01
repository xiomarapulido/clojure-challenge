(ns clojure-challenge.invoice-item
  (:gen-class))

(defn- discount-factor [{:invoice-item/keys [discount-rate]
                         :or                {discount-rate 0}}]
  (- 1 (/ discount-rate 100.0)))

(defn subtotal
  [{:invoice-item/keys [precise-quantity precise-price discount-rate]
    :as                item
    :or                {discount-rate 0}}]
  (* precise-price precise-quantity (discount-factor item)))
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





