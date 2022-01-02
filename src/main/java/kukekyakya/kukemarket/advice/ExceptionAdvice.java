package kukekyakya.kukemarket.advice;

import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.info("e = {}", e.getMessage());
        e.printStackTrace();
        return getFailureResponse("exception.code", "exception.msg");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response authenticationEntryPoint() {
        return getFailureResponse("authenticationEntryPoint.code", "authenticationEntryPoint.msg");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException() {
        return getFailureResponse("accessDeniedException.code", "accessDeniedException.msg");
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return getFailureResponse("bindException.code", "bindException.msg", e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return getFailureResponse("loginFailureException.code", "loginFailureException.msg");
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return getFailureResponse("memberEmailAlreadyExistsException.code", "memberEmailAlreadyExistsException.msg");
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return getFailureResponse("memberNicknameAlreadyExistsException.code", "memberNicknameAlreadyExistsException.msg", e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return getFailureResponse("memberNotFoundException.code", "memberNotFoundException.msg");
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return getFailureResponse("roleNotFoundException.code", "roleNotFoundException.msg");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response missingRequestHeaderException(MissingRequestHeaderException e) {
        return getFailureResponse("missingRequestHeaderException.code", "missingRequestHeaderException.msg", e.getHeaderName());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return getFailureResponse("categoryNotFoundException.code", "categoryNotFoundException.msg");
    }

    @ExceptionHandler(CannotConvertNestedStructureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertNestedStructureException e) {
        log.info("e = {}", e.getMessage());
        return getFailureResponse("cannotConvertNestedStructureException.code", "cannotConvertNestedStructureException.msg");
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response postNotFoundException() {
        return getFailureResponse("postNotFoundException.code", "postNotFoundException.msg");
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return getFailureResponse("unsupportedImageFormatException.code", "unsupportedImageFormatException.msg");
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return getFailureResponse("fileUploadFailureException.code", "fileUploadFailureException.msg");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return getFailureResponse("commentNotFoundException.code", "commentNotFoundException.msg");
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response messageNotFoundException() {
        return getFailureResponse("messageNotFoundException.code", "messageNotFoundException.msg");
    }

    private Response getFailureResponse(String codeKey, String messageKey) {
        return Response.failure(getCode(codeKey), getMessage(messageKey, null));
    }

    private Response getFailureResponse(String codeKey, String messageKey, Object... args) {
        return Response.failure(getCode(codeKey), getMessage(messageKey, args));
    }

    private Integer getCode(String key) {
        return Integer.valueOf(messageSource.getMessage(key, null, null));
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
