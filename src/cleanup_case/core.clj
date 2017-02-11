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


  "
  (:require [net.cgrand.enlive-html :as html])
  (:gen-class))

(def working-file (atom {}))

(defn tree-from-file [filename]
  (html/html-resource (java.io.StringReader. (slurp filename))))

(defn selectvec
  "selector is a vector"
  [tree selector]
  (mapv html/text
        (html/select tree selector)))

(defn footnote-text [tree]
    (selectvec tree [:p.p24]))


(defn -main
  "in experimenting"
  [& args]
  (do
    (reset! working-file (tree-from-file "nfiborig.html"))
    (let [tree @working-file]
    (println (first (footnote-text tree)))
  ;; (println (slurp "nfiborig.html"))
  )))
