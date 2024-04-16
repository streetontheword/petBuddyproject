import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { inject } from '@angular/core';

export const loginGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService)
  const router = inject(Router)

  console.log(">> Running autologinGuard")

  const existingTokenRole = authService.getTokenRole()
  if (existingTokenRole !== null) {
    if (existingTokenRole === "user") {
      console.log(">>AutologinGuard: Redirecting to /user")
      return router.parseUrl('/')
    } else if (existingTokenRole === "admin") {
      console.log(">>AutologinGuard: Redirecting to /admin")
      return router.parseUrl('/dashboard')
    }
  }

  console.log(">> AutoLoginGuard: Allows activate")

  return true;
};