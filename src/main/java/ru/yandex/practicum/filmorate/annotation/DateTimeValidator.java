package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeValidator implements ConstraintValidator<IsAfter, LocalDateTime> {
    String valid;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        valid = constraintAnnotation.valid();
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) return false;
        return localDateTime.isAfter(LocalDateTime.parse(valid, DateTimeFormatter.ISO_DATE_TIME));
    }
}
