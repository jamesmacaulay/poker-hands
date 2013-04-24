(ns poker-hands.test.core
  (:use clojure.test
        poker-hands.core))

(deftest parse-card-test
  (testing "(parse-card str) returns a card vector"
    (is (= [2 :d]
           (parse-card "2d")))))

(deftest hand-test
  (testing "(hand str) returns a vector of card vectors"
    (is (= [[2 :d] [3 :h] [4 :s] [5 :c] [10 :h]]
           (hand "2d 3h 4s 5c 10h")))))

(deftest ranks-test
  (testing "(ranks hand) returns just the card ranks"
    (is (= [2 3 4 5 10]
           (ranks (hand "2d 3h 4s 5c 10h"))))))

(deftest suits-test
  (testing "(suits hand) returns just the suits"
    (is (= [:d :h :s :c :h]
           (suits (hand "2d 3h 4s 5c 10h"))))))

(deftest n-of-a-kinds-test
  (testing "(n-of-a-kinds hand) returns a sorted map of ranks to group sizes"
    (is (= {2 3, 6 2}
           (n-of-a-kinds (hand "2h 2d 2c 6s 6c"))))
    (is (= {9 2, 11 1, 5 1, 2 1}
           (n-of-a-kinds (hand "2h 11d 5c 9s 9c"))))))

(deftest straight-test
  (testing "(straight? hand) returns a true or false"
    (is (= true
           (straight? (hand "4d 5c 6s 7s 8c"))))
    (is (= false
           (straight? (hand "2h 11d 5c 9s 9c"))))))

(deftest flush-test
  (testing "(flush? hand) returns a true or false"
    (is (= true
           (flush? (hand "2c 9c 5c 9c 11c"))))
    (is (= false
           (flush? (hand "2h 11d 5c 9s 9c"))))))

(deftest hand-category-test
  (testing "(hand-category hand) returns :high-card if you don't have anything"
    (is (= :high-card
           (hand-category (hand "2d 4c 5s 9s 11c")))))
  (testing "(hand-category hand) returns :one-pair if you have a pair"
    (is (= :one-pair
           (hand-category (hand "2d 4c 5s 2s 11c")))))
  (testing "(hand-category hand) returns :two-pair if you have two pairs"
    (is (= :two-pair
           (hand-category (hand "2d 4c 11s 2s 11c")))))
  (testing "(hand-category hand) returns :three-of-a-kind if you have 3 of a kind"
    (is (= :three-of-a-kind
           (hand-category (hand "11d 4c 11s 2s 11c")))))
  (testing "(hand-category hand) returns :straight if you have a straight"
    (is (= :straight
           (hand-category (hand "4d 5c 6s 7s 8c")))))
  (testing "(hand-category hand) returns :flush if you have a flush"
    (is (= :flush
           (hand-category (hand "2c 9c 5c 9c 11c")))))
  (testing "(hand-category hand) returns :full-house if you have a full-house"
    (is (= :full-house
           (hand-category (hand "5s 9s 5c 9c 5d")))))
  (testing "(hand-category hand) returns :four-of-a-kind if you have 4 of a kind"
    (is (= :four-of-a-kind
           (hand-category (hand "5s 9s 5c 5h 5d")))))
  (testing "(hand-category hand) returns :straight-flush if you have a straight flush"
    (is (= :straight-flush
           (hand-category (hand "7h 8h 9h 10h 11h"))))))

(deftest score-test
  (testing "(score hand) returns a vector of six score values in decreasing priority, padded with 0s"
    (is (= [0 8 6 4 3 2]
           (score (hand "8h 6d 4c 3s 2s"))))
    (is (= [(category-rank :straight) 11 10 9 8 7]
           (score (hand "7h 8d 9c 10s 11s"))))
    (is (= [(category-rank :one-pair) 9 11 10 7 0]
           (score (hand "7h 9d 9c 10s 11s"))))))

(deftest beats-test
  (testing "(beats? hand1 hand2) returns true iff hand1 beats hand2"
    (is (= true
           (beats? (hand "7h 8d 9c 10s 11s")
                   (hand "8h 6d 4c 3s 2s"))))
    (is (= false
           (beats? (hand "8h 6d 4c 3s 2s")
                   (hand "7h 8d 9c 10s 11s"))))
    (is (= false
           (beats? (hand "7h 8d 9c 10s 11s")
                   (hand "7h 8d 9c 10s 11s"))))
 
    (is (= true
           (beats? (hand "2d 10s 12c 12d 9h")
                   (hand "3s 5c 6d 8s 10h"))))))

(deftest best5-test
  (testing "(best-hand 5 cards) returns the best 5-card hand"
    (is (= (hand "13d 7d 13h 7s 7c")
           (best-hand 5 (hand "3s 3c 13d 7d 13h 7s 7c"))))))
