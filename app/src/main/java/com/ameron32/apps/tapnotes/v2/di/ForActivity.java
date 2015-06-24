package com.ameron32.apps.tapnotes.v2.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Qualifier annotation to explicitly differentiate dependencies between application and activity context.
 */
@Qualifier
@Target({ METHOD, CONSTRUCTOR, FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForActivity {
}
