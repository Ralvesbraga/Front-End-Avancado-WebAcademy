import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function diaUtilValidator(): ValidatorFn {
    return (controle: AbstractControl): ValidationErrors | null => {
        const data = new Date(controle.value + 'T00:00:00');
        if (controle.value != null && data.getDay() == 0 || data.getDay() == 6) {
            return { diaUtil: true };
        }
        return null;
    }
}
