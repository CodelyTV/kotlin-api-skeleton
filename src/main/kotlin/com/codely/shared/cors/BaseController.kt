package com.codely.shared.cors

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMethod

@CrossOrigin(
    allowCredentials = "true",
    originPatterns = ["*"],
    allowedHeaders = ["*"],
    methods = [
        RequestMethod.POST,
        RequestMethod.OPTIONS,
        RequestMethod.GET,
        RequestMethod.PATCH,
        RequestMethod.DELETE
    ]
)
open class BaseController
