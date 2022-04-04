package com.panxora.gravity.tick.exchange.core

import org.springframework.data.repository.CrudRepository

interface SymbolRespository : CrudRepository<Symbol, Int>{
    fun findByFeedGroupIdAndExchangeName(int: Int, exchangeName: String): List<Symbol>
}