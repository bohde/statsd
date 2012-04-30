# statsd

A statsd client for Clojure

## Usage

    (ns hellof-world
        (:use [statsd.core]))

    (def conn (connect "statsd.example.com" 8125))

    (incr "foo.bar.baz" {:connection conn})
    (incr "foo.bar.baz" {:value 10 :connection conn})
    (incr "foo.bar.baz" {:value 10 :connection conn :rate 0.1})

    (decr "foo.bar.baz" {:connection stastsd})
    (decr "foo.bar.baz" {:value -10 :connection stastsd})

    (timing "glork" {:value 320 :connection conn})
    (guage "guagor" {:value 333 :connection conn})

## License

Copyright (C) Josh Bohde

Distributed under the Eclipse Public License, the same as Clojure.
