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

## syntax
* Records provide some class-like features —well-known fields and constructors
  —to support domain entities.
    * (defrecord Planet [name
      moons
      volume ;; km^3
      mass ;; kg
      aphelion ;; km, farthest from sun
      perihelion ;; km, closest to sun
      ])
    *  there will be a positional factory function
      ( ->Planet ) that expects a value for each attribute in the order specified by defrecord
      and a map factory function ( map->Planet ) that expects a map with keyed values
    * (map->Planet {:name "Earth"
      :moons 1
      :volume 1.08321e12
      :mass 5.97219e24
      :aphelion 152098232
      :perihelion 147098290}))
    * Maps and records both use the standard map collection functions for access
      and modification, but most of the time records are a better choice for domain
      entities.
* destructuring
    * (defn describe-salary [person]
      (let [first (:first-name person)
      last (:last-name person)
      annual (:salary person)]
      (println first last "earns" annual)))
    * (defn describe-salary-2 [{first :first-name
      last :last-name
      annual :salary}]
      (println first last "earns" annual))
    * map destructuring
        * (defn describe-salary-2 [{first :first-name
          last :last-name
          annual :salary}]
          (println first last "earns" annual))
        * Now suppose that you also want to bind a bonus
          percentage, which may or may not exist.
            * (defn describe-salary-3 [{first :first-name
              last :last-name
              annual :salary
              bonus :bonus-percentage :or {bonus 5}}]
            * The value for :or is a map where the bound symbol (here category) is bound to the expression "Category not found". When category is not found in client, it is instead found in the :or map and bound to that value instead.

        * Finally, similar to the case of vectors, map bindings can use the :as option to bind the
          complete hash map to a name.
          * (defn describe-person [{first :first-name
            last :last-name
            bonus :bonus-percentage
            :or {bonus 5}
            :as p}]
        * (defn greet-user [{:keys [first-name last-name]}]
          (println "Welcome," first-name last-name))
* threading
    * ->>, ->, some->, some->>
    * (defn final-amount [principle rate time-periods]
      (* (Math/pow (+ 1 (/ rate 100)) time-periods) principle))
        * function definition is difficult to read, because it’s written inside
                      out, thanks to the prefix nature of Clojure’s syntax
        * (defn final-amount-> [principle rate time-periods]
          (-> rate
          (/ 100)
          (+ 1)
          (Math/pow time-periods)
          (* principle)))
        * What the thread-first macro does is take the first argument supplied and place it in
          the second position of the next expression
        * This is an example of how a macro can
          manipulate code to make it easier to read. Doing something like this is nearly impossi-
          ble in most other languages.
    * thread last
        *  Instead of
          taking the first expression and moving it into the second position of the next expres-
          sion, it moves it into the last place.
        * (defn factorial [n]
          (reduce * (range 1 (+ 1 n))))
        * (defn factorial->> [n]
          (->> n
          (+ 1)
          (range 1)
          (reduce *)))
        * A far more common use of this macro is when working with sequences of data ele-
          ments and using higher-order functions such as map , reduce , and filter
        * Each of
          these functions accepts the sequence as the last element, so the thread-last macro is
          perfect for the job.
    *  introduced two related
      ones called some-> and some->> . These two behave exactly the same as the respec-
      tive ones we just discussed, but computation ends if the result of any step in the
      expansion is nil .
* private methods
    * (defn- update-calories
## polymorphism
* Multimethods vs. Protocols
    * multimethod
        * The defmulti form defines the name and signature of the
          function as well as the dispatch function.
        * Each defmethod form provides a function implementation for a particular dispatch value
    * protocol
        * Protocols also have the ability to group related functions together in a
          single protocol. For these reasons, protocols are usually preferred for type-
          based dispatch.
* Parametric polymorphism
    * You’ve actually already come in contact with polymorphism in Clojure. As you saw in
      chapter 2, functions such as get , conj , assoc , map , into , reduce , and so on accept
      many different types in their arguments but always do the correct thing.
    * Clojure col-
      lections are also polymorphic because they can hold items of any type.
        * This kind of
          polymorphism is called parametric polymorphism because such code mentions only
          parameters and not types
        * it’s also present in
          some statically typed programming languages, both object-oriented languages such as
          Java and C# (where it’s called generics)

## macros
* Macros are the most distinguishing feature of Clojure when compared to languages
  such as Java and Ruby.
* Recall from chapter 1 that the Clojure runtime processes source code differ-
  ently compared to most other languages.
    * Specifically, there’s a read phase followed by
      an evaluation phase.
    * In the first phase, the Clojure reader converts a stream of charac-
      ters (the source code) into Clojure data structures
    * These data structures are then
      evaluated to execute the program. The trick that makes macros possible is that Clo-
      jure offers a hook between the two phases, allowing the programmer to process the
      data structures representing the code before they’re evaluated.
* Macros allow code to be modified programmatically
  before evaluation, making it possible to create whole new kinds of abstractions.
* Since the book The C Programming Language came out, almost all programming lan-
  guage books have used the “Hello, world!” program as an introductory example.
    * There’s a similar tradition when it comes to explaining macros, and it involves adding
      the unless control structure to the language.
    * (defn unless [test then]
      (if (not test)
      then))
    * The reason for this is
      that unless is a function, and all functions execute according to the following rules:
      1 Evaluate all arguments passed to the function call form.
      2 Evaluate the function using the values of the arguments.
    * Rule 1 causes the arguments to be evaluated. In the case of the unless function, those
      are the test and then expressions.
* (defmacro unless [test then]
  (list 'if (list 'not test)
  then))
  * This generates an s-expression of the form (if (not test) then) when the macro is
    expanded.
  * macroexpand-1 is a useful function when writing macros, because it can be used to
    check if the transformation of s-expressions is working correctly.
  * What’s more, such macros are quite common. For instance, Clojure provides when ,
    when-not , cond , if-not , and so on that are all constructs that allow conditional execu-
    tion of code and are all implemented as macros.
  * T EMPLATING USING THE BACKQUOTE ( ` ) MACRO
    * (defmacro unless [test then]
      `(if (not ~test)
      ~then))
    * The back-
      quote starts the template.
    * The template will be expanded into an s-expression and will
      be returned as the return value of the macro.
    * Things that do need to change—say, parameters passed to the macro—are
      unquoted using the ~ character.

* .edn
* nil
* validation
    * struct.core
* syntax
    * clojure.set/rename-keys
    * constantly, #()
    * keyword
    * if-let
* pattern matching
    * clojure.core.match :refer [match]
* loading config
    * cprop.core :refer [load-config]
* testing