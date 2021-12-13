package kukekyakya.kukemarket.aop;

import kukekyakya.kukemarket.config.security.guard.AuthHelper;
import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignMemberIdAspect {

    private final AuthHelper authHelper;
    private List<Class<?>> supportedClass = new ArrayList<>();

    @PostConstruct
    public void initSupportedClass() {
        supportedClass.clear();
        supportedClass.add(PostCreateRequest.class);
    }

    @Before("@annotation(kukekyakya.kukemarket.aop.AssignMemberId)")
    public void assignMemberId(JoinPoint joinPoint) {
        Optional<Object> arg = findSupportedArg(joinPoint.getArgs());
        arg.ifPresent(param -> {
            Method setMemberIdMethod = getSetMemberIdMethod(param.getClass());
            invokeSetMemberId(param, setMemberIdMethod);
        });
    }

    private Method getSetMemberIdMethod(Class<?> clazz) {
        try {
            return clazz.getMethod("setMemberId", Long.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeSetMemberId(Object arg, Method setMemberId) {
        try {
            setMemberId.invoke(arg, authHelper.extractMemberId());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Object> findSupportedArg(Object[] args) {
        return Arrays.stream(args).filter(arg -> supports(arg.getClass())).findAny();
    }

    private boolean supports(Class<?> clazz) {
        return supportedClass.contains(clazz);
    }
}
