package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<IsAfter, LocalDate> {
    private String checkDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        checkDate = constraintAnnotation.checkDate();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return false;
        }
        return localDate.isAfter(LocalDate.parse(checkDate, DateTimeFormatter.ISO_DATE));
    }
}
