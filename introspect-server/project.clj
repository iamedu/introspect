(defproject introspect-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1896"]
                 ;; Clojurescript
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [shoreleave/shoreleave-pubsub "0.3.0"]
                 ;; Clojure
                 [ring/ring-devel "1.2.0"]
                 [ring/ring-core "1.2.0"]
                 [org.clojure/core.cache "0.6.3"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.clojure/tools.cli "0.2.4"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [io.netty/netty-all "4.0.10.Final"]
                 [http-kit "2.1.12"]
                 [lib-noir "0.7.0"]
                 [compojure "1.1.5"]
                 [korma "0.3.0-RC5"]
                 [org.postgresql/postgresql "9.2-1003-jdbc4"]
                 [ring-anti-forgery "0.3.0"]
                 [fogus/ring-edn "0.2.0"]
                 [ring-anti-forgery "0.3.0"]
                 [amalloy/ring-gzip-middleware "0.1.3"]
                 [com.cemerick/friend "0.2.0"]
                 [ch.qos.logback/logback-core "1.0.13"]
                 [ch.qos.logback/logback-classic "1.0.13"]]
  :source-paths  ["src/clj"]
  :plugins  [[lein-cljsbuild "0.3.3"]]
  :cljsbuild {
    :crossovers [introspect.angular]
    :crossover-path "src/crossover-cljs"
    :builds {
      :main {
            :source-paths ["src/cljs"]
            :compiler {
                      :output-to "resources/public/js/main.js"
                      :externs  ["externs/angular.js"]
                      :optimizations :simple
                      :pretty-print true}}}}
  :min-lein-version "2.0.0"
  :main ^:skip-aot introspect.server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
