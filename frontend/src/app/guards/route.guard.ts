import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../services/auth-service";




export const roleGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService)
    const router = inject(Router)
  
    console.log(">> Running roleGuard")
  
    const expectedRole = route.data['expectedRole']
  
    if (authService.getTokenRole() !== expectedRole) {
       
      console.log(">> RoleGuard: Role does not match!")
      console.log("Role inside token: " + authService.getTokenRole())
      console.log("Expected role: " + expectedRole)
      
      return router.parseUrl('/forbidden')
    }
  
    return true;
  };