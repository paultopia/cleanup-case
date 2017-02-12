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

(defn split-opinion [page-html]
  (let [[front body] (str/split page-html #"<p class=\"p\d+\"><b>Opinion</b></p>" 2)
        [opinion footnotes] (str/split body #"<table" 2)]
    opinion))

(defn pre-clean [case-string]
  (let [s1 (str/replace case-string #"<span class=\"s2\"><sup>(\d*)<\/sup><\/span>" "[note $1]") ; note markers
        s2 (str/replace s1 #"p18" "p10") 
        s3 (str/replace s2 #"p21" "p10")
        s4 (str/replace s3 #"p9" "p10")
        final-string (str/replace s4 #"p11" "p10")]

   final-string))


(defn make-tree [hstring]
  (-> hstring html/html-snippet html/html-resource))

(defn trees-from-file [filename]
  (let [htmlfile (pre-clean (slurp filename))
        opinion (split-opinion htmlfile)]
    {:opinion (make-tree opinion) :wholebody (make-tree htmlfile)}))

(defn selectvec
  "selector is a vector"
  [tree selector]
  (mapv html/text
        (html/select tree selector)))

;; footnotes

(defn simple-footnotes [tree]
  (str/join "\n" (mapv html/text (html/select tree [:td :p]))))

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


;; tie it together

(defn extract-citation [whole-tree filename]
  (let [paragraphs (mapv html/text (html/select whole-tree [:p]))
        cite (first (remove str/blank? paragraphs))
        name (subs filename 0 (- (count filename) 5))
        decdate (first (filter #(str/starts-with? % "Decided ") paragraphs))
        decyear (re-find #"\d\d\d\d" decdate)]
    (str name ", " cite " (" decyear ").")))

(defn extract-body-and-footnotes [citation opinion-tree whole-tree]
  (let [base-file (str citation "\n\n" (extract-paragraphs opinion-tree) "\n\n" (simple-footnotes whole-tree))]
    (first (str/split base-file #"End of Document"))))

;;; main for testing and stuff

(defn -main
  "in experimenting"
  [& args]
  (let [filename "nfiborig.html"]
    (reset! working-file (trees-from-file filename))
    (let [opinion (:opinion @working-file)
          wholebody (:wholebody @working-file)
          citation (extract-citation wholebody filename)]
      (spit "test-paragraphs.txt" (extract-body-and-footnotes citation opinion wholebody))
     )))
