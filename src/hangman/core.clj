;---
; Excerpted from "Programming Clojure, Third Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/shcloj3 for more book information.
;---


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Run the game

;; clj -m hangman.core

(ns hangman.core
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))


(defn new-board
  []
  (vec (repeat 3 (vec (repeat 3 \_)))))

(defn empty-cell?
  [board row col]
  (= \_ (get-in board [row col])))

(defn update-board
  [board row col value]
  (assoc-in board [row col] value))

(defn player-move
  [board row col value]
  (if (empty-cell? board row col)
    (update-board board row col value)
    (do
      (println (str "invalid move" "row: " row " column: " col))
      (println board)
      board)))

(defn print-board [board]
  (println)
  (for [row board] (apply println row))
  (println))

(defn get-command []
  (flush)
  (let [input (.readLine *in*)
        line (str/trim input)]
    (cond
      (str/blank? line) (recur)
      (<= 0 (Integer. line) 2) (Integer. line)
      :else (do
              (println "That is not a valid entry!")
              (recur)))))

(defn human-move []
  (println)
  (print "Enter a row and column: ")
  (flush)
  (let [row  (get-command)
        col (get-command)]
    [row, col]))



(defn computer-move
  [board]
  (first
   (for [row [0 1 2]
         col [0 1 2]
         :when (empty-cell? board row col) ]
     [row col])))


(defn game-ended?
  [board]
  false)

(defn tic-tac-toe-game
  []
  (println "Welcome to Tic-tac-toe shell" )
  (loop [game-board (new-board) player :human]
    (println "The current board is: " game-board)
    (print-board game-board)
    (if (game-ended? game-board)
      (println "done")
      (if (= player :human)
        (let [[row col] (human-move)]
          (recur (player-move game-board row col \x) :computer))
        (let [[row col] (computer-move game-board)]
          (recur (player-move game-board row col \o) :human))))) )

(defn -main [& args]
  (tic-tac-toe-game))




;; (defn valid-letter? [c]
;;   (<= (int \a) (int c) (int \z)))

;; (defonce available-words
;;   (with-open [r (jio/reader "words.txt")]
;;     (->> (line-seq r)
;;          (filter #(every? valid-letter? %))
;;          vec)))

;; (defn rand-word []
;;   (rand-nth available-words))

;; (defprotocol Player
;;   (next-guess [player progress]))

;; (defn new-progress [word]
;;   (repeat (count word) \_))

;; (defn update-progress [progress word guess]
;;   (map #(if (= %1 guess) guess %2) word progress))

;; (defn complete? [progress word]
;;   (= progress (seq word)))

;; (defn report [begin-progress guess end-progress]
;;   (println)
;;   (println "You guessed:" guess)
;;   (if (= begin-progress end-progress)
;;     (if (some #{guess} end-progress)
;;       (println "Sorry, you already guessed:" guess)
;;       (println "Sorry, the word does not contain:" guess))
;;     (println "The letter" guess "is in the word!"))
;;   (println "Progress so far:" (apply str end-progress)))

;; (defn game
;;   [word player & {:keys [verbose] :or {verbose false}}]
;;   (when verbose
;;     (println "You are guessing a word with" (count word) "letters"))
;;   (loop [progress (new-progress word), guesses 1]
;;     (let [guess (next-guess player progress)
;;           progress' (update-progress progress word guess)]
;;       (when verbose (report progress guess progress'))
;;       (if (complete? progress' word)
;;         guesses
;;         (recur progress' (inc guesses))))))
;; ;;;; random-player

;; (defonce letters (mapv char (range (int \a) (inc (int \z)))))

;; (defn rand-letter []
;;   (rand-nth letters))

;; (def random-player
;;   (reify Player
;;     (next-guess [_ progress] (rand-letter))))

;; ;;;; random-memory-player

;; (defrecord ChoicesPlayer [choices]
;;   Player
;;   (next-guess [_ progress]
;;     (let [guess (first @choices)]
;;       (swap! choices rest)
;;       guess)))

;; (defn choices-player [choices]
;;   (->ChoicesPlayer (atom choices)))

;; (defn shuffled-player []
;;   (choices-player (shuffle letters)))

;; (defn alpha-player []
;;   (choices-player letters))

;; (defn freq-player []
;;   (choices-player (seq "etaoinshrdlcumwfgypbvkjxqz")))

;; ;;;; interactive-player

;; (defn take-guess []
;;   (println)
;;   (print "Enter a letter: ")
;;   (flush)
;;   (let [input (.readLine *in*)
;;         line (str/trim input)]
;;     (cond
;;       (str/blank? line) (recur)
;;       (valid-letter? (first line)) (first line)
;;       :else (do
;;               (println "That is not a valid letter!")
;;               (recur)))))

;; #_(def interactive-player
;;   (reify Player
;;     (next-guess [_ progress] (take-guess))))

;; #_(defn -main [& args]
;;   (game (rand-word) interactive-player :verbose true))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;; REPL code

;; (comment
;;   (game "hello" random-player)
;;   (game "hello" (shuffled-player))
;;   (game (rand-word) interactive-player :verbose true)
;;   (game "flask" interactive-player :verbose true)
;;   (game "hello" (alpha-player))
;;   (game "hello" (freq-player))
;;   )
