/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.maculaframework.boot.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p> <b>Length2Validator</b> is Check that a string's length is between min and max. </p>
 *
 * @author Wilson Luo
 * @version $Id: Length2Validator.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-11-25
 */
public class Length2Validator implements ConstraintValidator<Length2, String> {
    private int min;
    private int max;

    @Override
    public void initialize(Length2 parameters) {
        min = parameters.min();
        max = parameters.max();
        validateParameters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int length = getLength2(value);
        return length >= min && length <= max;
    }

    private void validateParameters() {
        if (min < 0) {
            throw new IllegalArgumentException("The min parameter cannot be negative.");
        }
        if (max < 0) {
            throw new IllegalArgumentException("The max parameter cannot be negative.");
        }
        if (max < min) {
            throw new IllegalArgumentException("The length cannot be negative.");
        }
    }

    private int getLength2(String value) {
        int size = 0, length = value.length();
        for (int i = 0; i < length; i++) {
            size += (value.charAt(i) > 127 ? 2 : 1);
        }
        return size;
    }

}
