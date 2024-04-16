import { Component, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators, ValidatorFn } from '@angular/forms';
import { userService } from '../services/user.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent implements OnInit{
  isPasswordShown: boolean = true
  invalidLoginMessage: string = ""

  hide = true;



  userForm!: FormGroup
  private fb = inject(FormBuilder)
  private userSvc = inject(userService)
  private router = inject(Router)


ngOnInit(): void {
    this.userForm = this.createForm()
}
isPasswordConditionMet(condition: string) {
  if (this.userForm.get('password')?.hasError('required')) {
    return false
  }
  return !this.userForm.get('password')!.hasError(condition)
}


  createForm(): FormGroup {

    return this.fb.group({

      email: this.fb.control<string>('', [Validators.required, Validators.email, Validators.maxLength(128)]),
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(3), Validators.maxLength(32)]),
      firstName: this.fb.control<string>('', [Validators.maxLength(64)]),
      lastName: this.fb.control<string>('', [Validators.maxLength(64)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(8), this.passwordValidator]),
      passwordConfirm: this.fb.control<string>('', )
    },
    { validators: this.passwordConfirmValidator } 
    )
    
  }
  private passwordConfirmValidator(control: AbstractControl) {
    return control.value.password === control.value.passwordConfirm
      ? null
      : ({ passwordsDoNotMatch: { value: 'error' } } as ValidationErrors);
  }


 
      passwordValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
      const value = control.value
    
      if (!value) {
        return null
      }
    
      const hasUpperCase = /[A-Z]+/.test(value)
    
      const hasLowerCase = /[a-z]+/.test(value)
    
      const hasNumeric = /[0-9]+/.test(value)
    
      if (hasUpperCase && hasLowerCase && hasNumeric) {
        return null
      }
    
      return {
        noUppercase: !hasUpperCase,
        noLowercase: !hasLowerCase,
        noNumeric: !hasNumeric
      }
    
    
  }
  


  reset(){
    this.userForm.reset()
  }

  processForm(){
    const value = this.userForm.value
    console.info("Value from form>>>>>",value)
    this.userSvc.saveUser(value).then((result)=>{
      // console.info("view fetched date", result)
      alert(result.success)
      this.router.navigate(["/login"])
      })
      .catch((err)=>{alert("Unable to create account")})
    }


    // this.newsSvc.saveForm(this.form, this.imageFile)
    // .then((result) => {
    //   console.info("view fetched date", result)
    
  }





