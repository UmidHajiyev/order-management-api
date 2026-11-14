package com.umid.sales.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** "order" is a reserved word in SQL, so the table is called "orders". */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Sent by the client so that a retried request does not create a second order. */
    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @Column(name = "customer_email", nullable = false, length = 254)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_cents", nullable = false)
    private long totalCents;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> lines = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Order() {
        // for JPA
    }

    public Order(String customerEmail, String idempotencyKey, Instant now) {
        this.customerEmail = customerEmail;
        this.idempotencyKey = idempotencyKey;
        this.status = OrderStatus.PLACED;
        this.totalCents = 0;
        this.createdAt = now;
        this.updatedAt = now;
    }

    /** Adds a line and keeps both sides of the relationship and the total in sync. */
    public void addLine(OrderLine line) {
        line.setOrder(this);
        lines.add(line);
        totalCents += line.getLineTotalCents();
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public long getTotalCents() {
        return totalCents;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
