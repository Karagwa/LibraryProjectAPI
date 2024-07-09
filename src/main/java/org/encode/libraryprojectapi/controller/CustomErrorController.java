package org.encode.libraryprojectapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    public String getErrorPath(){
        return "/error";
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object handler, Exception ex){
        Integer statusCode = getHttpStatusCode(request);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("statusCode", statusCode);
        mav.addObject("errorMessage", ex.getMessage());

        // You can customize the error view and data based on the exception type (ex)
        // or status code

        return mav;
    }

    private Integer getHttpStatusCode(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        return statusCode;
    }
}




