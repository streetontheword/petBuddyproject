import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { userService } from '../services/user.service';
import { MaterialModule } from '../material/material.module';
import { LocalStorageService } from '../services/local-storage-service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { tick } from '@angular/core/testing';
import { User } from '../model';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'

})
export class LandingComponent implements OnInit {
  hide = true;

  form!: FormGroup
  private fb = inject(FormBuilder)
  private router = inject(Router)
  private userSvc = inject(userService)
  private localStorage = inject(LocalStorageService)
  private authSvc = inject(AuthService)
  jwtHelper: JwtHelperService = inject(JwtHelperService)


  userId!: string

  ngOnInit(): void {
    this.form = this.createForm()
  }




  createForm(): FormGroup {
    return this.fb.group({

      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(6), Validators.maxLength(64)]),

    })

  }




  processForm() {
    if (this.jwtHelper.isTokenExpired(this.localStorage.getToken())) {
      console.log(">> AuthService: Token has expired! Deleting token...")
      this.localStorage.removeToken("token")
    }
    this.localStorage.removeToken("token")

    let value = this.form.value
    // console.info(value)
    this.userSvc.login(value).then((result) => {

      // console.info("this is the result when login",result)

      // this.userId = result
      
      this.localStorage.setItem("token", result.success)
      this.localStorage.setItem("username", result.username)
      this.localStorage.setItem("userid", result.userid)
      this.localStorage.setItem("role", result.role)
      this.localStorage.setItem("isLoggedIn", "true")
      this.localStorage.setItem("userurl", result.userurl)
      
      
      // console.info("useriD", this.userId)
      alert("Login successful!")
      if(result.role === 'admin'){
        this.router.navigate(['/dashboard'])
        this.authSvc.updateUserRole(true); 
        this.localStorage.setItem("isAdmin", "true")
        this.localStorage.setItem("isLoggedIn", "true")
        
      } else {
        this.router.navigate(['/home'])
        this.authSvc.updateUserRole(false)
        // this.localStorage.setItem("isAdmin", "false")
        this.localStorage.setItem("isLoggedIn", "false")

      }

    })
      .catch((err) => {
        alert("Unable to sign in, try again!")
        // this.router.navigate(['/']);
        this.reset()
      });
  }



  reset() {
    this.form.reset()
  }


}
