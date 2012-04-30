(ns statsd.core
  (:import (java.net InetAddress InetSocketAddress)
           (java.nio ByteBuffer)
           (java.nio.channels DatagramChannel)))


(defn connect
  ([] (connect "localhost" 8125))
  ([hostname port]
     (let [host (. InetAddress getByName hostname)
           address (InetSocketAddress. host port)
           channel (. DatagramChannel open)]
       (fn [text]
         (.send channel
                (. ByteBuffer wrap (.getBytes text "utf-8"))
                address)))))


(defn encode-rate [rate]
  (and rate (str "|@" rate)))


(defn encode-message [stat type {:keys [value rate]}]
  (str stat ":" value "|" type (encode-rate rate)))


(defn normalize-rate [rate]
  (if-not (or (not rate)
              (> 0 rate)
              (<= 1 rate))
    rate))


(defn should-send [rate]
  (or (nil? rate) (< (rand) rate)))


(defn send-stat [stat type {:keys [value rate connection] :or
                            {connection (connect)} :as options}]
  (let [real-rate (normalize-rate rate)
        text (encode-message stat type options)]
    (if (should-send rate)
      (do
        (connection text)
        text))))


(defn timing [stat {:keys [value rate connection] :as options}]
  (send-stat stat "ms" options))


(defn counting [stat {:keys [value rate connection] :as options}]
  (send-stat stat "c" options))


(defn incr
  ([stat] (incr stat {:value 1}))
  ([stat {:keys [value rate connection] :or {value 1} :as options}]
     (counting stat options)))


(defn decr
  ([stat] (decr stat {:value -1}))
  ([stat {:keys [value rate connection] :or {value -1} :as options}]
     (let [negative-value (if (< value 0)
                            value
                            (* -1 value))]
       (counting stat (assoc options :value negative-value)))))


(defn gauge [stat {:keys [value connection] :as options}]
  (send-stat stat "g" (dissoc options :rate)))
