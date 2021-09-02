package com.scalepoint.automation.utils.data;

import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.SupplierCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.ExistingSuppliers;
import com.scalepoint.automation.utils.data.entity.input.SimpleSupplier;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.LogManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataActions {

    public static List<Object> getTestDataParameters(Method method) {

        List<User> requestedUsers = UsersManager.fetchUsersWhenAvailable(extractAllUsersAttributesRequested(method));

        return getTestDataParameters(method, requestedUsers);
    }

    public static List<Object> getTestDataParameters(Method method, List<User> requestedUsers){
        MDC.put("sessionid", method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> instances = new ArrayList<>(parameterTypes.length);
        Iterator<User> requestedUserIterator = requestedUsers.iterator();

        try {

            for (int i = 0; i < parameterTypes.length; i++) {

                Class<?> parameterType = parameterTypes[i];

                if (parameterType.equals(User.class)) {

                    User user = requestedUserIterator.next();
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

    public static List<Object> getTestDataWithExternalParameters(Method method, Object...objects){

        List<Object> list = getTestDataParameters(method);
        list.addAll(Arrays.asList(objects));
        return list;
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

    public static List<UsersManager.RequestedUserAttributes> extractAllUsersAttributesRequested(Method method) {

        LinkedList<UsersManager.RequestedUserAttributes> companyCodes = new LinkedList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {

            Class<?> parameterType = parameterTypes[i];

            if (parameterType.equals(User.class)) {

                Annotation[] annotations = method.getParameterAnnotations()[i];

                companyCodes.add(UsersManager.RequestedUserAttributes.getRequestedUserAttributes(annotations));
            }
        }

        return companyCodes;
    }
}
