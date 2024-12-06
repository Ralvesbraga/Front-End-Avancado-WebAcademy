import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function notBlankValidator(minLength?: number): ValidatorFn {
    return (controle: AbstractControl): ValidationErrors | null => {
        minLength = minLength || 1;
        if (controle.value != null && controle.value != "" && controle.value?.trim().length < minLength) {
            return { notBlank: true }
        }
        return null;
    }
}
