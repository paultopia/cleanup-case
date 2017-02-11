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

(defn footnote-text [filename]
  (let [tree (html/html-resource filename)]
    (mapv html/text
          (html/select tree [:p.p24]))))

(defn -main
  "in experimenting"
  [& args]
  (println (footnote-text "nfiborig.html"))
  ;; (println (slurp "nfiborig.html"))
  )
