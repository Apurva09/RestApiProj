package com.examples.TradeCoin.Entity;

import javax.persistence.*;

@Entity
@Table(name="coins")
public class Coins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name="price", columnDefinition = "Decimal(10,2) default '00.00'")
    private Double price;
   @Column(name = "balance", columnDefinition = "Decimal(10,2) default '00.00'")
    private Double balance;
   private String category;
    public Coins(){
       // Super();
    }

    public Coins(String name, Double price,Double balance,String category) {
        this.name = name;
        this.price = price;
        this.balance=balance;
        this.category=category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Coins{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", balance=" + balance +
                ", category=" + category +
                '}';
    }
}
