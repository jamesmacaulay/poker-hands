(ns poker-hands.core
  (:require [clojure.string :as str]))

(defn parse-card
  [string]
  (let [[_ rank suit] (re-find #"(\d+)(\D+)" (name string))]
    [(Integer/parseInt rank) (keyword suit)]))

(defn hand
  [string]
  (map parse-card (str/split string #"\s+")))

(def rank first)
(def suit second)
(def ranks (partial map rank))
(def suits (partial map suit))

(defn rank-frequencies
  "Takes a hand-vector and returns a map of rank ints to frequencies"
  [hand]
  (-> hand ranks frequencies))

(defn n-of-a-kinds
  "Takes a hand-vector and returns a map sorted by descending group size"
  [hand]
  (let [freqs (rank-frequencies hand)
        compare-frequencies (fn [key1 key2]
                              (clojure.core/compare [(freqs key2) key2]
                                                    [(freqs key1) key1]))]
    (->> freqs
         (into (sorted-map-by compare-frequencies)))))

(defn straight?
  [hand]
  (let [sorted-ranks (-> hand ranks sort)
        first-rank (first sorted-ranks)
        expected (range first-rank (+ first-rank (count hand)))]
    (= expected sorted-ranks)))

(defn flush?
  [hand]
  (apply = (suits hand)))

(def category-rank
  {:high-card 0
   :one-pair 1
   :two-pair 2
   :three-of-a-kind 3
   :straight 4
   :flush 5
   :full-house 6
   :four-of-a-kind 7
   :straight-flush 8})

(def regular-secondary-score-fn (comp (partial sort >) ranks))
(def grouped-secondary-score-fn (comp keys n-of-a-kinds))

(defn secondary-score-fn
  [category]
  (if (#{:one-pair :two-pair :three-of-a-kind :full-house :four-of-a-kind} category)
    grouped-secondary-score-fn
    regular-secondary-score-fn))

(defn hand-category
  [hand]
  (let [group-sizes (->> (n-of-a-kinds hand)
                         vals
                         (filter (partial < 1)))]
    (cond (and (straight? hand) (flush? hand))
          :straight-flush
          (= [4] group-sizes)
          :four-of-a-kind
          (= [3 2] group-sizes)
          :full-house
          (flush? hand)
          :flush
          (straight? hand)
          :straight
          (= [3] group-sizes)
          :three-of-a-kind
          (= [2 2] group-sizes)
          :two-pair
          (= [2] group-sizes)
          :one-pair
          :else
          :high-card)))


(defn score
  [hand]
  (let [cat (hand-category hand)
        secondary-score (secondary-score-fn cat)]
    (apply vector
           (category-rank cat)
           (secondary-score hand))))

(defn beats?
  [hand1 hand2]
  (pos? (compare (score hand1) (score hand2))))