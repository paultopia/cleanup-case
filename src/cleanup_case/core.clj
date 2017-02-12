(ns cleanup-case.core
  "Being lazy and putting everything in this namespace.
  Selectors of relevance in html (extracted from rtf by textutil on osx)

  - text: <p class=\"p10\">

  - footnote anchors in text: <span class=\"s2\"><sup>14</sup></span>

  - name, citation and court in p classes p3-p8

  - headers: p19.  (I don't know if this is consistent, or will depend on doc)
  - footnote references: p23, in a link and a <sup>

  - footnote text: p24

  Really need to double-check and make sure this works for all cases

  If not, I can probably find the anchor for footnote numbers just by searching for the word Footnotes in a table and grabbing the class before that via regex, then incrementing for text.

  label for opinion to split on: <p class=\"p17\"><b>Opinion</b></p>


  "
  (:require
   [net.cgrand.enlive-html :as html]
   [clojure.string :as str])
  (:gen-class))

;; utility pieces

(def working-file (atom {}))

(defn split-on-opinion [page-html]
  (let [[front body] (str/split page-html #"<p class=\"p17\"><b>Opinion</b></p>" 2)]
  {:front front :body body}))

(defn pre-clean [case-string]
  (let [s1 (str/replace case-string #"<span class=\"s2\"><sup>(\d*)<\/sup><\/span>" "[note $1]") ; note markers
        s2 (str/replace s1 #"p18" "p10") ; make opinion author labels real paragraphs
        s3 (str/replace s2 #"p21" "p10") ; make opinion author labels real paragraphs
        final-string (str/replace s3 #"p11" "p10")] ; make opinion author labels real paragraphs
   final-string))


(defn make-tree [hstring]
  (-> hstring html/html-snippet html/html-resource))

(defn trees-from-file [filename]
  (let [{:keys [front body]} (split-on-opinion (pre-clean (slurp filename)))]
    {:front (make-tree front) :body (make-tree body)}))

;; (defn tree-from-file [filename]
;;   (html/html-resource
;;    (java.io.StringReader.
;;     (mark-footnote-refs
;;      (slurp filename)))))

(defn selectvec
  "selector is a vector"
  [tree selector]
  (mapv html/text
        (html/select tree selector)))

;; footnotes (working, complete)

(defn footnote-texts [tree]
    (selectvec tree [:p.p24]))

(defn footnote-numbers [tree]
  (vec (rest (selectvec tree [:p.p23]))))

(defn extract-footnotes [tree]
  (let [nums (footnote-numbers tree)
        texts (footnote-texts tree)
        parabreaks (repeat "\n\n")
        dotspace (repeat ". ")]
    (str "Footnotes\n"
     (str/join
      (interleave nums dotspace texts parabreaks)))))

;; paragraphs

(defn clean-paragraph [paragraph]
  (let [s1 (str/replace paragraph #"\s?\*+\d+\s?" " ")
        cleaned-paragraph (str/replace s1 #"\[\d+\]\s?" "")]
    cleaned-paragraph))

(defn remove-bad-grafs [paragraph-vec]
  (remove str/blank? paragraph-vec))
;; this doesn't actually seem to be doing anything, but I've hacked around it so whev.

(defn extract-paragraphs [tree]
  (let [raw-ps (selectvec tree [:p.p10])
        filtered-ps (remove-bad-grafs raw-ps)
        cleaned-ps (mapv clean-paragraph filtered-ps)]
    (str/join "\n" cleaned-ps)))



;;; main for testing and stuff

(defn -main
  "in experimenting"
  [& args]
  (do
    (reset! working-file (trees-from-file "nfiborig.html"))
    (let [tree (:body @working-file)]
      (spit "test-paragraphs.txt" (extract-paragraphs tree))
  )))
