# poker-hands

A little library to score [poker hands](http://en.wikipedia.org/wiki/List_of_poker_hands) in Clojure, inspired by the good times at [Toronto Coding Dojo](http://www.meetup.com/Toronto-Coding-Dojo/).

## Usage

Clone the repo and `lein repl`.

```clojure
(use '[poker-hands.core :only [hand beats? hand-category score]])

(def hands [(hand "2d 10s 12c 12d 9h")
            (hand "2h 10h 12h 11h 9h")
            (hand "3s 5c 6d 8s 10h")])
;=> #'user/hands

(-> (hands 0) (beats? (hands 2)))
;=> true

(-> (hands 0) (beats? (hands 1)))
;=> false

(map hand-category hands)
;=> (:one-pair :flush :high-card)

(score (hands 0))
;=> [1 12 10 9 2 0]

(->> hands
     (sort-by score)
     (map hand-category))
;=> (:high-card :one-pair :flush)
```

# Known Limitations

* Doesn't know anything about high/low aces, ranks are just numbers

## License

Copyright © 2013 James MacAulay

Distributed under the Eclipse Public License, the same as Clojure.
