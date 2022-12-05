package com.inensus.feature_ticket

import com.inensus.feature_ticket.ticket_detail.model.TicketCategoryResponse
import com.inensus.feature_ticket.ticket_detail.model.TicketDetailResponse
import com.inensus.feature_ticket.ticket_form.model.ConfirmTicketRequest
import com.inensus.feature_ticket.ticket_list.model.TicketResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface TicketService {
    @GET
    fun getTickets(@Url url: String): Single<TicketResponse>

    @GET
    fun getTicketDetail(@Url url: String): Single<TicketDetailResponse>

    @GET
    fun getCategories(@Url url: String): Single<TicketCategoryResponse>

    @POST
    fun confirmTicket(@Url url: String, @Body request: ConfirmTicketRequest): Completable
}