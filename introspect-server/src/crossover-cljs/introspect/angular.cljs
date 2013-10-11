(ns introspect.angular)

  (defmacro defangular [op module nm args body]
     `(let [str-args# (apply vector (map name (quote ~args)))
                str-name# (name (quote ~nm))
                         func# (fn ~args ~@body)
                                  full-args# (conj str-args# func#)]
                                       (~op ~module str-name# (to-array full-arg
s#))))

  (defmacro defcontroller [module nm args & body]
     `(defangular .controller ~module ~nm ~args ~body))

  (defmacro defservice [module nm args & body]
     `(defangular .factory ~module ~nm ~args ~body))

  (defmacro defdirective [module nm args & body]
     `(defangular .directive ~module ~nm ~args ~body))
