package com.duberton.application.port.output

import com.duberton.application.domain.User

interface SaveUserPort {

    fun save(user: User)
}