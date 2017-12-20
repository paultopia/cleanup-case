(ns cleanup-case.core
  (:import [org.jsoup Jsoup])
  (:gen-class))

(defn file->soup [filename] (Jsoup/parse (slurp filename)))

(defn elements->text [elements]
  (apply str
         (mapv #(.text %) elements)))

(defn get-header-info [soup]
  (let [title (elements->text (.select soup ".co_title"))
        cite (elements->text (.select soup ".co_cites"))
        court (elements->text (.select soup ".co_courtBlock"))
        date (elements->text (.select soup ".co_date"))]
    (str title "\n\n" cite "\n\n" court "\n\n" date)))


(defn -main
  [filename]
  (println (get-header-info (file->soup filename))))
