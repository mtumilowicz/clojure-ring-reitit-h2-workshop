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
    * https://stackoverflow.com/questions/5459865/how-can-i-throw-an-exception-in-clojure
    * https://stackoverflow.com/questions/7658981/how-to-reload-a-clojure-file-in-repl
    * https://clojure.org/guides/destructuring
    * https://www.braveclojure.com/multimethods-records-protocols
    * https://ilanuzan.medium.com/functional-polymorphism-using-clojures-multimethods-825c6f3666e6
    * https://ilanuzan.medium.com/polymorphism-w-clojure-protocols-396ff472ff3c
    * https://ericnormand.me/mini-guide/deftype-vs-defrecord
    * https://stackoverflow.com/questions/13150568/deftype-vs-defrecord
    * https://stackoverflow.com/questions/37048167/what-is-the-difference-between-macroexpand-and-macroexpand-1-in-clojure

## preface
* it may be worthwhile to refer first (basics): https://github.com/mtumilowicz/clojure-concurrency-stm-workshop
* goals of this workshop
    * introduction into clojure web development: ring, reitit
    * show how to validate function arguments: struct
    * show how to integrate with relational db: conman, mount
    * advanced clojure features: threading, polymorphism, macros, pattern matching
    * modeling domain with records
    * practice destructuring
    * note that in this project we do standard dependency injection (map with dependencies passed
    as a first param) - using components would be subject to different workshops
* workshop plan
    * add PATCH method - to edit person

## syntax
* threading
    * example
        ```
        (- (/ (+ c 3) 2) 1) // difficult to read, because it’s written inside out
        ```
        vs
        ```
        (-> c (+ 3) (/ 2) (- 1))
        ```
    * thread first: ->
        * takes the first argument supplied and place it in the second position of the next expression
        * example of how a macro can manipulate code to make it easier to read
            * something like this is nearly impossible in most other languages
        * useful for pulling nested data structures
            ```
            (-> person :employer :address :city)
            ```
            vs
            ```
            (:city (:address (:employer person)))
            ```
    * thread last: -->
        * takes the first expression and moving it into the last place
        * common use case: working with sequences and using map, reduce and filter
            * these functions accepts the sequence as the last element
    * two related ones: some-> and some->>
        * computation ends if the result of any step in the expansion is nil
* records
    * custom, maplike data types (associate keys with values)
    * example
        ```
        (defrecord Person [fname lname address])
        (defrecord Address [street city state zip])

        (def stu
            (map->Planet {:fname "Stu"
                          :lname "Halloway"
                          :address (map-Address {
                                :street "200 N Mangum"
                                :city "Durham"
                                :state "NC"
                                :zip 27701})
                          })) // factory methods map-EntityName, expects map
        ```
    * most of the time records are a better choice for domain entities than Map
    * internals
        * dynamically generate compiled bytecode for a named class with a set of given fields
        * fields can have type hints, and can be primitive
        * provides a complete implementation of a persistent map
            * value-based equality and hashCode
            * associative support
            * keyword accessors for fields
            * extensible fields (you can assoc keys not supplied with the defrecord definition)
        * deftype vs defrecord
            * deftype creates a bare-bones object which implements a protocol
            * defrecord creates an immutable persistent map which implements a protocol
* destructuring
    * is a way to concisely bind names to the values inside a data structure
    * is broken up into two categories
        * sequential destructuring
            ```
            (let [[x y z] my-vector]
              (println x y z))
            ```
            * if the vector is too small, the extra symbols will be bound to nil
        * associative destructuring
            * without destructuring we have to bind with let
                ```
                (defn greet-user [person]
                      (let [first (:first-name person)
                            last (:last-name person)]
                      (println "Welcome," first last)))
                ```
            * we can destructure it in method declaration
                ```
                (defn greet-user [{first :first-name last :last-name}]
                      (println "Welcome," first last)))
                ```
            * :or - supply a default value if the key is not present
                ```
                (defn greet-user [{first :first-name last :last-name} :or {:first "unknown" :last "unknown"}]
                      (println "Welcome," first last)))
                ```
            * :as - binds whole argument
                ```
                (defn greet-user [{first :first-name last :last-name} :as person]
                      (println "Welcome," first last)))
                ```
            * :keys - if local bindings and keys are the same
                ```
                (defn greet-user [{:keys [first-name last-name]}]
                          (println "Welcome," first-name last-name))
                ```
## polymorphism
* two approaches: multimethods and protocols
    * multimethod
        * example
            ```
            (defmulti make-sound :type)
            (defmethod make-sound :Dog [x] "Woof Woof")
            (defmethod make-sound :Cat [x] "Miauuu")

            (make-sound {:type :Dog}) => "Woof Woof"
            (make-sound {:type :Cat}) => "Miauuu"
            ```
        * `defmulti`: name, signature and dispatch function
            * note that dispatch function can be any function
                ```
                (def QUICK-SORT-THRESHOLD 5)
                (defmulti my-sort (fn [arr]
                                    (if (every? integer? arr)
                                       :counting-sort
                                       (if (< (count arr) QUICK-SORT-THRESHOLD)
                                          :quick-sort
                                          :merge-sort))))
                ```
                * in an OOP language this behavior can’t be implemented in a Polymorphic way
                    * it would have to be a code with an if statement
        * `defmethod`: function implementation for a particular dispatch value
        * default case: if no method is associated with the dispatching value, the multimethod will
        look for a method associated with the default dispatching value (:default), and
        will use that if present
    * protocols
        * replace what in an OOP language we know as interfaces
        * example
            ```
            (defprotocol Shape
              (area [this])
              (perimeter [this]))

            (defrecord Rectangle [width length]
              Shape
              (area [_] (* width length))
              (perimeter [_] (+ (* 2 width) (* 2 length))))

            (defrecord Square [side]
              Shape
              (area [_] (* side side))
              (perimeter [_] (* 4 side)))
            ```
    * protocols are usually preferred for type-based dispatch
        * have the ability to group related functions together in a single protocol

## macros
* most distinguishing feature of Clojure when compared to Java etc
* Clojure runtime processes source code differently compared to other languages
    1. read phase
        * Clojure reader converts a stream of characters (the source code) into Clojure
        data structures
    1. evaluation phase
        * data structures are then evaluated to execute the program
    * Clojure offers a hook between the two phases
        * sallow code to be modified programmatically before evaluation
* hello world example
    * similar tradition when it comes to explaining macros
        * add `unless` control structure to the language
    * without macros it is impossible
        ```
        (defn unless [test then]
            (if (not test)
            then))
        ```
        * all functions execute according to the following rules
            * evaluate all arguments passed to the function call form
            * evaluate the function using the values of the arguments
        * remark: you can pass thunk (function) instead of then to dalay evaluation
            ```
            (defn unless [test then]
                (if (not test)
                (then)))
            ```
    * with macros
        ```
        (defmacro unless [test then]
            `(if (not ~test)
            ~then))
        ```
* syntax
    * templating: backquote (`)
    * inserting value: ~
* verifying macros
    * macroexpand-1
        * if form represents a macro form, returns its expansion, else returns form
    * macroexpand
        * repeatedly calls macroexpand-1 on form until it no longer represents a macro form,
        then returns it
    * macroexpand vs macroexpand-1
        ```
        (defmacro inner-macro [arg]
          `(println ~arg))

        (defmacro top-level-macro [arg]
          `(inner-macro ~arg))

        (macroexpand-1 '(inner-macro "hello")) // (clojure.core/println "hello")
        (macroexpand-1 '(top-level-macro "hello")) // (user/inner-macro "hello")
        (macroexpand '(top-level-macro "hello")) // (clojure.core/println "hello")
        ```
* macros in syntax: when, when-not, cond, if-not, etc

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

* validation
    * struct.core
* syntax
    * clojure.set/rename-keys
    * constantly, #()
* pattern matching
    * clojure.core.match :refer [match]
* loading config
    * cprop.core :refer [load-config]
* testing