(defproject cleanup-case "0.1.1"
  :description "clean up cases downloaded from westlaw"
  :url "https://github.com/paultopia/cleanup-case"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.jsoup/jsoup "1.11.2"]]
  :main ^:skip-aot cleanup-case.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
