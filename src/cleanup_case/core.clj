(ns cleanup-case.core
  (:require
   [net.cgrand.enlive-html :as html]
   [clojure.string :as str]
   [clojure.java.shell :refer [sh]])
  (:gen-class))

;; utility pieces

(def working-file (atom {}))

(defn split-opinion [page-html]
  (let [[front body] (str/split page-html #"<p class=\"p\d+\"><b>Opinion</b></p>" 2)
        [opinion footnotes] (str/split body #"<table" 2)]
    opinion))

(defn pre-clean [case-string]
  (let [cleaned (str/replace case-string #"<span class=\"s2\"><sup>(\d*)<\/sup><\/span>" "[note $1]")]
   cleaned))

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
  (let [raw-ps (mapv html/text (html/select tree [:p]))
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
  (let [filename "National League of Cities v Usery.html"]
    (reset! working-file (trees-from-file filename))
    (let [opinion (:opinion @working-file)
          wholebody (:wholebody @working-file)
          citation (extract-citation wholebody filename)
          test-rtfname "National Federation of Independent Business v Sebelius.rtf"]
      ;; (spit "test-paragraphs.txt" (extract-body-and-footnotes citation opinion wholebody))
      (sh "textutil" "-convert" "html" test-rtfname)
     )))
