(defproject cleanup-case "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [enlive "1.1.6"]]
  :main ^:skip-aot cleanup-case.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
