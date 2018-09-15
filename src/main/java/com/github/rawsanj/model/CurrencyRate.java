package com.github.rawsanj.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String name;
    private Double rate;
    private LocalDateTime checkedAt;


    public CurrencyRate(String code, String name, Double rate, LocalDateTime updatedAt) {
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.checkedAt = updatedAt;
    }

    public CurrencyRate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CurrencyRate:" +
                " Code='" + code + '\'' +
                " Name='" + name + '\'' +
                " Rate=" + rate +
                " Checked At=" + checkedAt+".";
    }
}
