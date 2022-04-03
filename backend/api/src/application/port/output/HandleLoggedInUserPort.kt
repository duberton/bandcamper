package com.duberton.application.port.output

import com.duberton.application.domain.User

interface HandleLoggedInUserPort {

    fun execute(user: User)
}