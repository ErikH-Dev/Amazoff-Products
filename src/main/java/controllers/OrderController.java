package controllers;

import java.util.List;

import dto.OrderRequest;
import entities.Order;
import enums.OrderStatus;
import interfaces.IOrderService;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

@Path("/orders")
public class OrderController {
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createOrder(@Valid OrderRequest orderRequest) {
        return orderService.create(orderRequest)
            .onItem().transform(createdOrder -> Response.ok(createdOrder).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getOrderById(@PathParam("id") int id) {
        return orderService.read(id)
            .onItem().transform(order -> Response.ok(order).build());
    }

    @GET
    @Path("/user/{oauthId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllOrdersByUser(@PathParam("oauthId") int oauthId) {
        return orderService.readAllByUser(oauthId)
            .onItem().transform(orders -> Response.ok(orders).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateOrderStatus(@PathParam("id") int id, String orderStatus) {
        return orderService.updateOrderStatus(id, OrderStatus.valueOf(orderStatus))
            .onItem().transform(updatedOrder -> Response.ok(updatedOrder).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteOrder(@PathParam("id") int id) {
        return orderService.delete(id)
            .onItem().transform(v -> Response.noContent().build());
    }
}