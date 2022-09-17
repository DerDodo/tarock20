package eu.thelastdodo.tarock20.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

open class GameException: Exception()
class ErrorDetails(
	val type: String
)

@ControllerAdvice
class RestResponseStatusExceptionResolver: ResponseEntityExceptionHandler() {
	@ExceptionHandler(value = [(GameException::class)])
	fun handleGameSetupException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
		val response = ErrorDetails(ex::class.simpleName!!)
		return handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
	}
}
