(defproject cleanup-case "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [com.novemberain/pantomime "2.9.0"]
                 [reaver "0.1.2"]
                 [enlive "1.1.6"]
                 [me.raynes/conch "0.8.0"]]
  :main ^:skip-aot cleanup-case.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
