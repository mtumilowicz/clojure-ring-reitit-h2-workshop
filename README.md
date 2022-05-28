# clojure-ring-reitit-h2-workshop
* references
    * https://cemerick.com/blog/2011/07/05/flowchart-for-choosing-the-right-clojure-type-definition-form.html
    * https://github.com/omniti-labs/jsend
    * https://stackoverflow.com/questions/10947636/idiomatic-way-to-represent-sum-type-either-a-b-in-clojure
    * https://www.metosin.fi/blog/schema-spec-web-devs/
    * http://funcool.github.io/struct/latest/
    * https://www.manning.com/books/clojure-in-action
    * https://pragprog.com/titles/dswdcloj3/web-development-with-clojure-third-edition/
    * https://pragprog.com/titles/shcloj3/programming-clojure-third-edition/
    * https://www.manning.com/books/the-joy-of-clojure-second-edition
    * https://pragprog.com/titles/vmclojeco/clojure-applied/
    * https://clojuredocs.org/clojure.core

* middleware
    * wrap-formats
* records
* ring
* reitit
* leiningen
* atom
* namespaces
    * (:require [clojure.core.match :refer [match]])
* threads
    * ->>
    * https://clojuredocs.org/clojure.core/-%3E%3E
* .edn
* overriding
    * defprotocol, deftype
    * defmulti, defmethod
* validation: struct
* syntax
    * (let
    * clojure.set/rename-keys
    * constantly, #()
    * keyword
* destructuring
    * [{:keys
    * ->> request-map (:body-params)
* REPL
    * how to reload namespace
* pattern matching
    * clojure.core.match :refer [match]
* defmacro
* loading config
    * cprop.core :refer [load-config]
* testing