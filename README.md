# poker-hands

A library to score poker hands in Clojure, inspired by the good times at [Toronto Coding Dojo](http://www.meetup.com/Toronto-Coding-Dojo/).

## Usage

Clone the repo and `lein repl`.

```clojure
(use 'poker-hands.core)

(def hands [(hand "2d 10s 12c 12d 9h") (hand "2h 10h 12h 11h 9h")])
;=> #'user/hands

(-> (first hands) (beats? (second hands)))
;=> false

(map hand-category hands)
;=> (:one-pair :flush)
```

# Known Limitations

* Doesn't know anything about high/low aces, ranks are just numbers

## License

Copyright © 2013 James MacAulay

Distributed under the Eclipse Public License, the same as Clojure.
