package br.com.juan.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequiredObjectIsNullException : java.lang.RuntimeException {
    constructor():super("It is not allowed to persist a null object")
    constructor(exception: String?):super(exception)
}