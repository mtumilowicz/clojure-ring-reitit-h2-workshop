(defproject clojure-ring-reitit-h2-workshop "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[ch.qos.logback/logback-classic "1.2.10"]
                 [org.clojure/clojure "1.10.1"]
                 [ring "1.7.1"]
                 [metosin/ring-http-response "0.9.1"]
                 [metosin/muuntaja "0.6.4"]
                 [metosin/reitit "0.3.9"]
                 [funcool/struct "1.4.0"]
                 [org.clojure/core.match "1.0.0"]
                 [cprop "0.1.15"]
                 [mount "0.1.16"]
                 [ring/ring-mock "0.4.0"]
                 [com.h2database/h2 "1.4.200"]
                 [mount "0.1.16"]
                 [conman "0.8.4"]
                 [luminus-migrations "0.6.6"]]
  :repl-options {:init-ns clojure-ring-reitit-h2-workshop.core}
  :main app.application)
