(ns clojure-challenge.invoice-item-test
  (:require [clojure-challenge.invoice-item :as invoice-item]
            [clojure.test :refer [deftest is]]))

(deftest test-subtotal-no-discount
  (let [item {:invoice-item/precise-quantity 2
              :invoice-item/precise-price 10
              :invoice-item/discount-rate 0}]
    (is (= (invoice-item/subtotal item) 20.0))))

(deftest test-subtotal-with-discount
  (let [item {:invoice-item/precise-quantity 3
              :invoice-item/precise-price 15
              :invoice-item/discount-rate 10}]
    (is (= (invoice-item/subtotal item) 40.5))))

(deftest test-subtotal-zero-price
  (let [item {:invoice-item/precise-quantity 5
              :invoice-item/precise-price 0
              :invoice-item/discount-rate 5}]
    (is (= (invoice-item/subtotal item) 0.0))))

(deftest test-subtotal-zero-quantity
  (let [item {:invoice-item/precise-quantity 0
              :invoice-item/precise-price 25
              :invoice-item/discount-rate 10}]
    (is (= (invoice-item/subtotal item) 0.0))))

(deftest test-subtotal-zero-discount
  (let [item {:invoice-item/precise-quantity 4
              :invoice-item/precise-price 8
              :invoice-item/discount-rate 0}]
    (is (= (invoice-item/subtotal item) 32.0))))

(deftest test-subtotal-decimal-price
  (let [item {:invoice-item/precise-quantity 2
              :invoice-item/precise-price 12.5
              :invoice-item/discount-rate 5}]
    (is (= (invoice-item/subtotal item) 23.75))))

