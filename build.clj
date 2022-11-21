(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.java.io :as io]
            ))

(def class-dir (.getAbsolutePath (io/file "classes")))
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file "dist/hn.jar")

(defn uber
  "Builds an uber jar for the project."
  [{:keys [uber]}]
  ;; copies the source code and resource files into the target
  ;; directory that is the input for the next step:
  (b/copy-dir {:src-dirs (:src-dirs uber)
               :target-dir class-dir})
  ;; compiles the sources files in the target directory:
  (b/compile-clj {:basis basis
                  :class-dir class-dir
                  :ns-compile [(:main uber)]
                  })
  ;; creates an uber jar that also include all dependencies:
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main (:main uber)})
  )

(defn build
  [_]
  (uber {:uber {:src-dirs ["src/"]
                :main 'hn.core
                }}))
