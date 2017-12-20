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

(defn remove-selector [soup selector]
  (let [newsoup soup
        sel (.select newsoup selector)]
    (.remove sel)
    newsoup))

(defn remove-selectors [soup selectors]
  (let [newsoup soup
        sels (map #(.select newsoup %) selectors)]
    (run! #(.remove %) sels)
    newsoup))

(defn -main
  [filename]
  (let [soup (file->soup filename)]
    (spit "testmultiple.html" (.toString (remove-selectors soup [".co_starPage" ".co_headnotes"])))
    ))
