(ns cleanup-case.core
  "This is inefficient as all hell but does the trick."
  (:require
   [net.cgrand.enlive-html :as html]
   [clojure.string :as str]
   [clojure.java.shell :refer [sh]])
  (:gen-class))

(defn split-opinion [page-html]
  (let [[front body] (str/split page-html #"<p class=\"p\d+\"><b>Opinion</b></p>" 2)
        [opinion footnotes] (str/split body #"<table" 2)]
    opinion))

(defn pre-clean [case-string]
  (let [cleaned (str/replace case-string #"<span class=\"s2\"><sup>(\d*)<\/sup><\/span>" "[note $1]")]
   cleaned))

(defn make-tree [hstring]
  (-> hstring html/html-snippet html/html-resource))

(defn string-from-rtf [filename]
 (:out (sh "textutil" "-convert" "html" filename "-stdout")))

(defn trees-from-string [htmlstring]
  (let [htmlfile (pre-clean htmlstring)
        opinion (split-opinion htmlfile)]
    {:opinion (make-tree opinion) :wholebody (make-tree htmlfile)}))

(defn selectvec
  "selector is a vector"
  [tree selector]
  (mapv html/text
        (html/select tree selector)))

(defn simple-footnotes [tree]
  (str/join "\n" (mapv html/text (html/select tree [:td :p]))))

(defn clean-paragraph [paragraph]
  (let [s1 (str/replace paragraph #"\s?\*+\d+\s?" " ")
        cleaned-paragraph (str/replace s1 #"\[\d+\]\s?" "")]
    cleaned-paragraph))

(defn remove-bad-grafs [paragraph-vec]
  (remove str/blank? paragraph-vec))

(defn extract-paragraphs [tree]
  (let [raw-ps (mapv html/text (html/select tree [:p]))
        filtered-ps (remove-bad-grafs raw-ps)
        cleaned-ps (mapv clean-paragraph filtered-ps)]
    (str/join "\n" cleaned-ps)))

(defn extract-citation [whole-tree name]
  (let [paragraphs (mapv html/text (html/select whole-tree [:p]))
        cite (first (remove str/blank? paragraphs))
        decdate (first (filter #(str/starts-with? % "Decided ") paragraphs))
        decyear (re-find #"\d\d\d\d" decdate)]
    (str name ", " cite " (" decyear ").")))

(defn extract-body-and-footnotes [citation opinion-tree whole-tree]
  (let [base-file (str citation "\n\n" (extract-paragraphs opinion-tree) "\n\n" (simple-footnotes whole-tree))]
    (first (str/split base-file #"End of Document"))))

(defn -main
  [filename]
  (let [htmlstring (string-from-rtf filename)
        {:keys [opinion wholebody]} (trees-from-string htmlstring)
        name (subs filename 0 (- (count filename) 4))
        citation (extract-citation wholebody name)
        newname (str name ".txt")]
    (spit newname (extract-body-and-footnotes citation opinion wholebody))))
