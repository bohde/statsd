(ns statsd.test.core
  (:use [statsd.core])
  (:use [clojure.test]))

(deftest encode-incr
  (is "foo:1|c|@0.5"
      (encode-message "foo" "c" {:value 1, :rate 0.5})))

(deftest should-send-nil
  (is true (should-send nil)))

(deftest ignore-negative-rates
  (is (nil? (normalize-rate -1))))

(deftest ignore-large-rates
  (is (nil? (normalize-rate 2))))

(deftest ignore-nil-rates
  (is (nil? (normalize-rate nil))))



