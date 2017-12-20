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

(defn correct-remove-starpages [soup]
  (let [newsoup soup
        starpages (.select newsoup ".co_starPage")]
    (.remove starpages)
    newsoup))

(defn incorrect-remove-starpages [soup]
  (let [starpages (.select soup ".co_starPage")]
    (.remove starpages)
    soup))

(defn -main
  [filename]
  (do
    (spit "testbad.html" (.toString (incorrect-remove-starpages (file->soup filename))))
    (spit "testgood.html" (.toString (correct-remove-starpages (file->soup filename))))
    )
  )
