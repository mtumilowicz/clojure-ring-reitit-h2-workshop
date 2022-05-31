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
    * https://github.com/ring-clojure/ring
    * https://metosin.github.io/muuntaja
    * https://github.com/metosin/reitit
    * https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md
    * https://github.com/technomancy/leiningen/blob/stable/sample.project.clj

## ring
* is a Clojure web applications library
* higher level frameworks such as Compojure or lib-noir use Ring as a common basis
* without a basic understanding of Ring, you cannot write middleware, and you may find debugging
your application more difficult
* supports synchronous and asynchronous endpoints and middleware
    * we focus here only on synchronous part
* A web application developed for Ring consists of four components:
    * Handler
        * **synchronous** handlers
            ```
            (defn what-is-my-ip [request]
              {:status 200
               :headers {"Content-Type" "text/plain"}
               :body (:remote-addr request)})
            ```
    * Request
        * represented by Clojure maps
        * some standard keys: `:headers`, `:body`, `:content-type`, `:path-params`
    * Response
        * is created by the handler, and contains three keys: `:status`, `:headers`, `:body`
    * Middleware
        * higher-level functions that add additional functionality to handlers
        * first argument of a middleware function should be a handler, and its return value should be a new
        handler function that will call the original handler
        * threading macro (->) can be used to chain middleware together
            ```
            (def app
              (-> handler
                  (wrap-content-type "text/html")
                  (wrap-keyword-params)
                  (wrap-params)))
            ```
        * muuntaja/wrap-formats
            * negotiates a request body based on accept, accept-charset and content-type headers and decodes the body
            with an attached Muuntaja instance into `:body-params`
            * encodes also the response body
        * query params
            * `[app.gateway.middleware :refer [wrap-params]`
            * adds three new keys to the request map:
                * `:query-params` - A map of parameters from the query string
                * `:form-params` - A map of parameters from submitted form data
                * `:params` - A merged map of all parameters
            * `:query-string "q=clojure"` is transformed into `:query-params {"q" "clojure"}`
## reitit
* fast data-driven router for Clojure
* routes are defined as vectors of String path and optional (non-sequential) route argument
* paths can have path-parameters (:id) or catch-all-parameters (*path)
* example
    ```
    ["/api"
     ["/admin" {:middleware [middleware-wrappers...]}
      ["" admin-handler]
      ["/db" db-handler]]
     ["/ping" ping-handler]]
    ```
    and with router
    ```
    (def router
      (r/router routes))
    ```
* coercion
    * is a process of transforming parameters (and responses) from one format into another
    * by default, all wildcard and catch-all parameters are parsed into strings
    * problem
        ```
        (def router
          (r/router
            ["/:company/users/:user-id" ::user-view])) // :path-params {:company "metosin", :user-id "123"},
        ```
        but we would like to treat user-id as a number, so
        ```
        (def router
          (r/router
            ["/:company/users/:user-id" {:name ::user-view // {:path {:company "metosin", :user-id 123}}
                                         :coercion reitit.coercion.schema/coercion
                                         :parameters {:path {:company s/Str
                                                             :user-id s/Int}}}]
            {:compile coercion/compile-request-coercers}))
        ```
* default handlers
    ```
    (reitit/create-default-handler
          {:not-found
           (constantly (response/not-found "404 - Page not found"))
           :method-not-allowed
           (constantly (response/method-not-allowed "405 - Not allowed"))
           :not-acceptable
           (constantly (response/not-acceptable "406 - Not acceptable"))})))
    ```
* exception handling
    * it's good to add `{:exception pretty/exception}` because it makes routing errors (for example conflicting route)
    increases readability of errors
        * Exceptions thrown in router creation
* reitit.ring.middleware.dev/print-request-diffs
    * prints a request and response diff between each middleware
* Wrapper for Muuntaja middleware for content negotiation, request decoding and response encoding
    * you can compose your new muuntaja instance with as many options as you need
    * example
        ```
        (def new-muuntaja
          (m/create
           (-> m/default-options
               (assoc-in [:formats "application/json" :decoder-opts :bigdecimals] true)
               (assoc-in [:formats "application/json" :encoder-opts :date-format] "yyyy-MM-dd"))))
        ```
* The following request parameters are currently supported:
  type	request source
  :query	:query-params
  :body	:body-params
  :form	:form-params
  :header	:header-params
  :path	:path-params
* Routers can be configured via options.
    * :data	Initial route data

## leiningen
* offers various project-related tasks and can:
    * create new projects
    * fetch dependencies for your project
    * run tests
    * run a fully-configured REPL
    * run the project
    * compile and package projects for deployment
    * run custom automation tasks written in Clojure (leiningen plug-ins)
* Leiningen could be thought of as "Maven meets Ant without the pain"
* A project is a directory containing a group of Clojure (and possibly Java) source files, along with a bit of metadata about them
    * metadata is stored in a file named project.clj in the project's root directory
        * Project name
        * Project description
        * What libraries the project depends on
        * What Clojure version to use
        * Where to find source files
        * What's the main namespace of the app
* Clojure is a hosted language and Clojure libraries are distributed the same way as in other JVM languages: as jar files.
    * :dependencies vector in project.clj
* There are several popular open source repositories. Leiningen by default will use two of them: clojars.org and Maven Central.
    * Clojars is the Clojure community's centralized Maven repository
    * You can add third-party repositories by setting the :repositories key in project.clj
* To pass extra arguments to the JVM, set the :jvm-opts vector
* lein run
* lein test
* profiles
    * add various things into your project map in different contexts
    * For instance, during lein test runs, the contents of the :test profile, if present, will be merged into your project map

## syntax
* records
* atom
* namespaces
    * (:require [clojure.core.match :refer [match]])
* threads
    * ->>, ->, some->, some->>
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
    * if-let
    * nil
* destructuring
    * [{:keys
    * ->> request-map (:body-params)
* REPL
    * how to reload namespace
    * (read-eval-print loop)
    * is an interactive prompt where you can enter arbitrary code to run in the context of your project
* pattern matching
    * clojure.core.match :refer [match]
* defmacro
* loading config
    * cprop.core :refer [load-config]
* testing