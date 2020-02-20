package com.scalepoint.automation.utils.data;

import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.SupplierCompany;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.ExistingSuppliers;
import com.scalepoint.automation.utils.data.entity.SimpleSupplier;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.LogManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataActions {

    public static List<Object> getTestDataParameters(Method method) {
        MDC.put("sessionid", method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> instances = new ArrayList<>(parameterTypes.length);
        try {
            Map<UsersManager.CompanyMethodArgument, User> requestedUsers = UsersManager.fetchUsersWhenAvailable(TESTextractAllCompanyCodesRequested(method));
            Map<Integer, User> indexToUser = requestedUsers.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getIndex(), Map.Entry::getValue));
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (indexToUser.containsKey(i)) {
                    User user = indexToUser.get(i);
                    CurrentUser.setUser(user);
                    instances.add(user);
                } else if (parameterType.equals(SimpleSupplier.class)) {
                    Annotation[] annotations = method.getParameterAnnotations()[i];
                    instances.add(getTestDataForExistingSuppliers(annotations));
                } else {
                    try {
                        instances.add(TestData.Data.getInstance(parameterType));
                    } catch (Exception e) {
                        LogManager.getLogger(BaseTest.class).error(e.getMessage());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LogManager.getLogger(BaseTest.class).error(e.getMessage());
        }
        return instances;
    }

    private static Object getTestDataForExistingSuppliers(Annotation[] annotations) {
        ExistingSuppliers existingSuppliers = (ExistingSuppliers) TestData.Data.getInstance(ExistingSuppliers.class);
        if (annotations.length > 0) {
            Annotation annotation = annotations[0];
            if (annotation.annotationType().equals(SupplierCompany.class)) {
                SupplierCompany supplierCompany = (SupplierCompany) annotation;

                List<SimpleSupplier> simpleSuppliers = existingSuppliers.getSuppliers().stream()
                        .filter(sup -> sup.getInsuranceCompany().equals(supplierCompany.value().name()))
                        .collect(Collectors.toList());

                if (supplierCompany.areWithVouchers()) {
                    return simpleSuppliers.stream().filter(SimpleSupplier::isWithVouchers).findAny().orElseThrow(NoSuchElementException::new);
                } else {
                    return simpleSuppliers.stream().filter(sup -> !sup.isWithVouchers()).findAny().orElseThrow(NoSuchElementException::new);
                }
            }
        }
        return existingSuppliers.getSuppliers().stream().filter(sup -> sup.getInsuranceCompany().equals(CompanyCode.SCALEPOINT.name())).findFirst().orElseThrow(NoSuchElementException::new);
    }

    private static Map<UsersManager.CompanyMethodArgument, User> extractAllCompanyCodesRequested(Method method) {
        Map<UsersManager.CompanyMethodArgument, User> companyCodes = new HashMap<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.equals(User.class)) {
                CompanyCode companyCode = CompanyCode.FUTURE50;
                Annotation[] annotations = method.getParameterAnnotations()[i];
                if (annotations.length > 0) {
                    Annotation annotation = annotations[0];
                    if (annotation.annotationType().equals(UserCompany.class)) {
                        companyCode = ((UserCompany) annotation).value();
                    }
                }
                companyCodes.put(UsersManager.CompanyMethodArgument.create(i, companyCode), null);
            }
        }
        return companyCodes;
    }

    private static Map<UsersManager.CompanyMethodArgument, User> TESTextractAllCompanyCodesRequested(Method method) {
        Map<UsersManager.CompanyMethodArgument, User> companyCodes = new HashMap<>();
        Class<?>[] parameterTypes = method.getParameterTypes();

//        Arrays.stream(method.getParameterTypes())
//                .parallel()
//                .filter(parameterType -> parameterType.equals(User.class))
//                .



        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.equals(User.class)) {
                CompanyCode companyCode = CompanyCode.FUTURE50;
                Annotation[] annotations = method.getParameterAnnotations()[i];
                if (annotations.length > 0) {
                    Annotation annotation = annotations[0];
                    if (annotation.annotationType().equals(UserCompany.class)) {
                        companyCode = ((UserCompany) annotation).value();
                    }
                }
                companyCodes.put(UsersManager.CompanyMethodArgument.create(i, companyCode), null);
            }
        }
        return companyCodes;
    }
}
